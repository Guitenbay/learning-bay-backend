package wiki.biki.learningbaybackend.web;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.*;
import wiki.biki.learningbaybackend.model.Course;
import wiki.biki.learningbaybackend.service.CourseService;
import wiki.biki.learningbaybackend.service.impl.CourseServiceImpl;

import java.util.ArrayList;

@RestController
@RequestMapping(value = "/fuseki/course")     // 通过这里配置使下面的映射都在 /fuseki 下
public class CourseController {
    private CourseService courseService = new CourseServiceImpl();

    @GetMapping("/all")
    public String getCourses() {
        ArrayList<Course> courseList = courseService.getCourseList();
        JSONObject json = new JSONObject();
        json.put("res", true);
        json.put("data", courseList);
        return json.toJSONString();
    }

    @GetMapping
    public String getCourse(String uri) {
        Course course = courseService.getCourseByUri(uri);
        JSONObject json = new JSONObject();
        json.put("res", true);
        json.put("data", course);
        return json.toJSONString();
    }

    @PutMapping
    public String insertCourse(@RequestBody Course course) {
        boolean result = courseService.insert(course);
        return String.format("{ res: %b }", result);
    }

    @PostMapping
    public String updateCourse(@RequestBody Course course) {
        boolean result = courseService.update(course);
        return String.format("{ res: %b }", result);
    }

    @DeleteMapping
    public String deleteCourse(String uri) {
        boolean result = courseService.delete(uri);
        return String.format("{ res: %b }", result);
    }
}
