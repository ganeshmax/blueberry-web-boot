package com.blueberry.model.dto

import com.blueberry.framework.dto.Dto
import com.blueberry.model.domain.Role
import com.blueberry.model.domain.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

/**
 * UserDetails implementation representing a user in spring security
 *
 * @author Ganeshji Marwaha
 * @since 9/7/14
 */
public class SpringUserDetails extends Dto implements UserDetails {

    String email
    String password
    String phone

    Set<GrantedAuthority> authorities

    public SpringUserDetails(User user) {
        this.email = user.email
        this.password = user.password
        this.phone = user.phone

        this.authorities = new HashSet<>();
        for (Role role : user.roles) {
            this.authorities.add(new SimpleGrantedAuthority(role.name));
        }
    }

    @Override
    String getUsername() {
        return email
    }

    @Override
    boolean isAccountNonExpired() {
        return true
    }

    @Override
    boolean isAccountNonLocked() {
        return true
    }

    @Override
    boolean isCredentialsNonExpired() {
        return true
    }

    @Override
    boolean isEnabled() {
        return true
    }
}
