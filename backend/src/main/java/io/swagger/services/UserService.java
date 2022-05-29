package io.swagger.services;

import io.swagger.model.entities.User;
import io.swagger.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;


    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User updateUser(String userId, User user) {
        return userRepository.save(user);
    }

    public User getUserByUserName(String userName) {
        return userRepository.findByUsername(userName);
    }

    //finf by username
    public User findByUsername(String userName) {
        return userRepository.findByUsername(userName);
    }
}

