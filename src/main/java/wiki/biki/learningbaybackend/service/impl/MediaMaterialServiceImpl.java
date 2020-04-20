package wiki.biki.learningbaybackend.service.impl;

import org.apache.jena.rdf.model.Model;
import wiki.biki.learningbaybackend.LearningBayBackendApplication;
import wiki.biki.learningbaybackend.fuseki.*;
import wiki.biki.learningbaybackend.model.MediaMaterial;
import wiki.biki.learningbaybackend.service.MediaMaterialService;

import java.util.ArrayList;

public class MediaMaterialServiceImpl implements MediaMaterialService {
    private FusekiSPARQLStringBuilderFactory factory = new FusekiSPARQLStringBuilderFactory(
            PrefixConfig.getPrefix(new String[]{PrefixConfig.RESOURCE, PrefixConfig.MM})
    );
    @Override
    public ArrayList<MediaMaterial> getMediaMaterialList() {
        Model model = LearningBayBackendApplication.fusekiApp.queryDescribe(factory.build().set(SPARQLType.DESCRIBE)
                .append(" ?x ").startWhere().where("?x rdf:type :MediaMaterial").endWhere().toString());
        return FusekiUtils.createEntityListFromModel(MediaMaterial.class, model);
    }

    @Override
    public MediaMaterial getMediaMaterialByUri(String uri) {
        if (!isExist(uri)) return null;
        MediaMaterial mediaMaterial = null;
        Model model = LearningBayBackendApplication.fusekiApp.queryDescribe(factory.build().set(SPARQLType.DESCRIBE)
                .describe(uri).toString());
        try {
            mediaMaterial = FusekiUtils.createEntityFromModel(MediaMaterial.class, model);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mediaMaterial;
    }

    @Override
    public boolean isExist(String uri) {
        return LearningBayBackendApplication.fusekiApp.ask(factory.build().set(SPARQLType.ASK)
                .startWhere()
                .where(String.format("<%s> rdf:type :MediaMaterial.", uri))
                .endWhere().toString());
    }

    private boolean insertData(String uri, String title, String date, String creator,
                               String type, String filename, String description) {
        return LearningBayBackendApplication.fusekiApp.insert(factory.build().set(SPARQLType.INSERT)
                .startInsert()
                .to(uri).insertClass("rdf:type", ":MediaMaterial")
                .insert("resource:title", title).insert("resource:date", date)
                .insert("resource:creator", creator)
                .insert("mm:mediaType", type).insert("mm:filename", filename)
                .insert("mm:description", description)
                .endInsert().toString());
    }

    @Override
    public boolean insert(MediaMaterial mediaMaterial) {
        String uri = EntityConfig.MM_PREFIX + mediaMaterial.getId();
        if (isExist(uri)) return false;
        return insertData(uri, mediaMaterial.getTitle(), mediaMaterial.getDate(), mediaMaterial.getCreator(),
                mediaMaterial.getType(), mediaMaterial.getFilename(), mediaMaterial.getDescription());
    }

    @Override
    public boolean update(MediaMaterial mediaMaterial) {
        String uri = EntityConfig.MM_PREFIX + mediaMaterial.getId();
        boolean delete = this.delete(uri);
        if (!delete) return false;
        return insertData(uri, mediaMaterial.getTitle(), mediaMaterial.getDate(), mediaMaterial.getCreator(),
                mediaMaterial.getType(), mediaMaterial.getFilename(), mediaMaterial.getDescription());
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
