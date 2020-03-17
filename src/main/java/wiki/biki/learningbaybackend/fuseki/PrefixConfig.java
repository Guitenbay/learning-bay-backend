package wiki.biki.learningbaybackend.fuseki;

public class PrefixConfig {
    private static final String BASE     = "PREFIX : <http://www.semanticweb.org/bikiwiki/ontologies/2020/2/learning-bay#>";
    public static final String KE       = "PREFIX ke: <http://www.semanticweb.org/bikiwiki/ontologies/2020/2/learning-bay/knowledge-element#>";
    public static final String COURSE   = "PREFIX course: <http://www.semanticweb.org/bikiwiki/ontologies/2020/2/learning-bay/course#>";
    public static final String CHAPTER  = "PREFIX chapter: <http://www.semanticweb.org/bikiwiki/ontologies/2020/2/learning-bay/chapter#>";
    public static final String LESSON   = "PREFIX lesson: <http://www.semanticweb.org/bikiwiki/ontologies/2020/2/learning-bay/lesson#>";
    public static final String SECTION  = "PREFIX section: <http://www.semanticweb.org/bikiwiki/ontologies/2020/2/learning-bay/section#>";
    public static final String RESOURCE = "PREFIX resource: <http://www.semanticweb.org/bikiwiki/ontologies/2020/2/learning-bay/resource#>";
    public static final String MM       = "PREFIX mm: <http://www.semanticweb.org/bikiwiki/ontologies/2020/2/learning-bay/media-material#>";
    public static final String CQ       = "PREFIX cq: <http://www.semanticweb.org/bikiwiki/ontologies/2020/2/learning-bay/code-question#>";
    public static final String USER     = "PREFIX user: <http://www.semanticweb.org/bikiwiki/ontologies/2020/2/learning-bay/user#>";

    public static String getPrefix(String[] prefixes) {
        StringBuilder result = new StringBuilder("PREFIX owl: <http://www.w3.org/2002/07/owl#>" +
                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" +
                "PREFIX xml: <http://www.w3.org/XML/1998/namespace>" +
                "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>" +
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schem>" + BASE);
        for (String prefix: prefixes) {
            result.append(prefix);
        }
        return result.toString();
    }
}
