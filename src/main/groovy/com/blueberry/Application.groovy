package com.blueberry

import com.blueberry.service.DatabaseService
import org.springframework.boot.SpringApplication
import org.springframework.context.ConfigurableApplicationContext

/**
 * Main
 *
 * @author Ganeshji Marwaha
 * @since 9/6/14
 */
class Application {

    static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run ApplicationConfig, args
        initializeDatabase(context)
    }

    static void initializeDatabase(ConfigurableApplicationContext context) {
        DatabaseService databaseService = context.getBean(DatabaseService.class)
        databaseService.initializeDatabase()
    }
}
