package wiki.biki.learningbaybackend;

import org.apache.log4j.LogManager;

public class Logger {
    public static final Logger instance = new Logger();
    private org.apache.log4j.Logger logger = LogManager.getLogger(this.getClass().getName());
    Logger() {}
    public void info(String message) {
        logger.info(message);
    }
    public void warn(String message) {
        logger.warn(message);
    }
    public void error(String message) {
        logger.error(message);
    }
}
