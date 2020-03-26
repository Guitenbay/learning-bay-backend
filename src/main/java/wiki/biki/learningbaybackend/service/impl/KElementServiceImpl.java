package wiki.biki.learningbaybackend.service.impl;

import org.apache.jena.rdf.model.*;
import wiki.biki.learningbaybackend.LearningBayBackendApplication;
import wiki.biki.learningbaybackend.fuseki.*;
import wiki.biki.learningbaybackend.model.KElement;
import wiki.biki.learningbaybackend.service.KElementService;

import java.util.ArrayList;

public class KElementServiceImpl implements KElementService {
    private FusekiSPARQLStringBuilderFactory factory = new FusekiSPARQLStringBuilderFactory(
            PrefixConfig.getPrefix(new String[]{PrefixConfig.KE})
    );

    @Override
    public ArrayList<KElement> getKElementList() {
        Model model = LearningBayBackendApplication.fusekiApp.queryDescribe(factory.build().set(SPARQLType.DESCRIBE)
                .append(" ?x ").startWhere().graph(GraphConfig.KE_GRAPH).where("?x rdf:type :KElement").endWhere().toString());
        return FusekiUtils.createEntityListFromModel(KElement.class, model);
    }

    @Override
    public KElement getKElementByUri(String uri) {
        KElement element = null;
        Model model = LearningBayBackendApplication.fusekiApp.queryDescribe(factory.build().set(SPARQLType.DESCRIBE).describe(uri)
                .append(String.format("FROM NAMED %s", GraphConfig.KE_GRAPH)).toString());
        try {
            element = FusekiUtils.createEntityFromModel(KElement.class, model);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return element;
    }

    @Override
    public boolean isExist(String uri) {
        return LearningBayBackendApplication.fusekiApp.ask(factory.build().set(SPARQLType.ASK)
                .startWhere().graph(GraphConfig.KE_GRAPH)
                .where(String.format("<%s> rdf:type :KElement.", uri))
                .endWhere().toString());
    }

    private boolean insertData(String uri, String name, String description, String creator, String date, ArrayList<String> previousList) {
        FusekiSPARQLStringBuilder kElementBuilder = factory.build().set(SPARQLType.INSERT)
                .startInsert().graph(GraphConfig.KE_GRAPH)
                .to(uri).insertClass("rdf:type", ":KElement")
                .insert("ke:name", name).insert("ke:description", description)
                .insert("ke:creator", creator).insert("ke:date", date);
        if (previousList != null) {
            for (String previous: previousList) {
                kElementBuilder = kElementBuilder.insert("ke:previous", previous);
            }
        }
        return LearningBayBackendApplication.fusekiApp.insert(kElementBuilder
                .endInsert().toString());
    }

    @Override
    public boolean insert(KElement kElement) {
        String uri = EntityConfig.K_ELEMENT_PREFIX + kElement.getId();
        if (isExist(uri)) return false;
        return insertData(uri,
                kElement.getName(), kElement.getDescription(),
                kElement.getCreator(), kElement.getDate(), kElement.getPreviousList());
    }

    @Override
    public boolean update(KElement kElement) {
        String uri = EntityConfig.K_ELEMENT_PREFIX + kElement.getId();
        boolean delete = this.delete(uri);
        if (!delete) return false;
        return insertData(uri,
                kElement.getName(), kElement.getDescription(),
                kElement.getCreator(), kElement.getDate(), kElement.getPreviousList());
    }

    @Override
    public boolean delete(String uri) {
        if (!isExist(uri)) return false;
        else return LearningBayBackendApplication.fusekiApp.delete(factory.build().set(SPARQLType.DELETE)
                .startWhere().graph(GraphConfig.KE_GRAPH)
                .where(String.format("<%s> ?property ?object.", uri))
                .endWhere().toString());
    }
}
