package com.blueberry.presentation.rest

import com.blueberry.framework.dto.Dto
import com.blueberry.framework.presentation.PresentationRestController
import com.blueberry.model.domain.User
import com.blueberry.service.UserService
import org.springframework.beans.factory.annotation.Autowired
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
@PreAuthorize("hasRole('ROLE_USER')")
@RequestMapping("/rest/users")
public class UserController extends PresentationRestController {

    @Autowired
    UserService userService

    @RequestMapping(value="", method = GET)
    ResponseEntity<Dto> index() {
        return Dto.forEntities(userService.findAll())
    }

    @RequestMapping(value="/{id}", method = GET)
    ResponseEntity<Dto> show(@PathVariable Long id) {
        User user = userService.findById(id)
        return user ? Dto.forEntity(user) : Dto.forStatus(NOT_FOUND)
    }

    @RequestMapping(value="", method = POST)
    ResponseEntity<Dto> save(@RequestBody @Valid User user,
                             BindingResult result) {

        if(result.hasErrors()) {
            return Dto.forErrors(result.getAllErrors())
        }

        // Add any user with a default role of ROLE_USER
        userService.create(user)

        return Dto.forEntity(user, CREATED)
    }

    /**
     * Do not use DomainClassConverter to auto-fetch user for update methods. Use that for get() style methods only
     * @param currentUser
     * @param changedUser
     * @param result
     * @return
     */
    @RequestMapping(value="/{id}", method = PUT)
    ResponseEntity<Dto> update(@PathVariable Long id,
                               @RequestBody @Valid User changedUser,
                               BindingResult result) {

        if(result.hasErrors()) {
            return Dto.forErrors(result.getAllErrors())
        }

        User changedSavedUser = userService.update(id, changedUser)
        return Dto.forEntity(changedSavedUser)

    }

    @RequestMapping(value="/{id}", method = DELETE)
    ResponseEntity<Dto> delete(@PathVariable Long id) {
        userService.delete(id)
        return Dto.forStatus(NO_CONTENT)
    }
}
