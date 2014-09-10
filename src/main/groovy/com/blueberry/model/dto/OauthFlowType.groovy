package com.blueberry.model.dto

/**
 * Different Oauth flow types supported and their names.
 *
 * @author Ganeshji Marwaha
 * @since 9/10/14
 */
public enum OauthFlowType {

    AUTHORIZATION_CODE("authorization_code"),
    IMPLICIT("implicit"),
    PASSWORD("password"),
    CLIENT_CREDENTIALS("client_credentials"),
    REFRESH_TOKEN("refresh_token")

    String name

    private OauthFlowType(String name) {
        this.name = name
    }
}