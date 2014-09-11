package com.blueberry.test.framework

import org.apache.http.HttpRequest
import org.apache.http.HttpResponse
import org.apache.http.client.RedirectStrategy
import org.apache.http.client.methods.HttpUriRequest
import org.apache.http.protocol.HttpContext

/**
 * TODO: Document Me
 *
 * @author Ganeshji Marwaha
 * @since 9/9/14
 */
class NoRedirectionStrategy implements RedirectStrategy {
    @Override
    boolean isRedirected(HttpRequest request, HttpResponse response, HttpContext context) throws ProtocolException {
        return false
    }

    @Override
    HttpUriRequest getRedirect(HttpRequest request, HttpResponse response, HttpContext context) throws ProtocolException {
        return null
    }
}
