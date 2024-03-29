package com.blueberry.repository

import com.blueberry.model.domain.Role
import com.blueberry.model.domain.User
import org.springframework.data.jpa.repository.JpaRepository

/**
 * Repository for Role
 *
 * @author Ganeshji Marwaha
 * @since 9/7/14
 */
public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String name)

}