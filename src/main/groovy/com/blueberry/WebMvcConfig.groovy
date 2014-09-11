package com.blueberry

import org.springframework.context.annotation.Configuration
import org.springframework.data.web.config.EnableSpringDataWebSupport
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter

/**
 * Configure Web MVC related items.
 * This is found because of ComponentScan in ApplicationConfig.
 *
 * @author Ganeshji Marwaha
 * @since 9/7/14
 */
@Configuration
@EnableWebMvc                   // Optional: Spring boot would have handled it anyways
@EnableSpringDataWebSupport     // Spring MVC Domain class converter and Pageable/Sort support reducing boilerplate
class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Override
    void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/home").setViewName("home");    // TODO: Not required
    }
}
