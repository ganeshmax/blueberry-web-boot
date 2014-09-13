package com.blueberry.presentation

import com.blueberry.framework.dto.RestDto
import com.blueberry.framework.dto.RestError
import groovy.util.logging.Slf4j
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody

import javax.persistence.EntityNotFoundException

import static org.springframework.http.HttpStatus.*

/**
 * Advice for REST controllers to handle uncaught exceptions
 *
 * @author Ganeshji Marwaha
 * @since 9/11/14
 */
@ControllerAdvice
@Slf4j
class RestControllerAdvice {

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseBody
    public ResponseEntity<RestDto> notFound() {
        log.trace '<< RestControllerAdvice.notFound >>'
        return RestDto.forStatus(NOT_FOUND)
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<RestDto> catchAll() {
        log.error '<< RestControllerAdvice.catchAll >>'
        return RestDto.forRestErrors(
                [new RestError(code: '500', message: 'Internal Server Error')], INTERNAL_SERVER_ERROR)
    }
}
