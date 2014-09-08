package com.blueberry.util

import groovy.json.JsonBuilder

import javax.servlet.http.HttpServletResponse

/**
 * HTTP related utilities
 *
 * @author Ganeshji Marwaha
 * @since 9/7/14
 */
class HttpUtil {

    static void sendErrorToResponseInJsonFormat(HttpServletResponse response, int status, String message) {

        response.setStatus(status);
        response.contentType = 'application/json';

        def builder = new JsonBuilder();
        builder.error {
            code (status)
            message (message)
        };

        PrintWriter writer = response.getWriter();
        writer.println(builder.toPrettyString());
    }
}
