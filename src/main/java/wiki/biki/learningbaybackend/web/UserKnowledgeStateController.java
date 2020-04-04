package wiki.biki.learningbaybackend.web;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.*;
import wiki.biki.learningbaybackend.model.KElement;
import wiki.biki.learningbaybackend.model.UserKnowledgeState;
import wiki.biki.learningbaybackend.model.UserKnowledgeStateList;
import wiki.biki.learningbaybackend.service.KElementService;
import wiki.biki.learningbaybackend.service.UserKnowledgeStateService;
import wiki.biki.learningbaybackend.service.impl.KElementServiceImpl;
import wiki.biki.learningbaybackend.service.impl.UserKnowledgeStateServiceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/fuseki/user/knowledge-state")
public class UserKnowledgeStateController {

    private UserKnowledgeStateService userKnowledgeStateService = new UserKnowledgeStateServiceImpl();
    private KElementService kElementService = new KElementServiceImpl();

    @GetMapping("/all")
    public String getUserKnowledgeStates(String uri) {
        ArrayList<UserKnowledgeState> userKnowledgeStateList = userKnowledgeStateService.getStateListByUserUri(uri);
        JSONObject json = new JSONObject();
        json.put("res", true);
        json.put("data", userKnowledgeStateList);
        return json.toJSONString();
    }

    /**
     * 添加 knowledge-state
     * @param userKnowledgeStateList
     * @return
     */
    @PostMapping
    public String addUserKnowledgeState(@RequestBody UserKnowledgeStateList userKnowledgeStateList) {
        String userUri = userKnowledgeStateList.getUserUri();
        JSONObject json = new JSONObject();
        /*
         * 题目分析后有三种情况：
         * 1. 答案正确，即不存在 state < 0 的 UserKnowledgeState;
         * 2. 答案错误但考察知识点通过，即 targetUserKnowledgeStates 里不存在 state < 0 的 UserKnowledgeState;
         * 3. 答案错误且考察知识点未通过，即 targetUserKnowledgeStates 里存在 state < 0 的 UserKnowledgeState;
         */
        int situation = 0; // 未知状态
        for (UserKnowledgeState knowledgeState : userKnowledgeStateList.getKnowledgeStates()) {
            // 当第一次发现存在 state < 0 的 UserKnowledgeState 时，假定为第二种情况；
            if (situation == 0 && knowledgeState.getState() < 0) situation = 2;
            if (!userKnowledgeStateService.isExist(userUri, knowledgeState.getUri())) {
                // 当第一次发现存在 state < 0 的 UserKnowledgeState 并且是目标知识点时，确定为第三种情况；
                if (situation == 2 && knowledgeState.getState() < 0) situation = 3;
            }
        }
        // 当 situation 没有在循环中被改变时，确定为第一种情况
        if (situation == 0) situation = 1;
        switch (situation) {
            case 1: // 将题目涉及的所有知识点状态都设置为“应用”，即直接更新
                for (UserKnowledgeState knowledgeState : userKnowledgeStateList.getKnowledgeStates()) {
                    boolean result;
                    // 更新状态
                    if (!userKnowledgeStateService.isExist(userUri, knowledgeState.getUri())) {
                        result = userKnowledgeStateService.insert(userUri, knowledgeState);
                    } else {
                        result = userKnowledgeStateService.update(userUri, knowledgeState);
                    }
                    if (!result) {
                        // 更新失败
                        json.put("res", false);
                        return json.toJSONString();
                    }
                }
                break;
            case 2: // 将题目涉及的回答到知识点状态都设置为“应用”，即直接更新，未回答到的都从原来状态下降一级；
                for (UserKnowledgeState knowledgeState : userKnowledgeStateList.getKnowledgeStates()) {
                    boolean result;
                    // 更新状态
                    // 不存在的知识点一定都不会降级，所以直接插入
                    if (!userKnowledgeStateService.isExist(userUri, knowledgeState.getUri())) {
                        result = userKnowledgeStateService.insert(userUri, knowledgeState);
                    } else {
                        if (knowledgeState.getState() < 0) {
                            // 降级
                            int prevState = userKnowledgeStateService.getState(userUri, knowledgeState.getUri());
                            int state = prevState + knowledgeState.getState();
                            knowledgeState.setState(Math.max(state, 0));
                        }
                        result = userKnowledgeStateService.update(userUri, knowledgeState);
                    }
                    if (!result) {
                        // 更新失败
                        json.put("res", false);
                        return json.toJSONString();
                    }
                }
                break;
            case 3: // 将题目涉及的回答到知识点状态不变，即不更新，未回答到的都从原来状态下降一级。
                for (UserKnowledgeState knowledgeState : userKnowledgeStateList.getKnowledgeStates()) {
                    boolean result = true;
                    // 更新状态
                    if (!userKnowledgeStateService.isExist(userUri, knowledgeState.getUri())) {
                        // 只插入要降级的
                        if (knowledgeState.getState() < 0) {
                            knowledgeState.setState(0); // 知识点是考察目标且未回答到，则需要复习，因此设为 0
                            result = userKnowledgeStateService.insert(userUri, knowledgeState);
                        }
                    } else {
                        // 只更新要降级的
                        if (knowledgeState.getState() < 0) {
                            int prevState = userKnowledgeStateService.getState(userUri, knowledgeState.getUri());
                            int state = prevState + knowledgeState.getState();
                            knowledgeState.setState(Math.max(state, 0));
                            result = userKnowledgeStateService.update(userUri, knowledgeState);
                        }
                    }
                    if (!result) {
                        // 更新失败
                        json.put("res", false);
                        return json.toJSONString();
                    }
                }
                break;
            default: break;
        }
        // 对于前置知识点变为“信息提取”，即 0 的知识点进行更新
        if (updateKnowledgeStateByPreviousKnowledge(userUri)) {
            json.put("res", true);
        } else {
            json.put("res", false);
        }
        return json.toJSONString();
    }

