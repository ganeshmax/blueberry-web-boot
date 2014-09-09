package com.blueberry.web.resource

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

import static org.springframework.web.bind.annotation.RequestMethod.GET

/**
 * Test Rest Resource
 *
 * @author Ganeshji Marwaha
 * @since 9/9/14
 */
@RestController
@RequestMapping("/rest")
@PreAuthorize("hasRole('ROLE_ADMIN')")
class RestResource {

    @RequestMapping(value="/greet", method = GET)
    String greet() {
        return "Hello REST!"
    }

}
