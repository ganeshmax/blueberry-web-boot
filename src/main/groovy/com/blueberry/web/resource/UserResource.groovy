package com.blueberry.web.resource

import com.blueberry.framework.dto.Dto
import com.blueberry.model.domain.Role
import com.blueberry.model.dto.RoleType
import com.blueberry.model.domain.User

import com.blueberry.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

import javax.validation.Valid

import static org.springframework.http.HttpStatus.*
import static org.springframework.web.bind.annotation.RequestMethod.*

/**
 * User resource.
 *
 * @author Ganeshji Marwaha
 * @since 9/7/14
 */

@RestController
@RequestMapping("/api/users")
public class UserResource {

    @Autowired
    UserService userService

    @RequestMapping(value="", method = GET)
    @PreAuthorize("hasRole('ROLE_USER')")
    ResponseEntity<? extends Dto> index() {
        return Dto.forEntities(userService.findAll())
    }

    @RequestMapping(value="/{id}", method = GET)
    @PreAuthorize("hasRole('ROLE_USER')")
    ResponseEntity<? extends Dto> show(@PathVariable Long id) {
        User user = userService.findUserById(id)

        if(user == null) {
            return Dto.forStatus(NOT_FOUND)
        } else {
            return Dto.forEntity(user)
        }
    }

    @RequestMapping(value="", method = POST)
    ResponseEntity<? extends Dto> save(@RequestBody @Valid User user, BindingResult result) {

        if(result.hasErrors()) {
            Dto.forErrors(result.getAllErrors())
        } else {
            user.addRole(new Role(name: RoleType.ROLE_USER.name()))
            userService.create(user)

            return Dto.forEntity(user, CREATED)
        }
    }

    @RequestMapping(value="/{id}", method = PUT)
    ResponseEntity<? extends Dto> update(@PathVariable Long id, @RequestBody @Valid User changedUser, BindingResult result) {
        User currentUser = userService.findUserById(id)

        if(currentUser == null) {
            return Dto.forStatus(NOT_FOUND)
        } else {
            if(result.hasErrors()) {
                return Dto.forErrors(result.getAllErrors())
            } else {
                // TODO: Revisit how to handle this
                User changedSavedUser = userService.update(changedUser)
                return Dto.forEntity(changedSavedUser)
            }
        }
    }

    @RequestMapping(value="/{id}", method = DELETE)
    ResponseEntity<? extends Dto> delete(@PathVariable Long id) {
        try {
            userService.deleteUser(id)
            return Dto.forStatus(NO_CONTENT)
        } catch (EmptyResultDataAccessException e) {
            return Dto.forStatus(NOT_FOUND)
        }
    }
}
