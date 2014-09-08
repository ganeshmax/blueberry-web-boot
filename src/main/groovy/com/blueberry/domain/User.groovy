package com.blueberry.domain

import javax.persistence.*

@Entity
@Table(name = "user")
public class User extends DbEntity {

    @Column(name = "email", nullable = false, unique = true)
    String email;

    @Column(name = "pwd", nullable = false)
    String password;

    @Column(name = "phone", nullable = true)
    String phone;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    public void addRole(Role role) {
        if(this.roles == null) {
            this.roles = new HashSet<Role>()
        }

        this.roles.add(role);
        role.addUser(this);
    }

}
