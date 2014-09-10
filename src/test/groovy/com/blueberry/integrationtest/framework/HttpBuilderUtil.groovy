package com.blueberry.integrationtest.framework

import groovyx.net.http.HTTPBuilder

/**
 * TODO: Document Me
 *
 * @author Ganeshji Marwaha
 * @since 9/9/14
 */
class HttpBuilderUtil {
    public static void addBasicAuth(HTTPBuilder http) {
        http.headers['Authorization'] = 'Basic ' +
                'blueberry-client-external:blueberry-client-external-password'.bytes.encodeBase64().toString()
    }

    public static void removeBasicAuth(HTTPBuilder http) {
        http.headers['Authorization'] = null
    }
}
