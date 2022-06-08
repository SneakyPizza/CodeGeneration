package io.swagger.api;

import io.swagger.annotations.Api;
import io.swagger.model.GetUserDTO;
import io.swagger.model.dto.ErrorDTO;
import io.swagger.model.UserDTO;
import io.swagger.model.entities.User;
import io.swagger.jwt.JwtTokenProvider;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.services.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.*;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-05-04T11:04:07.506Z[GMT]")
@RestController
@Api(tags = {"Users"}, description = "the user API")
public class UsersApiController implements UsersApi {

    private static final Logger log = LoggerFactory.getLogger(UsersApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @org.springframework.beans.factory.annotation.Autowired
    public UsersApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<? extends Object> addUser(@Parameter(in = ParameterIn.DEFAULT, description = "", schema=@Schema()) @Valid @RequestBody UserDTO userDTO) {
        try {
            if (userDTO == null) {
                // checks if null
                return new ResponseEntity<ErrorDTO>(new ErrorDTO(LocalDateTime.now().toString(), "Bad request: given user is null", HttpStatus.BAD_REQUEST.value(), "BAD_REQUEST"), HttpStatus.BAD_REQUEST);
            }
            /*// checks if all fields are filled
            else if (userDTO.getUserid() == null || userDTO.getUsername() == null || userDTO.getPassword() == null || userDTO.getEmail() == null || userDTO.getFirstName() == null || userDTO.getLastName() == null || userDTO.getStreet() == null || userDTO.getCity() == null || userDTO.getZipcode() == null || userDTO.getUserstatus() == null || userDTO.getDayLimit() == null || userDTO.getTransactionLimit() == null || userDTO.getRoles() == null) {
                return new ResponseEntity<ErrorDTO>(new ErrorDTO(LocalDateTime.now().toString(), "Bad request: given user is incomplete", HttpStatus.BAD_REQUEST.value(), "BAD_REQUEST"), HttpStatus.BAD_REQUEST);
            }*/
            else {
                if (userService.findByUsername(userDTO.getUsername()) != null) {
                    return new ResponseEntity<ErrorDTO>(new ErrorDTO(LocalDateTime.now().toString(), "Bad request: given user already exists", HttpStatus.BAD_REQUEST.value(), "BAD_REQUEST"), HttpStatus.BAD_REQUEST);
                }
                else {
                    User user = new User();
                    user = user.getUserModel(userDTO);
                    // creates user in db
                    User createdUser = userService.createUser(user);
                    return new ResponseEntity<UserDTO>(createdUser.getUserDTO(), HttpStatus.OK);
                }
            }
        } catch (Exception e) { // no IOException because object mapper does not work
            log.error("Internal server error", e);
            return new ResponseEntity<ErrorDTO>(new ErrorDTO(LocalDateTime.now().toString(), "An internal server error had occured!", 500, "INTERNAL_SERVER_ERROR"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<? extends Object> getAllUsers(@Min(0)@Parameter(in = ParameterIn.QUERY, description = "The number of items to skip before starting to collect the result set." ,schema=@Schema(allowableValues={  }
)) @Valid @RequestParam(value = "offset", required = false) Integer offset, @Min(1) @Max(50) @Parameter(in = ParameterIn.QUERY, description = "The numbers of items to return." ,schema=@Schema(allowableValues={  }, minimum="1", maximum="50"
, defaultValue="20")) @Valid @RequestParam(value = "limit", required = false, defaultValue="20") Integer limit) {
        String accept = request.getHeader("Accept");
        try {
            // if unset or too low set to default value
            if (limit == null || limit < 1) {
                limit = 1;
            }

            // if unset or too low set to default value
            if (offset == null || offset < 0) {
                offset = 0;
            }

            // checks if user is admin

            // checks if request is within limits
            if (limit >= 50) {
                // checks if too high of a value
                return new ResponseEntity<ErrorDTO>(new ErrorDTO(LocalDateTime.now().toString(), "The limit is too high!", HttpStatus.BAD_REQUEST.value(), "BAD_REQUEST"), HttpStatus.BAD_REQUEST);
            } else if (offset > userService.getAllUsers().size() - 1) {
                // checks if too high of a value
                return new ResponseEntity<ErrorDTO>(new ErrorDTO(LocalDateTime.now().toString(), "The offset is too high!", HttpStatus.BAD_REQUEST.value(), "BAD_REQUEST"), HttpStatus.BAD_REQUEST);
            } else {
                if (userService.getAllUsers().size() == 0) {
                    // checks if there are no users
                    return new ResponseEntity<ErrorDTO>(new ErrorDTO(LocalDateTime.now().toString(), "There are no users!", HttpStatus.NOT_FOUND.value(), "NOT_FOUND"), HttpStatus.NOT_FOUND);
                } else {
                    // get all users
                    List<User> users = (List<User>) userService.getAllUsers();
                    List<GetUserDTO> getUserDTOs = new ArrayList<>();
                    // turns all users in userdtos
                    for (User user : users) {
                        GetUserDTO getUserDTO = user.getGetUserDTO();
                        getUserDTOs.add(getUserDTO);
                    }
                    return new ResponseEntity<List<GetUserDTO>>(getUserDTOs, HttpStatus.OK);
                }
            }
        } catch (Exception e) { // no IOException because object mapper does not work
            log.error("Internal server error", e);
            return new ResponseEntity<ErrorDTO>(new ErrorDTO(LocalDateTime.now().toString(), "An internal server error had occured!", 500, "INTERNAL_SERVER_ERROR"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<? extends Object> getUser(/*@DecimalMin("1")*/@Parameter(in = ParameterIn.PATH, description = "", required=true, schema=@Schema()) @PathVariable("id") UUID id) {
        try {
            // checks if id is in uuid format
            if (id == null || id.toString().length() != 36) {
                return new ResponseEntity<ErrorDTO>(new ErrorDTO(LocalDateTime.now().toString(), "Bad request: id is wrong", HttpStatus.BAD_REQUEST.value(), "BAD_REQUEST"), HttpStatus.BAD_REQUEST);
            }
            else {
                // checks if user exists
                if (userService.getUser(id) == null) {
                    return new ResponseEntity<ErrorDTO>(new ErrorDTO(LocalDateTime.now().toString(), "Not found", HttpStatus.NOT_FOUND.value(), "NOT_FOUND"), HttpStatus.NOT_FOUND);
                }
                else {
                    User user = userService.getUser(id);
                    GetUserDTO getUserDTO = user.getGetUserDTO();
                    return new ResponseEntity<GetUserDTO>(getUserDTO, HttpStatus.OK);
                }
            }

        } catch (Exception e) { // no IOException because object mapper does not work
            log.error("Internal server error", e);
            return new ResponseEntity<ErrorDTO>(new ErrorDTO(LocalDateTime.now().toString(), "An internal server error had occured!", 500, "INTERNAL_SERVER_ERROR"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<? extends Object> updateUser(/*@DecimalMin("1")*/@Parameter(in = ParameterIn.PATH, description = "", required=true, schema=@Schema()) @PathVariable("id") UUID id, @Parameter(in = ParameterIn.DEFAULT, description = "", schema=@Schema()) @Valid @RequestBody UserDTO userDTO) {
        try {
            // checks if userdto is null
            if (userDTO == null) {
                return new ResponseEntity<ErrorDTO>(new ErrorDTO(LocalDateTime.now().toString(), "Bad request: user is null", HttpStatus.BAD_REQUEST.value(), "BAD_REQUEST"), HttpStatus.BAD_REQUEST);
            }
            // checks if every value is set
            else if (userDTO.getUserid() == null || userDTO.getUsername() == null || userDTO.getPassword() == null || userDTO.getEmail() == null || userDTO.getFirstName() == null || userDTO.getLastName() == null || userDTO.getStreet() == null || userDTO.getCity() == null || userDTO.getZipcode() == null || userDTO.getUserstatus() == null || userDTO.getDayLimit() == null || userDTO.getTransactionLimit() == null || userDTO.getRoles() == null) {
                return new ResponseEntity<ErrorDTO>(new ErrorDTO(LocalDateTime.now().toString(), "Bad request: user is incomplete", HttpStatus.BAD_REQUEST.value(), "BAD_REQUEST"), HttpStatus.BAD_REQUEST);
            }
            else {
                // checks if user exists
                if (userService.getUser(id) == null) {
                    return new ResponseEntity<ErrorDTO>(new ErrorDTO(LocalDateTime.now().toString(), "Not found: user not found", HttpStatus.NOT_FOUND.value(), "NOT_FOUND"), HttpStatus.NOT_FOUND);
                }
                else {
                    User user = new User();
                    User user2 = user.getUserModel(userDTO);
                    UserDTO userDTO1 = userService.updateUser(user2).getUserDTO();
                    return new ResponseEntity<UserDTO>(userDTO1, HttpStatus.OK);
                }
            }
        } catch (Exception e) { // no IOException because object mapper does not work
            log.error("Internal server error", e);
            return new ResponseEntity<ErrorDTO>(new ErrorDTO(LocalDateTime.now().toString(), "An internal server error had occured!", 500, "INTERNAL_SERVER_ERROR"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
