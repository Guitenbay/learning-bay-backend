package wiki.biki.learningbaybackend.service.impl;

import org.apache.jena.rdf.model.Model;
import wiki.biki.learningbaybackend.LearningBayBackendApplication;
import wiki.biki.learningbaybackend.fuseki.*;
import wiki.biki.learningbaybackend.model.Section;
import wiki.biki.learningbaybackend.service.SectionService;

import java.util.ArrayList;

public class SectionServiceImpl implements SectionService {
    private FusekiSPARQLStringBuilderFactory factory = new FusekiSPARQLStringBuilderFactory(
            PrefixConfig.getPrefix(new String[]{PrefixConfig.SECTION})
    );
    @Override
    public ArrayList<Section> getSectionList() {
        Model model = LearningBayBackendApplication.fusekiApp.queryDescribe(factory.build().set(SPARQLType.DESCRIBE)
                .append(" ?x ").startWhere().where("?x rdf:type :Section").endWhere().toString());
        return FusekiUtils.createEntityListFromModel(Section.class, model);
    }

    @Override
    public Section getSectionByUri(String uri) {
        Section section = null;
        Model model = LearningBayBackendApplication.fusekiApp.queryDescribe(factory.build().set(SPARQLType.DESCRIBE)
                .describe(uri).toString());
        try {
            section = FusekiUtils.createEntityFromModel(Section.class, model);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return section;
    }

    @Override
    public boolean isExist(String uri) {
        return LearningBayBackendApplication.fusekiApp.ask(factory.build().set(SPARQLType.ASK)
                .startWhere()
                .where(String.format("<%s> rdf:type :Section.", uri))
                .endWhere().toString());
    }

    private boolean insertData(String uri, String title, String content, String kElementUri,
                               String codeQuestionUri) {
        return LearningBayBackendApplication.fusekiApp.insert(factory.build().set(SPARQLType.INSERT)
                .startInsert()
                .to(uri).insertClass("rdf:type", ":Section")
                .insert("section:title", title).insert("section:content", content)
                .insert("section:correspondKE", kElementUri)
                .insert("section:correspondCQ", codeQuestionUri)
                .endInsert().toString());
    }

    @Override
    public boolean insert(Section section) {
        String uri = EntityConfig.SECTION_PREFIX + section.getId();
        if (isExist(uri)) return false;
        return insertData(uri, section.getTitle(), section.getContent(),
                section.getkElementURi(), section.getCodeQuestionUri());
    }

    @Override
    public boolean update(Section section) {
        String uri = EntityConfig.SECTION_PREFIX + section.getId();
        boolean delete = this.delete(uri);
        if (!delete) return false;
        return insertData(uri, section.getTitle(), section.getContent(),
                section.getkElementURi(), section.getCodeQuestionUri());
    }

    @Override
    public boolean delete(String uri) {
        if (!isExist(uri)) return false;
        else return LearningBayBackendApplication.fusekiApp.delete(factory.build().set(SPARQLType.DELETE)
                .startWhere()
                .where(String.format("<%s> ?property ?object.", uri))
                .endWhere().toString());
    }
}
