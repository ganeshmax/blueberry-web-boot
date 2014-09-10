package com.blueberry.model.dto

/**
 * Different Oauth scopes supported. Similar to RoleType for users.
 *
 * @author Ganeshji Marwaha
 * @since 9/10/14
 */
public enum OauthScopeType {
    READ("read"),
    WRITE("write"),
    TRUST("trust")

    String name

    private OauthScopeType(String name) {
        this.name = name
    }
}