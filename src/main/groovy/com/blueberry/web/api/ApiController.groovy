package com.blueberry.web.api

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

import static org.springframework.web.bind.annotation.RequestMethod.GET

/**
 * Test Api Controller
 *
 * @author Ganeshji Marwaha
 * @since 9/9/14
 */

@RestController
@RequestMapping("/api")
@PreAuthorize("hasRole('ROLE_ADMIN')")
class ApiController {

    @RequestMapping(value="/greet", method = GET)
    String greet() {
        return "Hello API!"
    }
}
