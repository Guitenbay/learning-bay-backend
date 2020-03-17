package wiki.biki.learningbaybackend.web;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.*;
import wiki.biki.learningbaybackend.model.Chapter;
import wiki.biki.learningbaybackend.service.ChapterService;
import wiki.biki.learningbaybackend.service.impl.ChapterServiceImpl;

import java.util.ArrayList;

@RestController
@RequestMapping(value = "/fuseki/chapter")
public class ChapterController {
    private ChapterService chapterService = new ChapterServiceImpl();
    
    @GetMapping("/all")
    public String getChapters() {
        ArrayList<Chapter> chapterList = chapterService.getChapterList();
        JSONObject json = new JSONObject();
        json.put("res", true);
        json.put("data", chapterList);
        return json.toJSONString();
    }

    @GetMapping
    public String getChapter(String uri) {
        Chapter chapter = chapterService.getChapterByUri(uri);
        JSONObject json = new JSONObject();
        json.put("res", true);
        json.put("data", chapter);
        return json.toJSONString();
    }

    @PutMapping
    public String insertChapter(@RequestBody Chapter chapter) {
        boolean result = chapterService.insert(chapter);
        return String.format("{ res: %b }", result);
    }

    @PostMapping
    public String updateChapter(@RequestBody Chapter chapter) {
        boolean result = chapterService.update(chapter);
        return String.format("{ res: %b }", result);
    }

    @DeleteMapping
    public String deleteChapter(String uri) {
        boolean result = chapterService.delete(uri);
        return String.format("{ res: %b }", result);
    }
    
}
