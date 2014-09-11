package com.blueberry.test.framework

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

    public String extractFromFragment(String url, String param)  {
        def fragment = url.substring(url.lastIndexOf("#") + 1)
        def fragmentParams = fragment.split("&")

        for(String fragmentParam in fragmentParams) {
            if(fragmentParam.startsWith("access_token")) {
                def paramPair = fragmentParam.split("=")
                return paramPair[1]
            }
        }
        return null
    }

    public void log(message) {
        println "<<<<< $message >>>>>"
    }
}
