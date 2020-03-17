package wiki.biki.learningbaybackend.fuseki;

public class FusekiSPARQLStringBuilderFactory {
    private String prefixes;
    public FusekiSPARQLStringBuilderFactory(String prefixes) {
        this.prefixes = prefixes;
    }
    public FusekiSPARQLStringBuilder build() {
        return new FusekiSPARQLStringBuilder(prefixes);
    }
}
