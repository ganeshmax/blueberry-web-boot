package com.blueberry

import com.blueberry.integrationtest.framework.AbstractHttpIntegrationTest
import com.blueberry.integrationtest.framework.NoRedirectionStrategy
import groovyx.net.http.HTTPBuilder
import org.junit.Before
import org.junit.Test

import static groovyx.net.http.ContentType.*
import static com.blueberry.integrationtest.framework.HttpBuilderUtil.*

/**
 * Integration test for oauth
 *
 * @author Ganeshji Marwaha
 * @since 9/9/14
 */
class OauthIntegrationTest extends AbstractHttpIntegrationTest {

    HTTPBuilder http;

    def loginParams = [
            username: 'user1@email.com',
            password: 'password'
    ]

    def oauthAuthParams = [
            client_id: 'blueberry-client-external',
            response_type: 'code',
            scope: 'read',
            redirect_uri: 'http://localhost:8080/oauth/callback'
    ]

    def oauthAuthConfirmParams = [
            user_oauth_approval: 'true',
            'scope.read': 'true'
    ]

    def oauthAuthTokenParams = [
            grant_type: 'authorization_code',
            redirect_uri: 'http://localhost:8080/oauth/callback',
            code: ''
    ]

    def oauthImplicitParams = [
            client_id: 'blueberry-client-external',
            response_type: 'token',
            scope: 'read',
            redirect_uri: 'http://localhost:8080/oauth/callback'
    ]

    @Before
    public void setup() {
        http = new HTTPBuilder('http://localhost:8080/' )
        http.client.setRedirectStrategy(new NoRedirectionStrategy())
    }

    @Test
    public void oauthAuthorizationCodeFlow() {
        def out, codeValue = ''

        out = login()
        out = auth()

        if(out.status == 302) {
            def redirectUrl = out.headers.Location
            log "Redirected URL 0: ${redirectUrl}"
            assert redirectUrl != null

            codeValue = extractFromUrlParam(redirectUrl, 'code')
        } else if (out.status == 200) {
            out = authConfirm()
            codeValue = extractFromUrlParam(out.headers.Location, 'code')
        } else {
            assert false
        }

        log "Code Value: $codeValue"
        assert codeValue != null
        assert codeValue.size() == 6

        authToken(codeValue)
    }

    @Test
    public void oauthImplicitFlow() {
        def out, tokenValue = ''

        out = login()
        out = implicit()

        if(out.status == 302) {
            def redirectUrl = out.headers.Location
            log "Redirected URL 1: ${redirectUrl}"
            assert redirectUrl != null

            tokenValue = extractFromFragment(redirectUrl, "access_token")
        } else if (out.status == 200) {
            out = authConfirm()
            tokenValue = extractFromFragment(out.headers.Location, "access_token")
        } else {
            assert false
        }

        log "Token Value: $tokenValue"
        assert tokenValue != null
        assert tokenValue.size() == 36
    }

    def login() {
        return http.post(path: 'login', body: loginParams, requestContentType: URLENC) { response ->
            assert response.status == 200 || response.status == 302
            return response
        }
    }

    def auth() {
        return http.get(path: 'oauth/authorize', query: oauthAuthParams) { response ->
            assert response.status == 200 || response.status == 302
            return response
        }
    }

    def authConfirm() {
        return http.post(path: 'oauth/authorize', body: oauthAuthConfirmParams) { response ->
            assert response.status == 302
            return response
        }
    }

    def authToken(codeValue) {
        oauthAuthTokenParams.code = codeValue
        addBasicAuth(http)
        return http.post(path: '/oauth/token', body: oauthAuthTokenParams) { response, json ->
            log "token: $json"
            assert json.access_token != null
            removeBasicAuth(http)
            return response
        }
    }

    def implicit() {
        return http.get(path: 'oauth/authorize', query: oauthImplicitParams) { response ->
            return response
        }
    }



}
