package com.blueberry

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter

/**
 * TODO: Document Me
 *
 * @author Ganeshji Marwaha
 * @since 9/7/14
 */
@Configuration
@EnableWebMvc
class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Override
    void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/home").setViewName("home");    // TODO: Not required
    }
}
