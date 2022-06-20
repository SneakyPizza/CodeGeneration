package io.swagger.services;

import io.swagger.exception.custom.ForbiddenException;
import io.swagger.exception.custom.NotFoundException;
import io.swagger.exception.custom.UnauthorizedException;
import io.swagger.model.dto.GetUserDTO;
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
import org.springframework.security.core.context.SecurityContextHolder;
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

    private UUID id;

    private static final String USER_NOT_FOUND = "User not found";
    private static final String UNAUTHORIZED = "You are not authorized to perform this action";
    private static final String NULL_MESSAGE = "All fields must be filled in";

    public UserService (UserRepository userRepository) {
        pincodeGenerator = new PincodeGenerator();
        this.userRepository = userRepository;
    }

    public User getUser(String receivedId) {
        if (validateUUID(receivedId)) {
            id = UUID.fromString(receivedId);
        }
        else {
            throw new IllegalArgumentException("Invalid UUID");
        }

        if (!validateIfUserOwnsThisUser(id) && !validateIfAdmin()) {
            throw new UnauthorizedException(UNAUTHORIZED);
        }

        return userRepository.findById(id).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
    }

    public List<GetUserDTO> getAllUsers(Integer offset, Integer limit) {
        if (!validateIfAdmin()) {
            throw new ForbiddenException(UNAUTHORIZED);
        }
        else if (!validateOffset(offset)) {
            throw new IllegalArgumentException("Offset should be between 0 and the total number of users");
        }
        else {
            Pageable pageable = PageRequest.of(offset, limit);
            return getUserDTOs(userRepository.findAll(pageable).getContent());
        }
    }

    public User createUser(PostAsUserDTO postAsUserDTO) {
        if (validateUserFieldsNullAsUser(postAsUserDTO)) {
            throw new IllegalArgumentException(NULL_MESSAGE);
        }
        else {
            return userRepository.save(convertPostAsUserDTOtoUser(postAsUserDTO));
        }
    }

    public User createUserAdmin(PostUserDTO postUserDTO) {
        if (!validateIfAdmin()) {
            throw new ForbiddenException(UNAUTHORIZED);
        }
        else if (validateUserFieldsNullAsAdmin(postUserDTO)) {
            throw new IllegalArgumentException(NULL_MESSAGE);
        }
        else {
            User user = new User();
            user = user.setPropertiesFromPostUserDTO(postUserDTO);
            user.setPincode(pincodeGenerator.generatePincode());
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return userRepository.save(user);
        }
    }

    public User updateUser(PostUserDTO postUserDTO, String receivedId) {
        if (!validateIfAdmin()) {
            throw new ForbiddenException(UNAUTHORIZED);
        }
        else if (!validateUUID(receivedId)) {
            throw new IllegalArgumentException("Invalid UUID");
        }
        else {
            id = UUID.fromString(receivedId);
        }

        if (validateUserFieldsNullAsAdmin(postUserDTO)) {
            throw new IllegalArgumentException(NULL_MESSAGE);
        }
        else if (!validateUserExists(id)) {
            throw new NotFoundException(USER_NOT_FOUND);
        }
        else {
            User user = new User();
            user = user.setPropertiesFromPostUserDTO(postUserDTO);
            return userRepository.save(user);
        }
    }

    public List<User> findByFirstName(String firstname){
        return userRepository.findByFirstName(firstname).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
    }

    public List<User> findByLastName(String lastname){
        return userRepository.findByLastName(lastname).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
    }

    public JwtDTO login(String username, String password) {
        if (validateLogin(username, password)) {
            return createJwtDTO(username);
        } else {
            throw new UnauthorizedException("Invalid username or password");
        }
    }

    private boolean validateLogin (String username, String password) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
        if (user == null) {
            return false;
        }
        return passwordEncoder.matches(password, user.getPassword());
    }

    private boolean validateUUID(String id) {
        // check if id is correct format
        return id.matches("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}");
    }

    private boolean validateIfUserOwnsThisUser(UUID id) {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(name).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
        User userToCompare = userRepository.findById(id).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
        return user.getId().equals(userToCompare.getId());
    }

    private boolean validateUserExists(UUID id) {
        return userRepository.findById(id).isPresent();
    }

    private boolean validateIfAdmin() {
        // gets user from security context
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(name).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
        return user.getRoles().get(0).equals(Role.ROLE_ADMIN);
    }

    private boolean validateUserFieldsNullAsUser(PostAsUserDTO postAsUserDTO) {
        return postAsUserDTO.getFirstName() == null || postAsUserDTO.getLastName() == null || postAsUserDTO.getUsername() == null || postAsUserDTO.getPassword() == null || postAsUserDTO.getEmail() == null || postAsUserDTO.getStreet() == null || postAsUserDTO.getCity() == null || postAsUserDTO.getZipcode() == null || postAsUserDTO.getDayLimit() == null || postAsUserDTO.getTransactionLimit() == null;
    }

    private boolean validateUserFieldsNullAsAdmin(PostUserDTO postUserDTO) {
        return postUserDTO.getFirstName() == null || postUserDTO.getLastName() == null || postUserDTO.getUsername() == null || postUserDTO.getPassword() == null || postUserDTO.getEmail() == null || postUserDTO.getStreet() == null || postUserDTO.getCity() == null || postUserDTO.getZipcode() == null || postUserDTO.getDayLimit() == null || postUserDTO.getTransactionLimit() == null || postUserDTO.getRoles() == null || postUserDTO.getUserstatus() == null;
    }

    private User convertPostAsUserDTOtoUser(PostAsUserDTO postAsUserDTO) {
        User user = new User();
        user.setUsername(postAsUserDTO.getUsername());
        user.setPassword(postAsUserDTO.getPassword());
        user.setEmail(postAsUserDTO.getEmail());
        user.setFirstName(postAsUserDTO.getFirstName());
        user.setLastName(postAsUserDTO.getLastName());
        user.setStreet(postAsUserDTO.getStreet());
        user.setCity(postAsUserDTO.getCity());
        user.setZipcode(postAsUserDTO.getZipcode());
        user.setDayLimit(postAsUserDTO.getDayLimit());
        user.setTransactionLimit(postAsUserDTO.getTransactionLimit());

        user.setPincode(pincodeGenerator.generatePincode());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setUserstatus(UserStatus.DISABLED);
        user.setRoles(Collections.singletonList(Role.ROLE_USER));
        return user;
    }

    private boolean validateOffset(Integer offset) {
        List<User> users = (List<User>) userRepository.findAll();
        return offset != null && offset >= 0 && offset < (users.size() - 1);
    }

    private JwtDTO createJwtDTO(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
        JwtDTO jwtDTO = new JwtDTO();
        jwtDTO.setJwtToken(tokenProvider.createToken(user.getUsername(), user.getRoles()));
        jwtDTO.setId(user.getId().toString());
        return jwtDTO;
    }

    private List<GetUserDTO> getUserDTOs(List<User> users) {
        List<GetUserDTO> getUserDTOs = new java.util.ArrayList<>();
        for (User user : users) {
            getUserDTOs.add(user.getGetUserDTO());
        }
        return getUserDTOs;
    }
}

