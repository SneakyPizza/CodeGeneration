package io.swagger.api;

import io.swagger.annotations.Api;
import io.swagger.model.dto.ErrorDTO;
import io.swagger.model.UserDTO;
import io.swagger.model.dto.GetTransactionDTO;
import io.swagger.model.dto.JWT_DTO;
import io.swagger.model.entities.User;
import io.swagger.jwt.JwtTokenProvider;


import java.util.ArrayList;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

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

    public ResponseEntity<List<UserDTO>> addUser(@Parameter(in = ParameterIn.DEFAULT, description = "", schema=@Schema()) @Valid @RequestBody UserDTO userDTO) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                if (userDTO == null) {
                    // checks if null
                    log.error("Not implemented");
                    return new ResponseEntity<List<UserDTO>>(HttpStatus.NOT_IMPLEMENTED);
                }
                else if (userDTO.getUserid() == null || userDTO.getUsername() == null || userDTO.getPassword() == null || userDTO.getEmail() == null || userDTO.getFirstName() == null || userDTO.getLastName() == null || userDTO.getStreet() == null || userDTO.getCity() == null || userDTO.getZipcode() == null || userDTO.getUserstatus() == null || userDTO.getDayLimit() == null || userDTO.getTransactionLimit() == null || userDTO.getRoles() == null) {
                    // checks if null
                    log.error("Not implemented");
                    return new ResponseEntity<List<UserDTO>>(HttpStatus.NOT_IMPLEMENTED);
                }
                else {
                    // create new user
                    User user = new User();
                    user.getUserModel(userDTO);

                    // DOES NOT WORK PROPERLY
                    JWT_DTO jwt_dto = userService.createUser(user);
                    String username = jwtTokenProvider.getUsername(jwt_dto.getJwTtoken());
                    List<UserDTO> userDTOs = new ArrayList<>(1);
                    userDTOs.add(userService.findByUsername(username).getUserDTO());
                    return new ResponseEntity<List<UserDTO>>(userDTOs, HttpStatus.OK);
                }
            } catch (Exception e) { // no IOException because object mapper does not work
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<List<UserDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<List<UserDTO>>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<List<UserDTO>> getAllUsers(@Min(0)@Parameter(in = ParameterIn.QUERY, description = "The number of items to skip before starting to collect the result set." ,schema=@Schema(allowableValues={  }
)) @Valid @RequestParam(value = "offset", required = false) Integer offset,@Min(1) @Max(50) @Parameter(in = ParameterIn.QUERY, description = "The numbers of items to return." ,schema=@Schema(allowableValues={  }, minimum="1", maximum="50"
, defaultValue="20")) @Valid @RequestParam(value = "limit", required = false, defaultValue="20") Integer limit) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                // if unset or too low set to default value
                if (limit == null || offset == null) {
                    limit = 20;
                }

                // if unset or too low set to default value
                if (offset == null || offset < 0) {
                    offset = 0;
                }

                // checks if request is within limits
                if (limit >= 50) {
                    // checks if too high of a value
                    log.error("Payload Too Large");
                    return new ResponseEntity<List<UserDTO>>(HttpStatus.PAYLOAD_TOO_LARGE);
                } else if (offset > 2000000000) {
                    // checks if too high of a value
                    log.error("Payload Too Large");
                    return new ResponseEntity<List<UserDTO>>(HttpStatus.PAYLOAD_TOO_LARGE);
                } else {
                    // get all users
                    List<User> users = (List<User>) userService.getAllUsers(offset, limit);
                    List<UserDTO> userDTOs = new ArrayList<>();
                    // turns all users in userdtos
                    for (User user : users) {
                        UserDTO userDTO = user.getUserDTO();
                        userDTOs.add(userDTO);
                    }
                    return new ResponseEntity<List<UserDTO>>(userDTOs, HttpStatus.OK);
                }
            } catch (Exception e) { // no IOException because object mapper does not work
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<List<UserDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<List<UserDTO>>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<UserDTO> getUser(@DecimalMin("1")@Parameter(in = ParameterIn.PATH, description = "", required=true, schema=@Schema()) @PathVariable("id") UUID id) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                if (id == null) {
                    // checks if null
                    log.error("Not implemented");
                    return new ResponseEntity<UserDTO>(HttpStatus.NOT_IMPLEMENTED);
                } else if (UUID.fromString(id)) { // doesnt work

                } else {
                    // gets user and converts to userdto
                    User user = userService.getUser(id);
                    UserDTO userDTO = user.getUserDTO();
                    return new ResponseEntity<UserDTO>(userDTO, HttpStatus.OK);
                }
            } catch (Exception e) { // no IOException because object mapper does not work
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<UserDTO>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<UserDTO>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<UserDTO> updateUser(@DecimalMin("1")@Parameter(in = ParameterIn.PATH, description = "", required=true, schema=@Schema()) @PathVariable("id") UUID id,@Parameter(in = ParameterIn.DEFAULT, description = "", schema=@Schema()) @Valid @RequestBody UserDTO userDTO) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                if (userDTO == null) {
                    log.error("Not implemented");
                    return new ResponseEntity<UserDTO>(HttpStatus.NOT_IMPLEMENTED);
                }
                else {
                    User user = new User();
                    user.getUserModel(userDTO);
                    user = userService.updateUser(user);
                    UserDTO userDTO2 = user.getUserDTO();
                    return new ResponseEntity<UserDTO>(userDTO2, HttpStatus.OK);
                }
            } catch (Exception e) { // no IOException because object mapper does not work
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<UserDTO>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<UserDTO>(HttpStatus.NOT_IMPLEMENTED);
    }

}
