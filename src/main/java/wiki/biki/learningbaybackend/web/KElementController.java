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
        // 还可以通过@RequestParam从页面中传递参数来进行查询条件或者翻页信息的传递
        KElement kElement = kElementService.getKElementByUri(uri);
        JSONObject json = new JSONObject();
        json.put("res", true);
        json.put("data", kElement);
        return json.toJSONString();
    }

    @PutMapping
    public String insertKnowledge(@RequestBody KElement kElement) {
        boolean result = kElementService.insert(kElement);
        return String.format("{ res: %b }", result);
    }

    @PostMapping
    public String updateKnowledge(@RequestBody KElement kElement) {
        boolean result = kElementService.update(kElement);
        return String.format("{ res: %b }", result);
    }

    @DeleteMapping
    public String deleteKnowledge(String uri) {
        boolean result = kElementService.delete(uri);
        return String.format("{ res: %b }", result);
    }

}
