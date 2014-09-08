package com.blueberry.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Superclass for all entities
 *
 * @author Ganeshji Marwaha
 * @since 9/7/14
 */

@MappedSuperclass
public class DbEntity extends Dto {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    @JsonProperty
    Long id;

}
