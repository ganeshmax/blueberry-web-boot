package com.blueberry.presentation.web

import com.blueberry.framework.presentation.PresentationWebController
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

import static org.springframework.web.bind.annotation.RequestMethod.*


/**
 * Sample web controller.
 *
 * @author Ganeshji Marwaha
 * @since 9/6/14
 */
@Controller
@RequestMapping("/web")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class SampleWebController extends PresentationWebController {

    @RequestMapping(value="/greet", method = GET)
    String greet() {
        return "Hello Web!"
    }

}