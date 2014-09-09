package com.blueberry.web.controller

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

import static org.springframework.web.bind.annotation.RequestMethod.*

/**
 * Test web controller
 *
 * @author Ganeshji Marwaha
 * @since 9/6/14
 */
@RestController
@RequestMapping("/web")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class WebController {

    @RequestMapping(value="/greet", method = GET)
    String greet() {
        return "Hello Web!"
    }

}