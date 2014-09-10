package com.blueberry.model.dto

/**
 * Represents different oauth clients that will be supported.
 * Currently, this is a static list in enum. In future this could come from a database, when we support
 * multiple third party app developers to register and develop apps against our API
 *
 * @author Ganeshji Marwaha
 * @since 9/10/14
 */
public enum ClientType {
    ANDROID("blueberry-android", "blueberry-android-password"),
    IOS("blueberry-ios", "blueberry-ios-password"),
    EXTERNAL("blueberry-client-external", "blueberry-client-external")

    String clientId, clientSecret

    private ClientType(String clientId, String clientSecret) {
        this.clientId = clientId
        this.clientSecret = clientSecret
    }

    public String getCredentialsForBasicAuth() {
        return "$clientId:$clientSecret".bytes.encodeBase64().toString()
    }
}