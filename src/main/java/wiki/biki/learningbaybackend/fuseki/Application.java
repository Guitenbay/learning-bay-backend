package wiki.biki.learningbaybackend.fuseki;



public class Application {
    private static final int PORT = 3332;
    private static final String NAME = "/dsg";

    public static void main(String[] args) {
        EmbeddedFusekiApp fusekiApp = new EmbeddedFusekiApp(PORT, NAME, "dataset");
        fusekiApp.start();
//
        //建立 fuseki 服务器连接
        fusekiApp.setConnectionFusekiBuilder(String.format("http://localhost:%d/%s", PORT, NAME));
//        fusekiApp.createGraph("<http://biki.wiki/books>");
        //插入语句
//        fusekiApp.insert("PREFIX ke:  <http://www.semanticweb.org/bikiwiki/ontologies/2020/2/knowledge-element#>" +
//                "INSERT DATA { [] ke:name \"A new book\" ; ke:creator \"A.N.Other\" ; ke:2333 \"233\" . }");
        //删除语句
//        fusekiApp.delete("PREFIX ke:  <http://www.semanticweb.org/bikiwiki/ontologies/2020/2/knowledge-element#>" +
//                "DELETE WHERE { ?book ke:name \"A new book\"; ?property ?value  . }");
        //更新语句
//        fusekiApp.update("PREFIX dc:  <http://purl.org/dc/elements/1.1/>" +
//                "DELETE { <http://example/book1> dc:title \"A new book\";" +
//                " dc:creator \"A.N.Other\" . }" +
//                "INSERT { <http://example/book1> dc:title \"A old book\";" +
//                " dc:creator \"Guitenbay\" . }" +
//                "WHERE { <http://example/book1> ?p ?o . }");
        //Drop Graph
        fusekiApp.update("DROP ALL");
//        String learningBay = "http://biki.wiki/LearningBay-Knowledge";
        //create Graph
//        fusekiApp.createGraph(learningBay);
        //load Graph
//        fusekiApp.load("owl/learning-bay.owl");
//        fusekiApp.load(learningBay, "owl/knowledge-element.owl");
        //查询语句
//        JsonArray jsonArray = fusekiApp.queryForJson(
//                "PREFIX ke:  <http://www.semanticweb.org/bikiwiki/ontologies/2020/2/knowledge-element#>" +
//                        "JSON { 'object': $object, 'creator': $creator }" +
//                        "WHERE { [] ke:name $object; ke:creator $creator . }");
//        System.out.println(jsonArray.toString());

        fusekiApp.stop();
    }
}
