package com.blueberry

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.transaction.annotation.EnableTransactionManagement

/**
 * TODO: Document Me
 *
 * @author Ganeshji Marwaha
 * @since 9/7/14
 */
@Configuration
@EnableAutoConfiguration
@EnableTransactionManagement(proxyTargetClass = true)   // Required if you want to use Services without interfaces
@ComponentScan
class ApplicationConfig {
}
