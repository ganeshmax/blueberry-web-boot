package com.blueberry.domain

import javax.persistence.*

@Entity
@Table(name = "users")
public class User extends DbEntity {

    @Column(name = "email", nullable = false, unique = true)
    String email;

    @Column(name = "pwd", nullable = false)
    String password;

    @Column(name = "phone", nullable = true)
    String phone;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    Set<Role> roles;

    public void addRole(Role role) {
        if(this.roles == null) {
            this.roles = new HashSet<Role>()
        }

        this.roles.add(role);
        role.setUser(this);
    }

}
