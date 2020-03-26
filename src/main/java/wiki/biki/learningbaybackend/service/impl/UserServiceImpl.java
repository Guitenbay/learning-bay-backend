package wiki.biki.learningbaybackend.service.impl;

import org.apache.jena.rdf.model.RDFNode;
import wiki.biki.learningbaybackend.LearningBayBackendApplication;
import wiki.biki.learningbaybackend.fuseki.*;
import wiki.biki.learningbaybackend.model.User;
import wiki.biki.learningbaybackend.service.UserService;

import java.util.ArrayList;
import java.util.Map;

public class UserServiceImpl implements UserService {
    private FusekiSPARQLStringBuilderFactory factory = new FusekiSPARQLStringBuilderFactory(
            PrefixConfig.getPrefix(new String[]{PrefixConfig.USER})
    );
    @Override
    public String getUserUri(User user) {
        RDFNode uri = LearningBayBackendApplication.fusekiApp.querySelect(factory.build().set(SPARQLType.SELECT)
                .startSelect().select("?uri").endSelect()
                .startWhere().graph("?uri")
                .where(String.format("[] user:username '%s';", user.getUsername()))
                .where(String.format("user:password '%s';", user.getPassword()))
                .endWhere().toString()).get("uri");
        if (uri == null) {
            return null;
        } else {
            return uri.toString();
        }
    }

    @Override
    public boolean isExist(String uri) {
        return LearningBayBackendApplication.fusekiApp.ask(factory.build().set(SPARQLType.ASK)
                .startWhere().graph(String.format("<%s>", uri))
                .where("[] user:username ?name")
                .endWhere().toString());
    }

    private boolean insertData(String uri, String username, String password) {
//        System.out.println(factory.build().set(SPARQLType.INSERT)
//                .startInsert().graph(String.format("<%s>", uri))
//                .to("[]")
//                .insert("user:username", username)
//                .insert("user:password", password)
//                .endInsert().toString());
        return LearningBayBackendApplication.fusekiApp.insert(factory.build().set(SPARQLType.INSERT)
                .startInsert().graph(String.format("<%s>", uri))
                .to()
                .insert("user:username", username)
                .insert("user:password", password)
                .endInsert().toString());
    }

    @Override
    public boolean insert(User user) {
        String uri = GraphConfig.USER_GRAPH_PREFIX + user.getId();
        if (isExist(uri)) return false;
        return insertData(uri, user.getUsername(), user.getPassword());
    }

    @Override
    public boolean update(User user) {
        String uri = GraphConfig.USER_GRAPH_PREFIX + user.getId();
        boolean delete = this.delete(uri);
        if (!delete) return false;
        return insertData(uri, user.getUsername(), user.getPassword());
    }

    @Override
    public boolean delete(String uri) {
        if (!isExist(uri)) return false;
        else {
            return LearningBayBackendApplication.fusekiApp.delete(String.format("DROP GRAPH <%s>", uri));
        }
    }
}
