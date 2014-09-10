package com.blueberry.presentation.api

import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

import javax.servlet.http.HttpServletRequest

/**
 * TODO: Document Me
 *
 * @author Ganeshji Marwaha
 * @since 9/9/14
 */
@RestController
@RequestMapping("/oauth")
public class OauthController {

    protected final Log log = LogFactory.getLog(this.getClass());

    @RequestMapping("/callback")
    public String oauthCallbackCode(HttpServletRequest request) {
        String code = request.getParameter("code");
        String token = request.getParameter("access_token");

        log.debug("<<<<< Request Parameter Names: " + request.getParameterMap() + ">>>>>")

        log.debug("<<<<< oauthCallbackCode >>>>>" + code);
        log.debug("<<<<< oauthCallbackToken >>>>>" + token);

        return "Code: $code, Token: $token"
    }
}
