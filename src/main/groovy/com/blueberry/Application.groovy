package com.blueberry

import com.blueberry.domain.Role
import com.blueberry.domain.User
import com.blueberry.repository.UserRepository
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

        UserRepository userRepository = context.getBean(UserRepository.class);

        def adminRole = new Role(name: 'ROLE_ADMIN')
        def userRole = new Role(name: 'ROLE_USER')

        def adminUser = new User(email: 'admin@email.com', password: 'password')
        adminUser.addRole(adminRole)

        def userUser = new User(email: 'user@email.com', password: 'password')
        userUser.addRole(userRole)

        userRepository.save(adminUser);
        userRepository.save(userUser);
    }
}
