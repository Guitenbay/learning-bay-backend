package wiki.biki.learningbaybackend.web;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.*;
import wiki.biki.learningbaybackend.model.User;
import wiki.biki.learningbaybackend.service.UserService;
import wiki.biki.learningbaybackend.service.impl.UserServiceImpl;

@RestController
@RequestMapping(value = "/fuseki/user")
public class UserController {
    private UserService userService = new UserServiceImpl();

    @PostMapping("/login")
    public String getUser(@RequestBody User user) {
        String uri = userService.getUserUri(user);
        JSONObject json = new JSONObject();
        if (uri == null) {
            json.put("res", false);
        } else {
            json.put("res", true);
            JSONObject data = new JSONObject();
            data.put("uri", uri);
            data.put("username", user.getUsername());
            json.put("data", data);
        }
        return json.toJSONString();
    }

    @PutMapping
    public String insertUser(@RequestBody User user) {
        boolean result = userService.insert(user);
        JSONObject json = new JSONObject();
        json.put("res", result);
        return json.toJSONString();
    }

    @PostMapping
    public String updateUser(@RequestBody User user) {
        boolean result = userService.update(user);
        JSONObject json = new JSONObject();
        json.put("res", result);
        return json.toJSONString();
    }

    /**
     * 只有登录时才能删除账户，所以前端能拿到 uri，此时足够用来删除账户
     * @param uri
     * @return
     */
    @DeleteMapping
    public String deleteUser(String uri) {
        JSONObject json = new JSONObject();
        if (uri == null) {
            json.put("res", false);
            return json.toJSONString();
        }
        boolean result = userService.delete(uri);
        json.put("res", result);
        return json.toJSONString();
    }
}
