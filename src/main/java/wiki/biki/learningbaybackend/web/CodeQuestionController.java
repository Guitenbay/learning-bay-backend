package wiki.biki.learningbaybackend.web;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.*;
import wiki.biki.learningbaybackend.model.CodeQuestion;
import wiki.biki.learningbaybackend.service.CodeQuestionService;
import wiki.biki.learningbaybackend.service.impl.CodeQuestionServiceImpl;

import java.util.ArrayList;

@RestController
@RequestMapping(value = "/fuseki/code-question")
public class CodeQuestionController {
    private CodeQuestionService codeQuestionService = new CodeQuestionServiceImpl();

    @GetMapping("/all")
    public String getCodeQuestions() {
        ArrayList<CodeQuestion> codeQuestionList = codeQuestionService.getCodeQuestionList();
        JSONObject json = new JSONObject();
        json.put("res", true);
        json.put("data", codeQuestionList);
        return json.toJSONString();
    }

    @GetMapping
    public String getCodeQuestion(String uri) {
        CodeQuestion codeQuestion = codeQuestionService.getCodeQuestionByUri(uri);
        JSONObject json = new JSONObject();
        json.put("res", true);
        json.put("data", codeQuestion);
        return json.toJSONString();
    }

    @PutMapping
    public String insertCodeQuestion(@RequestBody CodeQuestion codeQuestion) {
        boolean result = codeQuestionService.insert(codeQuestion);
        JSONObject json = new JSONObject();
        json.put("res", result);
        return json.toJSONString();
    }

    @PostMapping
    public String updateCodeQuestion(@RequestBody CodeQuestion codeQuestion) {
        boolean result = codeQuestionService.update(codeQuestion);
        JSONObject json = new JSONObject();
        json.put("res", result);
        return json.toJSONString();
    }

    @DeleteMapping
    public String deleteCodeQuestion(String uri) {
        boolean result = codeQuestionService.delete(uri);
        JSONObject json = new JSONObject();
        json.put("res", result);
        return json.toJSONString();
    }

}