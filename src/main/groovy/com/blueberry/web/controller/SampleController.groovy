package com.blueberry.web.controller

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

import static org.springframework.web.bind.annotation.RequestMethod.*

/**
 * Sample controller for testing
 *
 * @author Ganeshji Marwaha
 * @since 9/6/14
 */
@RestController
@RequestMapping("/api/sample")
public class SampleController {

    @RequestMapping(value="", method = GET)
    String greet() {
        return "Hello world!"
    }

}