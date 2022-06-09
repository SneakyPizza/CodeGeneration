package io.swagger.services;

import io.swagger.model.dto.JWT_DTO;
import io.swagger.model.entities.User;
import io.swagger.repositories.UserRepository;
import io.swagger.jwt.JwtTokenProvider;

import java.awt.print.Pageable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import io.swagger.utils.PincodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private PincodeGenerator pincodeGenerator;

    public UserService (UserRepository userRepository) {
        pincodeGenerator = new PincodeGenerator();
        this.userRepository = userRepository;
    }

    public User getUser(UUID id) {
        return userRepository.findById(id).get();
    }

    public List<User> getAllUsers() {
        return userRepository.findAll(); // doesnt work properly yet
    }

    public User createUser(User user) {
        user.setId(UUID.randomUUID());
        user.setPincode(pincodeGenerator.generatePincode());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public boolean checkIfUserExists(UUID id) {
        return userRepository.existsById(id);
    }

    public boolean checkIfUserExistsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

    // find by username
    public User findByUsername(String userName) {
        return userRepository.findByUsername(userName);
    }

    public List<User> findByFirstName(String firstname){
        return userRepository.findByFirstName(firstname);
    }

    public List<User> findByLastName(String lastname){
        return userRepository.findByLastName(lastname);
    }
    public JWT_DTO login(String username, String password) {
        JWT_DTO jwt_dto = new JWT_DTO();
        User user = userRepository.findByUsername(username);
        jwt_dto.setJwTtoken(tokenProvider.createToken(username, user.getRoles()));
        jwt_dto.setId(user.getId().toString());
        return jwt_dto;
    }
}