    private boolean updateKnowledgeStateByPreviousKnowledge(String userUri) {
        List<UserKnowledgeState> knowledgeStates = userKnowledgeStateService.getStateListByUserUri(userUri);
        // 知识点与状态的映射
        Map<String, Integer> knowledgeStateMap = new HashMap<>();
        // 根知识点
        String ROOT = null;
        // 已学习的知识点前置关系映射
        Map<String, List<String>> knowledgePreviousRelationMap = new HashMap<>();
        // 已学习的知识点后置关系映射
        Map<String, List<String>> knowledgeNextRelationMap = new HashMap<>();
        for (UserKnowledgeState knowledgeState : knowledgeStates) {
            String kElementUri = knowledgeState.getUri();
            // 设置知识点与状态的映射
            if (!knowledgeStateMap.containsKey(kElementUri)) {
                knowledgeStateMap.put(kElementUri, knowledgeState.getState());
            } else return false;
            List<String> previousList = kElementService.getKElementByUri(kElementUri).getPreviousList();
            // 设置已学习的知识点前置关系映射
            if (!knowledgePreviousRelationMap.containsKey(kElementUri)) {
                knowledgePreviousRelationMap.put(kElementUri, previousList);
            } else return false;
            if (previousList == null) {
                ROOT = kElementUri;
                knowledgeNextRelationMap.put(ROOT, new ArrayList<>());
            }
            else {
                // 设置已学习的知识点后置关系映射
                previousList.forEach(previous -> {
                    if (!knowledgeNextRelationMap.containsKey(previous)) {
                        List<String> nextList = new ArrayList<>();
                        nextList.add(kElementUri);
                        knowledgeNextRelationMap.put(previous, nextList);
                    } else knowledgeNextRelationMap.get(previous).add(kElementUri);
                });
            }
        }
        // 更新过程应该从 Root 开始，一层层的向下遍历；
        if (ROOT == null) return false;
        // 接下来需要更新的知识点
        List<String> nextUpdateKnowledgeUris = new ArrayList<>();
        nextUpdateKnowledgeUris.add(ROOT);
        while(nextUpdateKnowledgeUris.size() > 0) {
            String kElementUri = nextUpdateKnowledgeUris.remove(0);
            // 添加接下来需要更新的知识点
            nextUpdateKnowledgeUris.addAll(knowledgeNextRelationMap.get(kElementUri));
            List<String> previousList = knowledgePreviousRelationMap.get(kElementUri);
            if (previousList != null) {
                /* 存在“信息提取”状态    任意“信息提取”状态 */
                boolean exist = false, every = true;
                for (String previous : previousList) {
                    // 若知识点状态为“信息提取”，即 0
                    if (knowledgeStateMap.get(previous) == 0) {
                        exist = true;
                    } else {
                        every = false;
                    }
                }
                boolean result = true;
                // 若任意前置知识点为“信息提取”状态
                if (exist && every) {
                    result = userKnowledgeStateService.update(userUri, new UserKnowledgeState(kElementUri, 0));
                }
                // 若存在前置知识点为“信息提取”状态
                if (exist && !every) {
                    result = userKnowledgeStateService.update(userUri, new UserKnowledgeState(kElementUri, Math.max(knowledgeStateMap.get(kElementUri) - 1, 0)));
                }
                if (!result) return false;
            }
        }
        return true;
    }

    @DeleteMapping
    public String deleteUserKnowledgeState(String userUri, String uri) {
        JSONObject json = new JSONObject();
        if (uri == null || userUri == null) {
            json.put("res", false);
            return json.toJSONString();
        }
        boolean result = false;
        if (userKnowledgeStateService.isExist(userUri, uri)) {
            result = userKnowledgeStateService.delete(userUri, uri);
        }
        json.put("res", result);
        return json.toJSONString();
    }
    
}
