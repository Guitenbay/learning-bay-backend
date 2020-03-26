package wiki.biki.learningbaybackend.web;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.*;
import wiki.biki.learningbaybackend.model.Course;
import wiki.biki.learningbaybackend.service.CourseService;
import wiki.biki.learningbaybackend.service.impl.CourseServiceImpl;

import java.util.ArrayList;
import java.util.Map;

@RestController
@RequestMapping(value = "/fuseki/course")     // 通过这里配置使下面的映射都在 /fuseki 下
public class CourseController {
    private CourseService courseService = new CourseServiceImpl();

    @GetMapping("/all")
    public String getCourses() {
//        ArrayList<Course> courseList = courseService.getCourseList();
        ArrayList<Map<String, String>> entities = courseService.getCourseEntities();
        JSONObject json = new JSONObject();
        json.put("res", true);
        json.put("data", entities);
        return json.toJSONString();
    }

    @GetMapping
    public String getCourse(String uri) {
        JSONObject json = new JSONObject();
        if (uri == null) {
            json.put("res", false);
            return json.toJSONString();
        }
        Course course = courseService.getCourseByUri(uri);
        json.put("res", true);
        json.put("data", course);
        return json.toJSONString();
    }

    @PutMapping
    public String insertCourse(@RequestBody Course course) {
        boolean result = courseService.insert(course);
        JSONObject json = new JSONObject();
        json.put("res", result);
        return json.toJSONString();
    }

    @PostMapping
    public String updateCourse(@RequestBody Course course) {
        boolean result = courseService.update(course);
        JSONObject json = new JSONObject();
        json.put("res", result);
        return json.toJSONString();
    }

    @DeleteMapping
    public String deleteCourse(String uri) {
        JSONObject json = new JSONObject();
        if (uri == null) {
            json.put("res", false);
            return json.toJSONString();
        }
        boolean result = courseService.delete(uri);
        json.put("res", result);
        return json.toJSONString();
    }
}
