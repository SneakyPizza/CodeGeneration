package io.swagger.services;

import io.swagger.exception.custom.ForbiddenException;
import io.swagger.exception.custom.NotFoundException;
import io.swagger.exception.custom.UnauthorizedException;
import io.swagger.model.dto.GetUserDTO;
import io.swagger.model.dto.JwtDTO;
import io.swagger.model.dto.PostAsUserDTO;
import io.swagger.model.entities.Role;
import io.swagger.model.dto.PostUserDTO;
import io.swagger.model.entities.Users;
import io.swagger.model.entities.UserStatus;
import io.swagger.repositories.UserRepository;
import io.swagger.jwt.JwtTokenProvider;

import java.util.*;

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
    private static final String WRONG_FIELDS_MESSAGE = "All fields must be filled in";

    public UserService (UserRepository userRepository) {
        pincodeGenerator = new PincodeGenerator();
        this.userRepository = userRepository;
    }

    public Users getUser(String receivedId) {
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
        if (validateUserFieldsNullOrEmptyAsUser(postAsUserDTO)) {
            throw new IllegalArgumentException(WRONG_FIELDS_MESSAGE);
        }
        else {
            return userRepository.save(convertPostAsUserDTOtoUser(postAsUserDTO));
        }
    }

    public Users createUserAdmin(PostUserDTO postUserDTO) {
        if (!validateIfAdmin()) {
            throw new ForbiddenException(UNAUTHORIZED);
        }
        else if (validateUserFieldsNullOrEmptyAsAdmin(postUserDTO)) {
            throw new IllegalArgumentException(WRONG_FIELDS_MESSAGE);
        }
        else {
            Users users = new Users();
            users = users.setPropertiesFromPostUserDTO(postUserDTO);
            users.setPincode(pincodeGenerator.generatePincode());
            users.setPassword(passwordEncoder.encode(users.getPassword()));
            return userRepository.save(users);
        }
    }

    public Users updateUser(PostUserDTO postUserDTO, String receivedId) {
        if (!validateIfAdmin()) {
            throw new ForbiddenException(UNAUTHORIZED);
        }
        else if (!validateUUID(receivedId)) {
            throw new IllegalArgumentException("Invalid UUID");
        }
        else {
            id = UUID.fromString(receivedId);
        }

        if (validateUserFieldsNullOrEmptyAsAdmin(postUserDTO)) {
            throw new IllegalArgumentException(WRONG_FIELDS_MESSAGE);
        }
        else if (!validateUserExists(id)) {
            throw new NotFoundException(USER_NOT_FOUND);
        }
        else {
            User correctionUser = userRepository.findById(id).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
            User user = new User();
            user = user.setPropertiesFromPostUserDTO(postUserDTO);
            user.setId(id);
            user.setPincode(correctionUser.getPincode());
            user.setAccounts(correctionUser.getAccounts());

            // checks if password is the same as the old one
            User compareUser = userRepository.findById(id).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
            if (!validateIfPasswordIsTheSame(postUserDTO.getPassword(), compareUser.getPassword())) {
                user.setPassword(passwordEncoder.encode(postUserDTO.getPassword()));
            }
            return userRepository.save(user);
        }
    }

    public List<Users> findByFirstName(String firstname){
        return userRepository.findByFirstName(firstname).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
    }

    public List<Users> findByLastName(String lastname){
        return userRepository.findByLastName(lastname).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
    }

    public Users findByUsername(String username) {
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
        Users users = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
        if (users == null) {
            return false;
        }
        return passwordEncoder.matches(password, users.getPassword());
    }

    private boolean validateUUID(String id) {
        // check if id is correct format
        return id.matches("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}");
    }

    private boolean validateIfUserOwnsThisUser(UUID id) {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        Users users = userRepository.findByUsername(name).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
        Users usersToCompare = userRepository.findById(id).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
        return users.getId().equals(usersToCompare.getId());
    }

    private boolean validateUserExists(UUID id) {
        return userRepository.findById(id).isPresent();
    }

    private boolean validateIfAdmin() {
        // gets user from security context
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(name).orElseThrow(() -> new UnauthorizedException(UNAUTHORIZED));
        return user.getRoles().get(0).equals(Role.ROLE_ADMIN);
    }

    private boolean validateIfPasswordIsTheSame(String password, String confirmPassword) {
        return passwordEncoder.matches(password, confirmPassword);
    }

    private boolean validateUserFieldsNullOrEmptyAsUser(PostAsUserDTO postAsUserDTO) {
        return postAsUserDTO.getFirstName() == null || postAsUserDTO.getLastName() == null || postAsUserDTO.getUsername() == null || postAsUserDTO.getPassword() == null || postAsUserDTO.getEmail() == null || postAsUserDTO.getStreet() == null || postAsUserDTO.getCity() == null || postAsUserDTO.getZipcode() == null || postAsUserDTO.getDayLimit() == null || postAsUserDTO.getTransactionLimit() == null || Objects.equals(postAsUserDTO.getFirstName(), "") || Objects.equals(postAsUserDTO.getLastName(), "") || Objects.equals(postAsUserDTO.getUsername(), "") || Objects.equals(postAsUserDTO.getPassword(), "") || Objects.equals(postAsUserDTO.getEmail(), "") || Objects.equals(postAsUserDTO.getStreet(), "") || Objects.equals(postAsUserDTO.getCity(), "") || Objects.equals(postAsUserDTO.getZipcode(), "");

    }

    private boolean validateUserFieldsNullOrEmptyAsAdmin(PostUserDTO postUserDTO) {
        return postUserDTO.getFirstName() == null || postUserDTO.getLastName() == null || postUserDTO.getUsername() == null || postUserDTO.getPassword() == null || postUserDTO.getEmail() == null || postUserDTO.getStreet() == null || postUserDTO.getCity() == null || postUserDTO.getZipcode() == null || postUserDTO.getDayLimit() == null || postUserDTO.getTransactionLimit() == null || postUserDTO.getRoles() == null || postUserDTO.getUserstatus() == null || Objects.equals(postUserDTO.getFirstName(), "") || Objects.equals(postUserDTO.getLastName(), "") || Objects.equals(postUserDTO.getUsername(), "") || Objects.equals(postUserDTO.getPassword(), "") || Objects.equals(postUserDTO.getEmail(), "") || Objects.equals(postUserDTO.getStreet(), "") || Objects.equals(postUserDTO.getCity(), "") || Objects.equals(postUserDTO.getZipcode(), "");
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

        users.setPincode(pincodeGenerator.generatePincode());
        users.setPassword(passwordEncoder.encode(users.getPassword()));
        users.setUserstatus(UserStatus.DISABLED);
        users.setRoles(Collections.singletonList(Role.ROLE_USER));
        return users;
    }

    private boolean validateOffset(Integer offset) {
        List<Users> users = (List<Users>) userRepository.findAll();
        return offset != null && offset >= 0 && offset < (users.size() - 1);
    }

    private JwtDTO createJwtDTO(String username) {
        Users users = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
        JwtDTO jwtDTO = new JwtDTO();
        jwtDTO.setJwtToken(tokenProvider.createToken(users.getUsername(), users.getRoles()));
        jwtDTO.setId(users.getId().toString());
        return jwtDTO;
    }

    private List<GetUserDTO> getUserDTOs(List<User> users) {
        List<GetUserDTO> getUserDTOs = new ArrayList<>();
        for (User user : users) {
            getUserDTOs.add(user.getGetUserDTO());
        }
        return getUserDTOs;
    }
    
    public boolean checkIfUserExists(UUID id) {
        return userRepository.existsById(id);
    }


}

