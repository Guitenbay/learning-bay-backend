package wiki.biki.learningbaybackend.web;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.jena.atlas.json.JsonArray;
import org.springframework.web.bind.annotation.*;
import wiki.biki.learningbaybackend.model.KElement;
import wiki.biki.learningbaybackend.service.KElementService;
import wiki.biki.learningbaybackend.service.impl.KElementServiceImpl;

import java.util.ArrayList;
import java.util.Collections;

@RestController
@RequestMapping(value = "/fuseki/knowledge")     // 通过这里配置使下面的映射都在 /fuseki 下
public class KElementController {

    private KElementService kElementService = new KElementServiceImpl();

    @GetMapping("/all")
    public String getKnowledgeList() {
        // 还可以通过@RequestParam从页面中传递参数来进行查询条件或者翻页信息的传递
        ArrayList<KElement> kElementList = kElementService.getKElementList();
        JSONObject json = new JSONObject();
        json.put("res", true);
        json.put("data", kElementList);
        return json.toJSONString();
    }

    @GetMapping
    public String getKnowledge(String uri) {
        JSONObject json = new JSONObject();
        if (uri == null) {
            json.put("res", false);
            return json.toJSONString();
        }
        KElement kElement = kElementService.getKElementByUri(uri);
        json.put("res", true);
        json.put("data", kElement);
        return json.toJSONString();
    }

    @PutMapping
    public String insertKnowledge(@RequestBody KElement kElement) {
        boolean result = kElementService.insert(kElement);
        JSONObject json = new JSONObject();
        json.put("res", result);
        return json.toJSONString();
    }

    @PostMapping
    public String updateKnowledge(@RequestBody KElement kElement) {
        boolean result = kElementService.update(kElement);
        JSONObject json = new JSONObject();
        json.put("res", result);
        return json.toJSONString();
    }

    @DeleteMapping
    public String deleteKnowledge(String uri) {
        JSONObject json = new JSONObject();
        if (uri == null) {
            json.put("res", false);
            return json.toJSONString();
        }
        boolean result = kElementService.delete(uri);
        json.put("res", result);
        return json.toJSONString();
    }

}
