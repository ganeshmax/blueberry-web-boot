package com.blueberry.presentation

import com.blueberry.framework.dto.Dto
import com.blueberry.util.DebugUtil
import groovy.util.logging.Slf4j
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus

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
    public ResponseEntity<Dto> notFound() {
        log.trace '<< RestControllerAdvice.notFound >>'
        return Dto.forStatus(NOT_FOUND)
    }
}
