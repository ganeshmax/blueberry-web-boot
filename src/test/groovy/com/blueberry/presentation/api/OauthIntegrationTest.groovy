package com.blueberry.presentation.api

import com.blueberry.test.framework.AbstractHttpIntegrationTest
import com.blueberry.test.framework.NoRedirectionStrategy
import groovyx.net.http.HTTPBuilder
import org.junit.Before
import org.junit.Test

import static com.blueberry.model.dto.ClientType.ANDROID
import static com.blueberry.model.dto.ClientType.EXTERNAL
import static groovyx.net.http.ContentType.*
import static com.blueberry.test.framework.HttpBuilderUtil.*

/**
 * Integration test for oauth
 *
 * @author Ganeshji Marwaha
 * @since 9/9/14
 */
class OauthIntegrationTest extends AbstractHttpIntegrationTest {

    HTTPBuilder http;

    static def redirectUri = "http://localhost:8080/oauth/callback"
    def loginParams,
        codeFlowStartParams, codeFlowConfirmParams, codeFlowTokenParams,
        implicitFlowParams, passwordFlowParams, refreshTokenFlowParams

    @Before
    public void setup() {
        http = new HTTPBuilder('http://localhost:8080/' )
        http.client.setRedirectStrategy(new NoRedirectionStrategy())

        loginParams = [
                username: 'user1@email.com',
                password: 'password'
        ]

        codeFlowStartParams = [
                client_id: 'blueberry-client-external',
                response_type: 'code',
                scope: 'read',
                redirect_uri: redirectUri
        ]

        codeFlowConfirmParams = [
                user_oauth_approval: 'true',
                'scope.read': 'true'
        ]

        codeFlowTokenParams = [
                grant_type: 'authorization_code',
                redirect_uri: redirectUri,
                code: ''
        ]

        implicitFlowParams = [
                client_id: 'blueberry-client-external',
                response_type: 'token',
                scope: 'read',
                redirect_uri: redirectUri
        ]

        passwordFlowParams = [
                username: 'admin1@email.com',
                password: 'password',
                grant_type: 'password'
        ]

        refreshTokenFlowParams = [
                redirect_uri: redirectUri,
                grant_type: 'refresh_token',
                refresh_token: ''
        ]
    }

    @Test
    public void oauthAuthorizationCodeFlowTest() {
        def out
        def codeValue = ''

        // Perform login as resource owner first
        out = login()                                                       // Login
        assert out.res.status == 302                                        // Login success

        // Perform /oauth/authorize flow for code (Authorization Code Flow)
        out = oauthCodeFlowStart()                                          // /oauth/authorize for code
        assert out.res.status == 200 || out.res.status == 302               // Authorize success

        if(out.res.status == 302) {                                         // No user approval needed (was granted previously)

            String redirectUrl = out.res.headers.Location
            log "Redirected URL 0: ${redirectUrl}"
            assert redirectUrl != null;                                     // Assert that we have a redirectUrl

            codeValue = extractFromUrlParam(redirectUrl, 'code')            // Extract auth code from redirect url

        } else if (out.res.status == 200) {                                 // User approval needed (first time access per session)

            out = oauthCodeFlowConfirm()                                    // Submit user approval
            assert out.res.status == 302                                    // User approval success

            String redirectUrl = out.res.headers.Location
            log "Redirected URL 1: ${redirectUrl}"
            assert redirectUrl != null                                      // Assert that we have a redirect Url

            codeValue = extractFromUrlParam(redirectUrl, 'code')            // Extract auth code from query param

        } else {
            assert false                                                    // Error in /oauth/authorize flow
        }

        log "Code Value: $codeValue"                                        // Assert that we have code
        assert codeValue != null
        assert codeValue.size() == 6

        // Perform /oauth/token flow for access_token
        out = oauthCodeFlowToken(codeValue)                                 // /oauth/token for access_token
        assert out.data.access_token != null                                // Assert that we have access_token
        assert out.data.refresh_token != null                               // Assert that we have refresh_token

        // Perform /oauth/token flow for new access_token from refresh_token
        out = oauthRefreshTokenFlow(out.data.refresh_token, EXTERNAL)       // /oauth/token for refresh_token
        assert out.data.access_token != null                                // Assert that we have access_token again
        assert out.data.refresh_token != null                               // Assert that we have refresh_token again
    }

