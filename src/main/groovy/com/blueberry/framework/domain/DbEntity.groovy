package com.blueberry.framework.domain;

import com.blueberry.framework.dto.Dto;
import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.ToString;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Superclass for all entities
 *
 * @author Ganeshji Marwaha
 * @since 9/7/14
 */

@MappedSuperclass
@ToString(includeNames = true, includePackage = false)
public class DbEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    @JsonProperty
    Long id;

}
