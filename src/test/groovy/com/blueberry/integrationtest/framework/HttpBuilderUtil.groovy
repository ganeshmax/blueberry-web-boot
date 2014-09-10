package com.blueberry.integrationtest.framework

import com.blueberry.model.dto.ClientType
import groovyx.net.http.HTTPBuilder

import static com.blueberry.model.dto.ClientType.*


/**
 * TODO: Document Me
 *
 * @author Ganeshji Marwaha
 * @since 9/9/14
 */
class HttpBuilderUtil {

    public static void addBasicAuth(HTTPBuilder http, ClientType clientType = EXTERNAL) {
        http.headers['Authorization'] = 'Basic ' + clientType.credentialsForBasicAuth
    }

    public static void removeBasicAuth(HTTPBuilder http) {
        http.headers['Authorization'] = null
    }
}
