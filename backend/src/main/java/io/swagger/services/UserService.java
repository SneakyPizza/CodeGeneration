package io.swagger.services;

import io.swagger.model.entities.User;
import io.swagger.repositories.UserRepository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserService (UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //generate 4number pincode
    public String generatePincode(){
        StringBuilder pincode = new StringBuilder();
        for(int i = 0; i < 4; i++){
            pincode.append((int) (Math.random() * 10));
        }
        return pincode.toString();
    }

    public User getUser(UUID id) {
        return userRepository.findById(id).get();
    }

//    public User getAllUsers(int limit, int offset) {
//        return (User) userRepository.getAllUsers(limit, offset);
//    }

    public User createUser(User user) {
        user.setPincode(generatePincode());
        return userRepository.save(user);
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

    // find by username
    public User findByUsername(String userName) {
        return userRepository.findByUsername(userName);
    }
}

