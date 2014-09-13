package com.blueberry.framework.dto

import com.blueberry.framework.domain.DbEntity
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.ObjectError

import static org.springframework.http.HttpStatus.*

/**
 * DTO will represent any data transfer object in the system.
 * All DBEntities are implicitly DTOs as well
 *
 * @author Ganeshji Marwaha
 * @since 9/7/14
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Dto implements Serializable {


}
