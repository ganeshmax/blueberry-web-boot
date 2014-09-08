package com.blueberry.domain

import com.fasterxml.jackson.annotation.JsonIgnore

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

@Entity
public class Role extends DbEntity {


    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    User user

    @Column(nullable = false)
    String name

    @Column(nullable = true)
    String description;
}