package io.swagger.api;

import io.swagger.annotations.Api;
import io.swagger.model.dto.ErrorDTO;
import io.swagger.model.UserDTO;
import io.swagger.model.dto.GetTransactionDTO;
import io.swagger.model.entities.User;


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
                    log.error("Not implemented");
                    return new ResponseEntity<List<UserDTO>>(HttpStatus.NOT_IMPLEMENTED);
                }
                else if (userDTO.getUserid() == null || userDTO.getUsername() == null || userDTO.getPassword() == null || userDTO.getEmail() == null || userDTO.getFirstName() == null || userDTO.getLastName() == null || userDTO.getStreet() == null || userDTO.getCity() == null || userDTO.getZipcode() == null || userDTO.getUserstatus() == null || userDTO.getDayLimit() == null || userDTO.getTransactionLimit() == null || userDTO.getRoles() == null) {
                    log.error("Not implemented");
                    return new ResponseEntity<List<UserDTO>>(HttpStatus.NOT_IMPLEMENTED);
                }
                else {
                    User user = new User();
                    user.getUserModel(userDTO);
                    user = userService.createUser(user);
                    return new ResponseEntity<List<UserDTO>>(HttpStatus.OK);
                }
            } catch (Exception e) {
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
                if (offset == null) {
                    log.error("Offset not implemented");
                    return new ResponseEntity<List<UserDTO>>(HttpStatus.NOT_IMPLEMENTED);
                }
                else if (limit == null) {
                    log.error("Limit not implemented");
                    return new ResponseEntity<List<UserDTO>>(HttpStatus.NOT_IMPLEMENTED);
                }
                else {
                    // needs more security checks 
//                    List<User> users = (List<User>) userService.getAllUsers(offset, limit);
//                    List<UserDTO> userDTOs = new ArrayList<>();
//                    for (User user : users) {
//                        UserDTO userDTO = user.getUserDTO();
//                        userDTOs.add(userDTO);
//                    }
                    return new ResponseEntity<List<UserDTO>>(HttpStatus.OK);
//                    return new ResponseEntity<List<UserDTO>>(userDTOs, HttpStatus.OK);
                }
            } catch (Exception e) {
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
                    log.error("Not implemented");
                    return new ResponseEntity<UserDTO>(HttpStatus.NOT_IMPLEMENTED);
                }
                else {
                    User user = userService.getUser(id);
                    UserDTO userDTO = user.getUserDTO();
                    return new ResponseEntity<UserDTO>((UserDTO) userDTO, HttpStatus.OK);
                }
            } catch (Exception e) { // no IOException because object mapper does not work
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<UserDTO>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<UserDTO>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<UserDTO> updateUser(@DecimalMin("1")@Parameter(in = ParameterIn.PATH, description = "", required=true, schema=@Schema()) @PathVariable("id") UUID id,@Parameter(in = ParameterIn.DEFAULT, description = "", schema=@Schema()) @Valid @RequestBody UserDTO body) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<UserDTO>(objectMapper.readValue("{\n  \"userid\" : \"5e9f8f8f-8f8f-8f8f-8f8f-8f8f8f8f8f8f\",\n  \"email\" : \"user@gmail.com\",\n  \"firstName\" : \"John\",\n  \"lastName\" : \"Doe\",\n  \"street\" : \"examplestreet 1a\",\n  \"city\" : \"Amsterdam\",\n  \"zipcode\" : \"1234AB\",\n  \"userstatus\" : \"active\",\n  \"dayLimit\" : \"1000\",\n  \"transactionLimit\" : \"100\",\n  \"role\" : \"user\"\n}", UserDTO.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<UserDTO>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<UserDTO>(HttpStatus.NOT_IMPLEMENTED);
    }

}
