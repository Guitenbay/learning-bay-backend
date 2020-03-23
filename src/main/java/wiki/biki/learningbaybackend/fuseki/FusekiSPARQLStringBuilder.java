package wiki.biki.learningbaybackend.fuseki;

public class FusekiSPARQLStringBuilder {
    private StringBuilder builder;
    private boolean afterSetType;
    private boolean startSetSelect;
    private boolean startSetInsert;
    private boolean afterSetGraph;
    public FusekiSPARQLStringBuilder(String prefixes) {
        builder = new StringBuilder(prefixes).append(" ");
        afterSetType = startSetSelect = startSetInsert = afterSetGraph = false;
    }
    
    public FusekiSPARQLStringBuilder set(SPARQLType type) {
        if (afterSetType) return this;
        switch (type) {
            case JSON:      builder.append("JSON"); break;
            case INSERT:    builder.append("INSERT DATA"); break;
            case DELETE:    builder.append("DELETE"); break;
            case ASK:       builder.append("ASK"); break;
            case DESCRIBE:  builder.append("DESCRIBE"); break;
            case SELECT:    builder.append("SELECT"); break;
            default: break;
        }
        afterSetType = true;
        return this;
    }
    
    public FusekiSPARQLStringBuilder append(String str) {
        builder.append(str);
        return this;
    }

    public FusekiSPARQLStringBuilder graph(String graphName) {
        afterSetGraph = true;
        return this.append("GRAPH ").append(graphName).append("{");
    }

    public FusekiSPARQLStringBuilder describe(String uri) {
        if (!afterSetType) return this;
        return this.append(String.format("<%s>", uri));
    }

    public FusekiSPARQLStringBuilder startSelect() {
        startSetSelect = true;
        return this.append(" ");
    }

    public FusekiSPARQLStringBuilder select(String header) {
        if (!startSetSelect) this.append(" ");
        else startSetSelect = false;
        return this.append(header);
    }

    public FusekiSPARQLStringBuilder endSelect() {
        return this.append(" ");
    }
    
    public FusekiSPARQLStringBuilder startJsonSelect() {
        startSetSelect = true;
        return this.append("{");
    }

    public FusekiSPARQLStringBuilder jsonSelect(String name, String property) {
        if (!startSetSelect) this.append(",");
        else startSetSelect = false;
        return this.append(String.format("'%s':?%s", name, property));
    }

    public FusekiSPARQLStringBuilder jsonSelect(String property) {
        if (!startSetSelect) this.append(",");
        else startSetSelect = false;
        return this.append(String.format("'%s':?%s", property, property));
    }

    public FusekiSPARQLStringBuilder endJsonSelect() {
        if (afterSetGraph) this.append("}");
        return this.append("}");
    }
    public FusekiSPARQLStringBuilder startInsert() {
        startSetInsert = true;
        return this.append("{");
    }
    public FusekiSPARQLStringBuilder to(String id) {
        if (!startSetInsert) return this;
        return this.append(String.format("<%s>", id));
    }
    public FusekiSPARQLStringBuilder to() {
        if (!startSetInsert) return this;
        return this.append("[] ");
    }
    public FusekiSPARQLStringBuilder insert(String property, String value) {
        if (!startSetInsert) this.append(";");
        else startSetInsert = false;
        return this.append(property).append(" ").append(String.format("'%s'", value));
    }
    public FusekiSPARQLStringBuilder insert(String property, int value) {
        if (!startSetInsert) this.append(";");
        else startSetInsert = false;
        return this.append(property).append(" ").append(String.valueOf(value));
    }
    public FusekiSPARQLStringBuilder insertClass(String property, String value) {
        if (!startSetInsert) this.append(";");
        else startSetInsert = false;
        return this.append(property).append(" ").append(String.format("%s", value));
    }
    public FusekiSPARQLStringBuilder insertUri(String property, String uri) {
        if (!startSetInsert) this.append(";");
        else startSetInsert = false;
        return this.append(property).append(" ").append(String.format("<%s>", uri));
    }
    public FusekiSPARQLStringBuilder endInsert() {
        this.append(".");
        if (afterSetGraph) this.append("}");
        return this.append("}");
    }

    public FusekiSPARQLStringBuilder startWhere() {
        return this.append("WHERE{");
    }

    public FusekiSPARQLStringBuilder where(String whereStr) {
        return this.append(whereStr);
    }

    public FusekiSPARQLStringBuilder endWhere() {
        if (afterSetGraph) this.append("}");
        return this.append("}");
    }

    @Override
    public String toString() {
        return builder.toString();
    }
}
