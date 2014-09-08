package com.blueberry.controller

import com.blueberry.domain.Dto
import com.blueberry.domain.Role
import com.blueberry.domain.RoleType
import com.blueberry.domain.User

import com.blueberry.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.ResponseEntity
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
public class UserController {

    @Autowired
    UserService userService

    @RequestMapping(value="", method = GET)
    List<User> index() {
        return userService.findAll()
    }

    @RequestMapping(value="/{id}", method = GET)
    ResponseEntity<? extends Dto> show(@PathVariable Long id) {
        User user = userService.findUserById(id)

        if(user == null) {
            return new ResponseEntity<User>(NOT_FOUND)
        } else {
            return new ResponseEntity<User>(user, OK)
        }
    }

    @RequestMapping(value="", method = POST)
    ResponseEntity<? extends Dto> save(@RequestBody @Valid User user, BindingResult result) {

        if(result.hasErrors()) {
            return new ResponseEntity<Dto>(new Dto(errors: result.getAllErrors()), UNPROCESSABLE_ENTITY);
        } else {
            user.addRole(new Role(name: RoleType.ROLE_USER.name()))
            userService.create(user);

            return new ResponseEntity<User>(user, CREATED)
        }
    }

    @RequestMapping(value="/{id}", method = PUT)
    ResponseEntity<? extends Dto> update(@PathVariable Long id, @RequestBody @Valid User changedUser, BindingResult result) {
        User currentUser = User.get(id)

        if(currentUser == null) {
            return new ResponseEntity<User>(NOT_FOUND)
        } else {
            if(result.hasErrors()) {
                return new ResponseEntity<Dto>(new Dto(errors: result.getAllErrors()), UNPROCESSABLE_ENTITY)
            } else {

                // TODO: Revisit how to handle this
                User changedSavedUser = userService.update(changedUser)
                return new ResponseEntity<User>(changedSavedUser, OK)
            }
        }
    }

    @RequestMapping(value="/{id}", method = DELETE)
    ResponseEntity<? extends Dto> delete(@PathVariable Long id) {
        try {
            userService.deleteUser(id)
            return new ResponseEntity<User>(NO_CONTENT)
        } catch (EmptyResultDataAccessException e) {
            return new ResponseEntity<User>(NOT_FOUND)
        }
    }
}
