package com.blueberry.presentation.api

import com.blueberry.framework.presentation.PresentationRestController
import org.springframework.web.bind.annotation.RequestMapping
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
public class OauthController extends PresentationRestController {

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
