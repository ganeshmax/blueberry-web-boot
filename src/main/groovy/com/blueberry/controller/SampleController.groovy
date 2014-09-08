package com.blueberry.controller

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

import static org.springframework.web.bind.annotation.RequestMethod.*

/**
 * TODO: Document Me
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