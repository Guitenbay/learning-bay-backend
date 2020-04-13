package wiki.biki.learningbaybackend.web;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.*;
import wiki.biki.learningbaybackend.StateChangeController;
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
     * 判断传入的知识元状态是否都大于 1
     * @param userKnowledgeStateList
     * @return
     */
//    @PostMapping("/could/analyse")
//    public String couldAnalyse(@RequestBody UserKnowledgeStateList userKnowledgeStateList) {
//        String userUri = userKnowledgeStateList.getUserUri();
//        JSONObject json = new JSONObject();
//        for (UserKnowledgeState knowledgeState : userKnowledgeStateList.getKnowledgeStates()) {
//            String knowledgeUri = knowledgeState.getUri();
//            if (!userKnowledgeStateService.isExist(userUri, knowledgeUri)
//                    || userKnowledgeStateService.getState(userUri, knowledgeUri) < 1) {
//                json.put("res", false);
//                return json.toJSONString();
//            }
//        }
//        json.put("res", true);
//        return json.toJSONString();
//    }

    /**
     * 更新 knowledge-state 状态为 1 （理解）
     * @param userKnowledgeStateList
     * @return
     */
    @PostMapping("/lesson")
    public String updateUserKnowledgeState(@RequestBody UserKnowledgeStateList userKnowledgeStateList) {
        String userUri = userKnowledgeStateList.getUserUri();
        JSONObject json = new JSONObject();
        /*
         * 课时学完打卡情况下，知识元当前状态有 5 种情况：
         * 1. 未学习，不存在 UserKnowledgeState
         * 2. 已学习未掌握，状态为 0
         * 3. 理解，状态为 1
         * 4. 缓冲，状态为 2
         * 5. 应用，状态为 3
         * 第 1，2 种情况下，需要更新知识元状态为 1，其他情况不更新
         */
        for (UserKnowledgeState knowledgeState : userKnowledgeStateList.getKnowledgeStates()) {
            String knowledgeUri = knowledgeState.getUri();
            boolean result = true;
            if (!userKnowledgeStateService.isExist(userUri, knowledgeUri)) {
                result = userKnowledgeStateService.insert(userUri, knowledgeState);
            } else if (userKnowledgeStateService.getState(userUri, knowledgeUri) == 0) {
                result = userKnowledgeStateService.update(userUri, knowledgeState);
            }
            if (!result) {
                // 更新失败
                json.put("res", false);
                return json.toJSONString();
            }
        }
        json.put("res", true);
        return json.toJSONString();
    }

    /**
     * 代码题分析后，更新 knowledge-state
     * @param userKnowledgeStateList
     * @return
     */
    @PostMapping("/code")
    public String updateUserKnowledgeStateAfterAnalysing(@RequestBody UserKnowledgeStateList userKnowledgeStateList) {
        String userUri = userKnowledgeStateList.getUserUri();
        JSONObject json = new JSONObject();
        /*
         * 代码题中的知识元当前状态有 5 种情况：
         * 1. 未学习，不存在 UserKnowledgeState
         * 2. 已学习未掌握，状态为 0
         * 3. 理解，状态为 1
         * 4. 缓冲，状态为 2
         * 5. 应用，状态为 3
         */
        for (UserKnowledgeState knowledgeState : userKnowledgeStateList.getKnowledgeStates()) {
            // 降级
            if (knowledgeState.getState() < 0) {
                int state = StateChangeController.decreaseStateFrom(userKnowledgeStateService.getState(userUri, knowledgeState.getUri()));
                knowledgeState.setState(Math.max(state, 0));
            }
            boolean result;
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
//        /*
//         * 题目分析后有三种情况：
//         * 1. 答案正确，即不存在 state < 0 的 UserKnowledgeState;
//         * 2. 答案错误但考察知识元通过，即 targetUserKnowledgeStates 里不存在 state < 0 的 UserKnowledgeState;
//         * 3. 答案错误且考察知识元未通过，即 targetUserKnowledgeStates 里存在 state < 0 的 UserKnowledgeState;
//         * 若不存在考察知识元且回答错误，认为为第二种情况
//         */
//        int situation = 0; // 未知状态
//        for (UserKnowledgeState knowledgeState : userKnowledgeStateList.getKnowledgeStates()) {
//            int updateState = knowledgeState.getState();
//            // 当第一次发现存在 state < 0 的 UserKnowledgeState 时，假定为第二种情况；
//            if (situation == 0 && updateState < 0) situation = 2;
//            if (userKnowledgeStateService.getState(userUri, knowledgeState.getUri()) == 1) {
//                // 表示需要更新状态的知识元是理解状态的知识元，此时这类知识元为目标知识元
//                // 当第一次发现存在 state < 0 的 UserKnowledgeState 并且是目标知识元时，确定为第三种情况；
//                if (situation == 2 && updateState < 0) situation = 3;
//            }
//        }
//        // 当 situation 没有在循环中被改变时，确定为第一种情况
//        if (situation == 0) situation = 1;
//        switch (situation) {
//            case 1: // 将题目涉及的所有知识元状态都设置为“应用”，即直接更新
//                for (UserKnowledgeState knowledgeState : userKnowledgeStateList.getKnowledgeStates()) {
//                    boolean result;
//                    // 更新状态
//                    if (!userKnowledgeStateService.isExist(userUri, knowledgeState.getUri())) {
//                        result = userKnowledgeStateService.insert(userUri, knowledgeState);
//                    } else {
//                        result = userKnowledgeStateService.update(userUri, knowledgeState);
//                    }
//                    if (!result) {
//                        // 更新失败
//                        json.put("res", false);
//                        return json.toJSONString();
//                    }
//                }
//                break;
//            case 2: // 将题目涉及的回答到知识元状态都设置为“应用”，即直接更新，未回答到的分类讨论状态下降；
//                for (UserKnowledgeState knowledgeState : userKnowledgeStateList.getKnowledgeStates()) {
//                    boolean result;
//                    // 更新状态
//                    // 不存在的知识元一定都不会降级，所以直接插入
//                    if (!userKnowledgeStateService.isExist(userUri, knowledgeState.getUri())) {
//                        result = userKnowledgeStateService.insert(userUri, knowledgeState);
//                    } else {
//                        if (knowledgeState.getState() < 0) {
//                            // 降级
//                            int currentState = userKnowledgeStateService.getState(userUri, knowledgeState.getUri());
//                            int state = 0;
//                            // 当前知识元不为缓冲状态
//                            if (currentState != 2) {
//                                state = currentState - 1;
//                            }
//                            knowledgeState.setState(Math.max(state, 0));
//                        }
//                        result = userKnowledgeStateService.update(userUri, knowledgeState);
//                    }
//                    if (!result) {
//                        // 更新失败
//                        json.put("res", false);
//                        return json.toJSONString();
//                    }
//                }
//                break;
//            case 3: // 将题目涉及的回答到知识元状态不变，即不更新，未回答到的分类讨论状态下降；
//                for (UserKnowledgeState knowledgeState : userKnowledgeStateList.getKnowledgeStates()) {
//                    boolean result = true;
//                    // 更新状态
//                    if (!userKnowledgeStateService.isExist(userUri, knowledgeState.getUri())) {
//                        // 只插入要降级的
//                        if (knowledgeState.getState() < 0) {
////                            knowledgeState.setState(0); // 知识元是考察目标且未回答到，则需要复习，因此设为 0
//                            int currentState = userKnowledgeStateService.getState(userUri, knowledgeState.getUri());
//                            int state = 0;
//                            // 当前知识元不为缓冲状态
//                            if (currentState != 2) {
//                                state = currentState - 1;
//                            }
//                            knowledgeState.setState(Math.max(state, 0));
//
//                            result = userKnowledgeStateService.insert(userUri, knowledgeState);
//                        }
//                    } else {
//                        // 只更新要降级的
//                        if (knowledgeState.getState() < 0) {
//                            int prevState = userKnowledgeStateService.getState(userUri, knowledgeState.getUri());
//                            int state = prevState + knowledgeState.getState();
//                            knowledgeState.setState(Math.max(state, 0));
//                            result = userKnowledgeStateService.update(userUri, knowledgeState);
//                        }
//                    }
//                    if (!result) {
//                        // 更新失败
//                        json.put("res", false);
//                        return json.toJSONString();
//                    }
//                }
//                break;
//            default: break;
//        }
        // 对于前置知识元变为“未掌握”，即 0 的知识元进行更新
        if (decreaseKnowledgeStateByPreviousKnowledge(userUri)) {
            json.put("res", true);
        } else {
            json.put("res", false);
        }
        return json.toJSONString();
    }

    private boolean decreaseKnowledgeStateByPreviousKnowledge(String userUri) {
        List<UserKnowledgeState> knowledgeStates = userKnowledgeStateService.getStateListByUserUri(userUri);
        // 知识元与状态的映射
        Map<String, Integer> knowledgeStateMap = new HashMap<>();
        // 根知识元
        String ROOT = null;
        // 已学习的知识元前置关系映射
        Map<String, List<String>> knowledgePreviousRelationMap = new HashMap<>();
        // 已学习的知识元后置关系映射
        Map<String, List<String>> knowledgeNextRelationMap = new HashMap<>();
        for (UserKnowledgeState knowledgeState : knowledgeStates) {
            String kElementUri = knowledgeState.getUri();
            // 设置知识元与状态的映射
            if (!knowledgeStateMap.containsKey(kElementUri)) {
                knowledgeStateMap.put(kElementUri, knowledgeState.getState());
            } else return false;
            List<String> previousList = kElementService.getKElementByUri(kElementUri).getPreviousList();
            // 设置已学习的知识元前置关系映射
            if (!knowledgePreviousRelationMap.containsKey(kElementUri)) {
                knowledgePreviousRelationMap.put(kElementUri, previousList);
            } else return false;
            if (previousList == null) {
                ROOT = kElementUri;
                knowledgeNextRelationMap.put(ROOT, new ArrayList<>());
            }
            else {
                // 设置已学习的知识元后置关系映射
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
        // 接下来需要更新的知识元
        List<String> nextUpdateKnowledgeUris = new ArrayList<>();
        nextUpdateKnowledgeUris.add(ROOT);
        while(nextUpdateKnowledgeUris.size() > 0) {
            String kElementUri = nextUpdateKnowledgeUris.remove(0);
            // 添加接下来需要更新的知识元
            List<String> nextLevelKnowledgeUris = knowledgeNextRelationMap.get(kElementUri);
            if (nextLevelKnowledgeUris != null) {
                nextUpdateKnowledgeUris.addAll(nextLevelKnowledgeUris);
            }
            List<String> previousList = knowledgePreviousRelationMap.get(kElementUri);
            if (previousList != null) {
                /* 存在“未掌握”状态    任意“未掌握”状态 */
                boolean exist = false, every = true;
                for (String previous : previousList) {
                    // 若知识元状态为“未掌握”，即 0
                    if (knowledgeStateMap.get(previous) == 0) {
                        exist = true;
                    } else {
                        every = false;
                    }
                }
                boolean result = true;
                // 若任意前置知识元为“未掌握”状态
                if (exist && every) {
                    // 则当前知识元也为“未掌握”状态
                    result = userKnowledgeStateService.update(userUri, new UserKnowledgeState(kElementUri, 0));
                }
                // 若存在前置知识元为“未掌握”状态
                if (exist && !every) {
                    int state = StateChangeController.decreaseStateFrom(knowledgeStateMap.get(kElementUri));
                    result = userKnowledgeStateService.update(userUri, new UserKnowledgeState(kElementUri, Math.max(state, 0)));
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
