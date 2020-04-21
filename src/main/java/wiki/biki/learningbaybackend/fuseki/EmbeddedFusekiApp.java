package wiki.biki.learningbaybackend.fuseki;

import org.apache.jena.atlas.json.JsonArray;
import org.apache.jena.fuseki.main.FusekiServer;
import org.apache.jena.fuseki.system.FusekiLogging;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdfconnection.RDFConnectionFuseki;
import org.apache.jena.rdfconnection.RDFConnectionRemoteBuilder;
import org.apache.jena.sparql.core.DatasetGraph;
import org.apache.jena.system.Txn;
import org.apache.jena.tdb.TDBFactory;
import wiki.biki.learningbaybackend.Logger;

import java.util.*;

public class EmbeddedFusekiApp {
    private FusekiServer server;
    private RDFConnectionRemoteBuilder builder;

    public EmbeddedFusekiApp(int port, String name, String datasetPath) {
        FusekiLogging.setLogging();
//        LogCtl.setLevel(Fuseki.serverLogName,  "WARN");
//        LogCtl.setLevel(Fuseki.actionLogName,  "WARN");
//        LogCtl.setLevel(Fuseki.requestLogName, "WARN");
//        LogCtl.setLevel(Fuseki.adminLogName,   "WARN");
//        LogCtl.setLevel("org.eclipse.jetty",   "WARN");
        DatasetGraph dsg = TDBFactory.createDatasetGraph(datasetPath);
        this.server = FusekiServer.make(port, name, dsg);
    }

    public void start() {
        this.server.start();
    }

    public void stop() {
        this.server.stop();
    }

    public void setConnectionFusekiBuilder(String url) {
        this.builder = RDFConnectionFuseki.create().destination(url);
    }

    public RDFConnectionRemoteBuilder getConnectionFusekiBuilder() {
        return this.builder;
    }

    private RDFConnectionFuseki getConnect() {
        return (RDFConnectionFuseki) this.builder.build();
    }

    public JsonArray queryForJsonArray(String queryString) {
        Query query = QueryFactory.create(queryString, Syntax.syntaxARQ);
        try (RDFConnectionFuseki conn = this.getConnect(); QueryExecution queryExecution = conn.query(query)) {
            return Txn.calculateRead(conn, queryExecution::execJson);
        } catch (Exception err) {
            Logger.instance.warn(err.toString());
            return new JsonArray();
        }
    }

    public Map<String, RDFNode> querySelect(String selectString) {
        Map<String, RDFNode> map = new HashMap<>();
        RDFConnectionFuseki conn = this.getConnect();
        conn.begin(TxnType.READ);
        try (QueryExecution queryExecution = conn.query(selectString)) {
            ResultSet resultSet = queryExecution.execSelect();
            QuerySolution querySolution = resultSet.next();
            Iterator<String> iterator = querySolution.varNames();
            while(iterator.hasNext()) {
                String header = iterator.next();
                if (!map.containsKey(header)) {
                    map.put(header, querySolution.get(header));
                }
            }
        } catch (Exception err) {
            Logger.instance.warn(err.toString());
        } finally {
            conn.commit();
        }
        return map;
    }

    public ArrayList<Map<String, String>> querySelectAsEntities(String selectString) {
        ArrayList<Map<String, String>> entities = new ArrayList<>();
        RDFConnectionFuseki conn = this.getConnect();
        conn.begin(TxnType.READ);
        try (QueryExecution queryExecution = conn.query(selectString)) {
            ResultSet resultSet = queryExecution.execSelect();
            resultSet.forEachRemaining(row -> {
                Map<String, String> entity = new HashMap<>();
                Iterator<String> iterator = row.varNames();
                while(iterator.hasNext()) {
                    String header = iterator.next();
                    entity.put(header, FusekiUtils.getObjectValue(row.get(header)));
                }
                entities.add(entity);
            });
        } catch (Exception err) {
            Logger.instance.warn(err.toString());
        } finally {
            conn.commit();
        }
        return entities;
    }

    public Map<String, ArrayList<RDFNode>> querySelectList(String selectString) {
        Map<String, ArrayList<RDFNode>> map = new HashMap<>();
        RDFConnectionFuseki conn = this.getConnect();
        conn.begin(TxnType.READ);
        try (QueryExecution queryExecution = conn.query(selectString)) {
            ResultSet resultSet = queryExecution.execSelect();
            resultSet.forEachRemaining(row -> {
                Iterator<String> iterator = row.varNames();
                while(iterator.hasNext()) {
                    String header = iterator.next();
                    if (!map.containsKey(header)) {
                        map.put(header, new ArrayList<>());
                    }
                    map.get(header).add(row.get(header));
                }
            });
        } catch (Exception err) {
            Logger.instance.warn(err.toString());
        } finally {
            conn.commit();
        }
        return map;
    }

    public Model queryDescribe(String describeString) {
        try (RDFConnectionFuseki conn = this.getConnect()) {
            return Txn.calculateRead(conn, ()-> conn.queryDescribe(describeString));
        } catch (Exception err) {
            Logger.instance.warn(err.toString());
            return null;
        }
    }

    public boolean ask(String askString) {
        try (RDFConnectionFuseki conn = this.getConnect()) {
            return Txn.calculateRead(conn, ()-> conn.queryAsk(askString));
        } catch (Exception err) {
            Logger.instance.warn(err.toString());
            return false;
        }
    }

    public boolean update(String updateString) {
        try (RDFConnectionFuseki conn = this.getConnect()) {
            Txn.executeWrite(conn, ()-> conn.update(updateString));
            return true;
        } catch (Exception err) {
            Logger.instance.warn(err.toString());
            return false;
        }
    }

    public boolean insert(String insertString) {
        return update(insertString);
    }

    public boolean delete(String deleteString) {
        return update(deleteString);
    }

    public boolean createGraph(String graphName) {
        return update(String.format("CREATE GRAPH <%s>", graphName));
    }

    void load(String file) {
        try (RDFConnectionFuseki conn = this.getConnect()) {
            Txn.executeWrite(conn, ()-> conn.load(file));
        } catch (Exception err) {
            Logger.instance.warn(err.toString());
        }
    }

    public void load(String graph, String file) {
        try (RDFConnectionFuseki conn = this.getConnect()) {
            Txn.executeWrite(conn, ()-> conn.load(graph, file));
        } catch (Exception err) {
            Logger.instance.warn(err.toString());
        }
    }

    void put(String graphName, Model model) {
        try (RDFConnectionFuseki conn = this.getConnect()) {
            Txn.executeWrite(conn, ()-> conn.put(graphName, model));
        } catch (Exception err) {
            Logger.instance.warn(err.toString());
        }
    }
}
