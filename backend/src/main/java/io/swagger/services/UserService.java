package io.swagger.services;

import io.swagger.model.entities.User;
import io.swagger.repositories.UserRepository;
import io.swagger.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.naming.AuthenticationException;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

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

    public String login(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        return tokenProvider.createToken(username, userRepository.findByUsername(username).getRoles());
    }
}

