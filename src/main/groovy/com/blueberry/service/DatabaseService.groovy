package com.blueberry.service

import com.blueberry.domain.Role
import com.blueberry.domain.User
import com.blueberry.repository.RoleRepository
import com.blueberry.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import javax.transaction.Transactional

/**
 * TODO: Document Me
 *
 * @author Ganeshji Marwaha
 * @since 9/8/14
 */
@Service
@Transactional
class DatabaseService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    public void initializeDatabase() {
        Role adminRole = roleRepository.saveAndFlush(new Role(name: 'ROLE_ADMIN'))
        Role operatorRole = roleRepository.saveAndFlush(new Role(name: 'ROLE_OPERATOR'))
        Role providerRole = roleRepository.saveAndFlush(new Role(name: 'ROLE_PROVIDER'))
        Role receiverRole = roleRepository.saveAndFlush(new Role(name: 'ROLE_RECEIVER'))
        Role userRole = roleRepository.saveAndFlush(new Role(name: 'ROLE_USER'))

        userRepository
                .saveAndFlush(new User(email: 'admin1@email.com', password: 'password'))
                .addRole(adminRole)

        userRepository
                .saveAndFlush(new User(email: 'user1@email.com', password: 'password'))
                .addRole(userRole)

        userRepository
                .saveAndFlush(new User(email: 'admin2@email.com', password: 'password'))
                .addRole(adminRole)

        userRepository
                .saveAndFlush(new User(email: 'user2@email.com', password: 'password'))
                .addRole(userRole)
    }
}
