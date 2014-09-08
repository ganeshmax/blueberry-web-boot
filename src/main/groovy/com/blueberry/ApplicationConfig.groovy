package com.blueberry

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.transaction.annotation.EnableTransactionManagement

/**
 * Main application configuration class.
 * Spring boot compatible. This is found because it is given as input to Application.main method
 *
 * @author Ganeshji Marwaha
 * @since 9/7/14
 */
@Configuration
@EnableAutoConfiguration
@EnableTransactionManagement(proxyTargetClass = true)   // Required if you want to use Services without interfaces
@ComponentScan
@EnableJpaRepositories  // Optional: Spring boot would have added this
class ApplicationConfig {
}
