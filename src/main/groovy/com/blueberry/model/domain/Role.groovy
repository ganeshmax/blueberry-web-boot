package com.blueberry.model.domain

import com.blueberry.framework.domain.DbEntity
import com.fasterxml.jackson.annotation.JsonIgnore

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.ManyToMany
import javax.persistence.Table
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Entity
@Table(name = "t_role")
public class Role extends DbEntity {

    @Column(name = "name", nullable = false, unique = true, length = 255)
    @NotNull
    @Size(min = 6, max = 50)
    String name

    @Column(name = "description", nullable = true, unique = false, length = 255)
    @Size(min = 0, max = 255)
    String description;

    @JsonIgnore
    @ManyToMany(mappedBy = "roles")
    Set<User> users

    public void addUser(User user) {
        if(this.users == null) {
            this.users = new HashSet<User>();
        }

        this.users.add(user);
    }
}