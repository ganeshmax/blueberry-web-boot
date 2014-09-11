package com.blueberry.presentation.rest

import com.blueberry.test.framework.AbstractHttpIntegrationTest
import com.blueberry.test.framework.NoRedirectionStrategy
import groovyx.net.http.HTTPBuilder
import org.junit.Before
import org.junit.Test

/**
 * TODO: Document Me
 *
 * @author Ganeshji Marwaha
 * @since 9/11/14
 */
class LoginIntegrationTest extends AbstractHttpIntegrationTest {
    HTTPBuilder http;

    @Before
    public void setup() {
        http = new HTTPBuilder('http://localhost:8080/')
        http.client.setRedirectStrategy(new NoRedirectionStrategy())
    }

    @Test
    public void testLoginSuccess() {
        http.post(path: '/rest/login', body: [username: 'user1@email.com', password: 'password']) { response ->
            assert response.status == 200
        }
    }
}
