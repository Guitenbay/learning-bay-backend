package wiki.biki.learningbaybackend.web;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.*;
import wiki.biki.learningbaybackend.model.Section;
import wiki.biki.learningbaybackend.service.SectionService;
import wiki.biki.learningbaybackend.service.impl.SectionServiceImpl;

import java.util.ArrayList;

@RestController
@RequestMapping(value = "/fuseki/section")
public class SectionController {
    private SectionService sectionService = new SectionServiceImpl();

    @GetMapping("/all")
    public String getSections() {
        ArrayList<Section> sectionList = sectionService.getSectionList();
        JSONObject json = new JSONObject();
        json.put("res", true);
        json.put("data", sectionList);
        return json.toJSONString();
    }

    @GetMapping
    public String getSection(String uri) {
        Section section = sectionService.getSectionByUri(uri);
        JSONObject json = new JSONObject();
        json.put("res", true);
        json.put("data", section);
        return json.toJSONString();
    }

    @PutMapping
    public String insertSection(@RequestBody Section section) {
        boolean result = sectionService.insert(section);
        return String.format("{ res: %b }", result);
    }

    @PostMapping
    public String updateSection(@RequestBody Section section) {
        boolean result = sectionService.update(section);
        return String.format("{ res: %b }", result);
    }

    @DeleteMapping
    public String deleteSection(String uri) {
        boolean result = sectionService.delete(uri);
        return String.format("{ res: %b }", result);
    }

}