    @Test
    public void oauthImplicitFlowTest() {
        def out
        def tokenValue = ''

        // Perform login as a resource owner first
        out = login()
        assert out.res.status == 302                                        // Login success

        // Perform /oauth/authorize flow for access_token (Implicit Flow)
        out = oauthImplicitFlow()
        assert out.res.status == 200 || out.res.status == 302               // Authorize success

        if(out.res.status == 302) {                                         // No user approval needed (was granted previously)

            def redirectUrl = out.res.headers.Location
            log "Redirected URL 0: ${redirectUrl}"
            assert redirectUrl != null                                      // Assert that we have a redirectUrl

            tokenValue = extractFromFragment(redirectUrl, 'access_token')   // Extract auth code from redirect url

        } else if (out.res.status == 200) {                                 // User approval needed (first time access per session)

            out = oauthCodeFlowConfirm()                                    // Submit user approval
            assert out.res.status == 302                                    // User approval success

            String redirectUrl = out.res.headers.Location
            log "Redirected URL 1: ${redirectUrl}"
            assert redirectUrl != null                                      // Assert that we have a redirect Url

            tokenValue = extractFromFragment(redirectUrl, 'access_token')   // Extract access_token from fragment

        } else {
            assert false                                                    // Error in /oauth/authorize flow
        }

        log "Token Value: $tokenValue"                                      // Assert that we have access_token
        assert tokenValue != null
        assert tokenValue.size() == 36
    }

    @Test
    public void oauthPasswordFlowTest() {
        def out
        def tokenValue = ''

        // Perform /oauth/token flow for access_token (Resource owner password flow)
        out = oauthPasswordFlow()
        assert out.res.status == 200                                        // Access token success

        tokenValue = out.data.access_token                                  // Assert that we have an access_token
        log "Token Value: $tokenValue"
        assert tokenValue != null
        assert tokenValue.size() == 36

        out = oauthRefreshTokenFlow(out.data.refresh_token, ANDROID)        // /oauth/token for refresh_token
        assert out.data.access_token != null                                // Assert that we have access_token again
        assert out.data.refresh_token != null                               // Assert that we have refresh_token again
    }

    def login() {
        return http.post(path: 'login', body: loginParams, requestContentType: URLENC) { response ->
            return [res: response]
        }
    }

    def oauthCodeFlowStart() {
        return http.get(path: 'oauth/authorize', query: codeFlowStartParams) { response ->
            return [res: response]
        }
    }

    def oauthCodeFlowConfirm() {
        return http.post(path: 'oauth/authorize', body: codeFlowConfirmParams) { response ->
            return [res: response]
        }
    }

    def oauthCodeFlowToken(codeValue) {
        codeFlowTokenParams.code = codeValue
        addBasicAuth(http, EXTERNAL)
        return http.post(path: '/oauth/token', body: codeFlowTokenParams) { response, json ->
            log "First Access Token JSON: $json"
            removeBasicAuth(http)
            return [res: response, data: json]
        }
    }

    def oauthRefreshTokenFlow(refreshToken, clientType) {
        refreshTokenFlowParams.refresh_token = refreshToken
        addBasicAuth(http, clientType)
        return http.post(path: '/oauth/token', body: refreshTokenFlowParams) { response, json ->
            log "Refreshed Access Token JSON: $json"
            removeBasicAuth(http)
            return [res: response, data: json]
        }

    }

    def oauthImplicitFlow() {
        return http.get(path: 'oauth/authorize', query: implicitFlowParams) { response ->
            return [res: response]
        }
    }

    def oauthPasswordFlow() {
        addBasicAuth(http, ANDROID)
        return http.get(path: 'oauth/token', query: passwordFlowParams) { response, json ->
            log "oauthPasswordFlow response json: $json"
            removeBasicAuth(http)

            return [res: response, data: json]
        }
    }



}
