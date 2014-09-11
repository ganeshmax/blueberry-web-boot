package com.blueberry.model.dto

/**
 * An enum for different types of roles supported in the system.
 * This has to be kept in sync with the roles in the database
 *
 * @author Ganeshji Marwaha
 * @since 8/24/14
 */
public enum RoleType {
    ROLE_ADMIN(1),
    ROLE_OPERATOR(2),
    ROLE_PROVIDER(3),
    ROLE_RECEIVER(4),
    ROLE_USER(5)

    Integer roleId

    private RoleType(Integer roleId) {
        this.roleId = roleId
    }
}