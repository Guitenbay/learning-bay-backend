package wiki.biki.learningbaybackend.web;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.*;
import wiki.biki.learningbaybackend.model.UserKnowledgeState;
import wiki.biki.learningbaybackend.model.UserKnowledgeStateList;
import wiki.biki.learningbaybackend.service.UserKnowledgeStateService;
import wiki.biki.learningbaybackend.service.impl.UserKnowledgeStateServiceImpl;

import java.util.ArrayList;

@RestController
@RequestMapping(value = "/fuseki/user/knowledge-state")
public class UserKnowledgeStateController {

    private UserKnowledgeStateService userKnowledgeStateService = new UserKnowledgeStateServiceImpl();

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
        boolean result = false;
        for (UserKnowledgeState knowledgeState : userKnowledgeStateList.getKnowledgeStates()) {
            if (userKnowledgeStateService.isExist(userUri, knowledgeState.getUri())) {
                if (knowledgeState.getState() < 0) {
                    int prevState = userKnowledgeStateService.getState(userUri, knowledgeState.getUri());
                    int state = prevState + knowledgeState.getState();
                    knowledgeState.setState(Math.max(state, 0));
                }
                result = userKnowledgeStateService.update(userUri, knowledgeState);
            } else {
                result = userKnowledgeStateService.insert(userUri, knowledgeState);
            }
            if (!result) break;
        }
        JSONObject json = new JSONObject();
        json.put("res", result);
        return json.toJSONString();
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
