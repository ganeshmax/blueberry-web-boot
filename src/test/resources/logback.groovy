import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.core.ConsoleAppender
import static ch.qos.logback.classic.Level.*

def STDOUT = 'STDOUT'

appender STDOUT, ConsoleAppender, {
    encoder(PatternLayoutEncoder) {
        pattern = '[%-5level %logger{5}] - %msg%n'
    }
}

root WARN, ['STDOUT']
    logger 'groovyx.net.http', DEBUG, [STDOUT], false
        logger 'groovyx.net.http.HttpURLClient', INFO, [STDOUT], false
    logger 'org.apache.http', INFO, [STDOUT], false
        logger 'org.apache.http.headers', DEBUG, [STDOUT], false
        logger 'org.apache.http.wire', DEBUG, [STDOUT], false
