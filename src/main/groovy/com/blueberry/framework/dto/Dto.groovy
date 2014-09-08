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

    List<ObjectError> errors

    DbEntity entity

    List<? extends DbEntity> entities

    public static ResponseEntity<Dto> forEntity(DbEntity entity) {
        return forEntity(entity, OK)
    }

    public static ResponseEntity<Dto> forEntity(DbEntity entity, HttpStatus status) {
        return new ResponseEntity<Dto>(new Dto(entity: entity), status)
    }

    public static ResponseEntity<Dto> forEntities(Collection<DbEntity> entities) {
        return forEntities(entities, OK)
    }

    public static ResponseEntity<Dto> forEntities(Collection<DbEntity> entities, HttpStatus status) {
        return new ResponseEntity<Dto>(new Dto(entities: entities), OK)
    }

    public static ResponseEntity<Dto> forErrors(List<ObjectError> errors) {
        return forErrors(errors, UNPROCESSABLE_ENTITY)
    }

    public static ResponseEntity<Dto> forErrors(List<ObjectError> errors, HttpStatus status) {
        return new ResponseEntity<Dto>(new Dto(errors: errors), status)
    }

    public static ResponseEntity<Dto> forStatus(HttpStatus status) {
        return new ResponseEntity<Dto>(status)
    }

    public static ResponseEntity<Dto> success() {
        forStatus(OK)
    }
}
