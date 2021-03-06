package wiki.biki.learningbaybackend.web;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.*;
import wiki.biki.learningbaybackend.model.Lesson;
import wiki.biki.learningbaybackend.service.LessonService;
import wiki.biki.learningbaybackend.service.impl.LessonServiceImpl;

import java.util.ArrayList;

@RestController
@RequestMapping(value = "/fuseki/lesson")
public class LessonController {
    private LessonService lessonService = new LessonServiceImpl();

    @GetMapping("/all")
    public String getLessons() {
        ArrayList<Lesson> lessonList = lessonService.getLessonList();
        JSONObject json = new JSONObject();
        json.put("res", true);
        json.put("data", lessonList);
        return json.toJSONString();
    }

    @GetMapping("/all/chapter")
    public String getLessonsByChapterUri(String uri) {
        ArrayList<Lesson> lessonList = lessonService.getLessonListByChapterUri(uri);
        JSONObject json = new JSONObject();
        json.put("res", true);
        json.put("data", lessonList);
        return json.toJSONString();
    }

    @GetMapping
    public String getLesson(String uri) {
        JSONObject json = new JSONObject();
        if (uri == null) {
            json.put("res", false);
            return json.toJSONString();
        }
        Lesson lesson = lessonService.getLessonByUri(uri);
        json.put("res", true);
        json.put("data", lesson);
        return json.toJSONString();
    }

    @PutMapping
    public String insertLesson(@RequestBody Lesson lesson) {
        boolean result = lessonService.insert(lesson);
        JSONObject json = new JSONObject();
        json.put("res", result);
        return json.toJSONString();
    }

    @PostMapping
    public String updateLesson(@RequestBody Lesson lesson) {
        boolean result = lessonService.update(lesson);
        JSONObject json = new JSONObject();
        json.put("res", result);
        return json.toJSONString();
    }

    @DeleteMapping
    public String deleteLesson(String uri) {
        JSONObject json = new JSONObject();
        if (uri == null) {
            json.put("res", false);
            return json.toJSONString();
        }
        boolean result = lessonService.delete(uri);
        json.put("res", result);
        return json.toJSONString();
    }

}