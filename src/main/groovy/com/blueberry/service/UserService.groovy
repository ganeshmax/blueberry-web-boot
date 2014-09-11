package com.blueberry.service

import com.blueberry.model.domain.Role
import com.blueberry.model.domain.User
import com.blueberry.model.dto.RoleType
import com.blueberry.model.dto.SpringUserDetails
import com.blueberry.repository.RoleRepository
import com.blueberry.repository.UserRepository
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

import javax.persistence.EntityNotFoundException
import javax.transaction.Transactional

/**
 * Services for User and related domain objects.
 * This also implements UserDetailsService required by spring-security. This, in combination with SpringUserDetails
 * adapts our application model to the model spring expects.
 *
 * @author Ganeshji Marwaha
 * @since 9/7/14
 */
@Service
@Transactional
@Slf4j
public class UserService implements UserDetailsService {

    @Autowired
    UserRepository userRepository

    @Autowired
    RoleRepository roleRepository

    public User findByEmailAndPassword(String email, String password) {
        userRepository.findByEmailAndPassword(email,password)
    }

    public User findByEmail(String email) {
        return userRepository.findUserByEmail(email)
    }

    public List<User> findAll() {
        return userRepository.findAll()
    }

    public User findById(Long id) {
        return userRepository.findOne(id)
    }

    public User create(User user, RoleType roleType = RoleType.ROLE_USER) {
        Role role = roleRepository.findByName(roleType.name())
        user.addRole(role)
        return userRepository.save(user)
    }

    /**
     * Update a user with given id using changeUser's attributes
     * @param id
     * @param changedUser
     * @throws EntityNotFoundException if User with the incoming "id" is not found
     * @return
     */
    public User update(Long id, User changedUser) {
        User currentUser = findById(id)
        if(currentUser == null) {
            throw new EntityNotFoundException("User for id: {id} was not found")
        }
        currentUser.update(changedUser)
        return currentUser
    }

    /**
     * Delete user by given id
     * @throws EntityNotFoundException if User with the incoming "id" is not found
     * @param id
     */
    public void delete(Long id) {
        try {
            userRepository.delete(id)
        } catch(EmptyResultDataAccessException e) {
            throw new EntityNotFoundException("User with id: {id} was not found")
        }
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        log.info("===== UserDetails.loadByUserName")
        User user = findByEmail(userName)
        if(user == null){
            throw new UsernameNotFoundException("UserName "+userName+" not found")
        }
        return new SpringUserDetails(user)
    }

}
