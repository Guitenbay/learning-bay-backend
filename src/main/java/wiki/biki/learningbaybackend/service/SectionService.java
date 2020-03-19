package wiki.biki.learningbaybackend.service;

import org.apache.jena.atlas.json.JsonValue;
import wiki.biki.learningbaybackend.model.Section;

import java.util.ArrayList;

public interface SectionService {
    ArrayList<Section> getSectionList();
    ArrayList<Section> getSectionListByLessonUri(String uri);
    Section getSectionByUri(String uri);
    boolean isExist(String uri);
    boolean insert(Section section);
    boolean update(Section section);
    boolean delete(String uri);
}
