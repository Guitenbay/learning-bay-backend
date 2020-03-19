package wiki.biki.learningbaybackend.service;

import wiki.biki.learningbaybackend.model.CodeQuestion;

import java.util.ArrayList;

public interface CodeQuestionService {
    ArrayList<CodeQuestion> getCodeQuestionList();
    ArrayList<CodeQuestion> getCodeQuestionListByCourseUri(String uri);
    CodeQuestion getCodeQuestionByUri(String uri);
    boolean isExist(String uri);
    boolean insert(CodeQuestion codeQuestion);
    boolean update(CodeQuestion codeQuestion);
    boolean delete(String uri);
}
