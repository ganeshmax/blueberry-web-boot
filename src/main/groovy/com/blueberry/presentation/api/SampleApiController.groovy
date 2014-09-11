package com.blueberry.presentation.api

import com.blueberry.framework.presentation.PresentationRestController
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

import static org.springframework.web.bind.annotation.RequestMethod.GET

/**
 * Sample api controller
 *
 * @author Ganeshji Marwaha
 * @since 9/9/14
 */

@RestController
@RequestMapping("/api/sample")
@PreAuthorize("hasRole('ROLE_ADMIN')")
class SampleApiController extends PresentationRestController {

    @RequestMapping(value="/greet", method = GET)
    String greet() {

        return "Hello API!"
    }
}
