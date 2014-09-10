package com.blueberry.presentation.rest

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

import static org.springframework.web.bind.annotation.RequestMethod.GET

/**
 * Sample rest controller.
 *
 * @author Ganeshji Marwaha
 * @since 9/9/14
 */
@RestController
@RequestMapping("/rest/sample")
@PreAuthorize("hasRole('ROLE_ADMIN')")
class SampleRestController {

    @RequestMapping(value="/greet", method = GET)
    String greet() {
        return "Hello REST!"
    }

}
