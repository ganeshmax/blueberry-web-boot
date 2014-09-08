package com.blueberry.service

import com.blueberry.model.dto.SpringUserDetails
import com.blueberry.model.domain.User
import com.blueberry.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

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
public class UserService implements UserDetailsService {

    @Autowired
    UserRepository userRepository

    public User login(String email, String password) {
        userRepository.findByEmailAndPassword(email,password)
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User create(User user) {
        return userRepository.save(user);
    }

    public User findUserById(Long id) {
        return userRepository.findOne(id);
    }

    public User update(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.delete(id);
    }

    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = findUserByEmail(userName);
        if(user == null){
            throw new UsernameNotFoundException("UserName "+userName+" not found");
        }
        return new SpringUserDetails(user);
    }

}
