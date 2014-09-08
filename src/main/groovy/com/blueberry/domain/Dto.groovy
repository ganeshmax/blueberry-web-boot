package com.blueberry.domain

import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.validation.ObjectError

import javax.persistence.MappedSuperclass
import javax.persistence.Transient

/**
 * DTO will represent any data transfer object in the system.
 * All DBEntities are implicitly DTOs as well
 *
 * @author Ganeshji Marwaha
 * @since 9/7/14
 */
@MappedSuperclass
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Dto implements Serializable {
    @Transient
    List<ObjectError> errors;

}
