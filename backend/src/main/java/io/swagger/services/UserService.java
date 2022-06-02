package io.swagger.services;

import io.swagger.model.dto.JWT_DTO;
import io.swagger.model.entities.User;
import io.swagger.repositories.UserRepository;
import io.swagger.jwt.JwtTokenProvider;

import java.util.Optional;
import java.util.UUID;

import io.swagger.utils.PincodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PincodeGenerator pincodeGenerator;

    public UserService (UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUser(UUID id) {
        return userRepository.findById(id).get();
    }

    public User getAllUsers(int limit, int offset) {  return (User) userRepository.findAllUsers(limit, offset);  }

    public JWT_DTO createUser(User user) {
        user.setPincode(pincodeGenerator.generatePincode());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        JWT_DTO jwt_dto = new JWT_DTO();
        jwt_dto.setJwTtoken(tokenProvider.createToken(user.getUsername(), user.getRoles()));
        return jwt_dto;
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

    // find by username
    public User findByUsername(String userName) {
        return userRepository.findByUsername(userName);
    }

    public JWT_DTO login(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        JWT_DTO jwt_dto = new JWT_DTO();
        jwt_dto.setJwTtoken(tokenProvider.createToken(username, userRepository.findByUsername(username).getRoles()));
        return jwt_dto;
    }
}

