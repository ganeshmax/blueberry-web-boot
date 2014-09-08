package com.blueberry.domain

import com.fasterxml.jackson.annotation.JsonIgnore

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.ManyToMany
import javax.persistence.ManyToOne

@Entity
public class Role extends DbEntity {


    @JsonIgnore
    @ManyToMany(mappedBy = "roles")
    Set<User> users

    @Column(nullable = false)
    String name

    @Column(nullable = true)
    String description;

    public void addUser(User user) {
        if(this.users == null) {
            this.users = new HashSet<User>();
        }

        this.users.add(user);
    }
}