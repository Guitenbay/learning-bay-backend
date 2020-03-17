package wiki.biki.learningbaybackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import wiki.biki.learningbaybackend.fuseki.EmbeddedFusekiApp;

@SpringBootApplication
public class LearningBayBackendApplication {
    private static final int PORT = 3332;
    private static final String NAME = "/dsg";
    public static final EmbeddedFusekiApp fusekiApp = new EmbeddedFusekiApp(PORT, NAME, "dataset");
    {
        fusekiApp.start();
        fusekiApp.setConnectionFusekiBuilder(String.format("http://localhost:%d/%s", PORT, NAME));
    }
    public static void main(String[] args) {
        SpringApplication.run(LearningBayBackendApplication.class, args);
    }

}
