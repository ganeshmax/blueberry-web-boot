package com.blueberry.framework.dto

import com.blueberry.framework.domain.DbEntity
import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.ObjectError

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import static org.springframework.http.HttpStatus.OK
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY

/**
 * Rest Response. This is the Dto that is always sent as a response to the rest client
 *
 * @author Ganeshji Marwaha
 * @since 9/12/14
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
class RestDto extends Dto {

    List<RestError> errors

    DbEntity entity

    List<? extends DbEntity> entities

    public static ResponseEntity<RestDto> forEntity(DbEntity entity,
                                                    HttpStatus status = OK) {
        return new ResponseEntity<RestDto>(new RestDto(entity: entity), status)
    }

    public static ResponseEntity<RestDto> forEntities(Collection<? extends DbEntity> entities,
                                                      HttpStatus status = OK) {
        return new ResponseEntity<RestDto>(new RestDto(entities: entities), status)
    }

    public static ResponseEntity<RestDto> forErrors(List<ObjectError> objectErrors,
                                                    HttpStatus status = UNPROCESSABLE_ENTITY) {
        List<RestError> errors = objectErrors.collect(new ArrayList<RestError>()) { new RestError(it) }
        return new ResponseEntity<RestDto>(new RestDto(errors: errors), status)
    }

    public static ResponseEntity<RestDto> forRestErrors(List<RestError> errors,
                                                        HttpStatus status = INTERNAL_SERVER_ERROR) {
        return new ResponseEntity<RestDto>(new RestDto(errors: errors), status)
    }

    public static ResponseEntity<RestDto> forStatus(HttpStatus status) {
        return new ResponseEntity<RestDto>(status)
    }

    public static ResponseEntity<RestDto> success() {
        forStatus(OK)
    }
}
