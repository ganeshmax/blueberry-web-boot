package com.blueberry

import groovyx.net.http.HTTPBuilder
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.IntegrationTest
import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.web.WebAppConfiguration

import static groovyx.net.http.ContentType.*

/**
 * TODO: Document Me
 *
 * @author Ganeshji Marwaha
 * @since 9/9/14
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ApplicationConfig.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
public class WebLoginTest {

    @Value('${local.server.port}')
    int port;

    HTTPBuilder http;

    @Before
    public void setup() {
        println "<<<<< Local Port: $port"
        http = new HTTPBuilder('http://localhost:$port/rest/' )
    }

    @Test
    public void login() {
        def loginParams = [username: 'user1@email.com', password: 'password']

        http.post( path: 'login', body: loginParams, requestContentType: URLENC) { response ->

            println response.status
            println response.statusLine
        }
    }
}
