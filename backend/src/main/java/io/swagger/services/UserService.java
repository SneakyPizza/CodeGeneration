package io.swagger.services;

import io.swagger.exception.custom.NotFoundException;
import io.swagger.exception.custom.UnauthorizedException;
import io.swagger.model.dto.JwtDTO;
import io.swagger.model.dto.PostAsUserDTO;
import io.swagger.model.entities.Role;
import io.swagger.model.dto.PostUserDTO;
import io.swagger.model.entities.User;
import io.swagger.model.entities.UserStatus;
import io.swagger.repositories.UserRepository;
import io.swagger.jwt.JwtTokenProvider;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import io.swagger.utils.PincodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
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

    private final PincodeGenerator pincodeGenerator;

    private User u;

    private static final String userNotFound = "User not found";

    public UserService (UserRepository userRepository) {
        pincodeGenerator = new PincodeGenerator();
        this.userRepository = userRepository;
    }

    public User getUser(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException(userNotFound));
    }

    public List<User> getAllUsers(Integer offset, Integer limit) {
        if (!validateLimit(limit)) {
            throw new IllegalArgumentException("Limit must be between 1 and 50");
        }
        if (!validateOffset(offset)) {
            throw new IllegalArgumentException("Offset should be between 0 and the total number of users");
        }
        Pageable pageable = PageRequest.of(offset, limit);
        return userRepository.findAll(pageable).getContent();
    }

    public User createUser(PostAsUserDTO postAsUserDTO) {
        User user = u.getUserModelFromPostAsUserDTO(postAsUserDTO);
        user.setId(UUID.randomUUID());
        user.setPincode(pincodeGenerator.generatePincode());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setUserstatus(UserStatus.DISABLED);
        user.setRoles(Collections.singletonList(Role.ROLE_USER));
        return userRepository.save(user);
    }

    public User createUserAdmin(PostUserDTO postUserDTO) {
        User user = u.getUserModelFromPostUserDTO(postUserDTO);
        user.setId(UUID.randomUUID());
        user.setPincode(pincodeGenerator.generatePincode());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User updateUser(PostUserDTO postUserDTO) {
        User user = new User();
        user = user.getUserModelFromPostUserDTO(postUserDTO);
        return userRepository.save(user);
    }

    public List<User> findByFirstName(String firstname){
        return userRepository.findByFirstName(firstname).orElseThrow(() -> new NotFoundException(userNotFound));
    }

    public List<User> findByLastName(String lastname){
        return userRepository.findByLastName(lastname).orElseThrow(() -> new NotFoundException(userNotFound));
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException(userNotFound));
    }

    public JwtDTO login(String username, String password) {
        if (validateLogin(username, password)) {
            return createJwtDTO(username);
        } else {
            throw new UnauthorizedException("Invalid username or password");
        }
    }

    private boolean validateLogin (String username, String password) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException(userNotFound));
        if (user == null) {
            return false;
        }
        return passwordEncoder.matches(password, user.getPassword());
    }

    private boolean validateLimit(Integer limit) {
        return limit != null && limit >= 1 && limit <= 50;
    }

    private boolean validateOffset(Integer offset) {
        List<User> users = (List<User>) userRepository.findAll();
        return offset != null && offset >= 0 && offset < (users.size() - 1);
    }

    private JwtDTO createJwtDTO(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException(userNotFound));
        JwtDTO jwtDTO = new JwtDTO();
        jwtDTO.setJwtToken(tokenProvider.createToken(user.getUsername(), user.getRoles()));
        jwtDTO.setId(user.getId().toString());
        return jwtDTO;
    }
}

