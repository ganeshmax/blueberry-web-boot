package com.blueberry.integrationtest.framework

/**
 * TODO: Document Me
 *
 * @author Ganeshji Marwaha
 * @since 9/9/14
 */
public abstract class AbstractHttpIntegrationTest {

    public String extractFromUrlParam(String url, String param)  {
        def paramStr = url.substring(url.lastIndexOf("?$param") + 1)
        def paramPair = paramStr.split('=')
        return paramPair[1]
    }

    public void log(message) {
        println "<<<<< $message >>>>>"
    }
}
