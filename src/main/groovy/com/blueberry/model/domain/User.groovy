package com.blueberry.model.domain

import com.blueberry.framework.domain.DbEntity
import com.blueberry.framework.validation.ValidateEmail
import groovy.transform.ToString

import javax.persistence.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Entity
@Table(name = "t_user")
@ToString(includeNames = true, includePackage = false, includeSuper = true)
public class User extends DbEntity {

    @Column(name = "email", nullable = false, unique = true, length = 255)
    @NotNull @ValidateEmail
    String email;

    @Column(name = "pwd", nullable = false, unique = false, length = 255)
    @NotNull
    @Size(min = 6, max = 10)
    String password;

    @Column(name = "phone", nullable = false, unique = false, length = 255)
    @NotNull
    @Size(min = 8, max = 20)
    String phone;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    Set<Role> roles;

    public void addRole(Role role) {
        if(this.roles == null) {
            this.roles = new HashSet<Role>()
        }

        this.roles.add(role);
        role.addUser(this);
    }

    public void update(User changedUser) {
        this.email = changedUser.email
        this.password = changedUser.password
        this.phone = changedUser.phone
    }

}
