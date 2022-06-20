package io.swagger.api;

import io.swagger.annotations.Api;
import io.swagger.model.dto.GetUserDTO;
import io.swagger.model.dto.ErrorDTO;
import io.swagger.model.dto.PostAsUserDTO;
import io.swagger.model.dto.PostUserDTO;
import io.swagger.model.entities.User;
import io.swagger.jwt.JwtTokenProvider;


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
import org.springframework.security.access.prepost.PreAuthorize;
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

    public ResponseEntity<? extends Object> addUser(@Parameter(in = ParameterIn.DEFAULT, description = "", schema=@Schema()) @Valid @RequestBody PostAsUserDTO postAsUserDTO) {
        User user = userService.createUser(postAsUserDTO);
        return new ResponseEntity<>(user.getPostAsUserDTO(), HttpStatus.CREATED);
    }

    // checks if endpoint is called by an admin
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<? extends Object> addUserAdmin(@Parameter(in = ParameterIn.DEFAULT, description = "", schema=@Schema()) @Valid @RequestBody PostUserDTO postUserDTO) {
        User user = userService.createUserAdmin(postUserDTO);
        return new ResponseEntity<>(user.getPostUserDTO(), HttpStatus.CREATED);
    }

    // checks if endpoint is called by an admin
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<? extends Object> getAllUsers(@Min(0)@Parameter(in = ParameterIn.QUERY, description = "The number of items to skip before starting to collect the result set." ,schema=@Schema(allowableValues={  }
)) @Valid @RequestParam(value = "offset", required = false) Integer offset, @Min(1) @Max(50) @Parameter(in = ParameterIn.QUERY, description = "The numbers of items to return." ,schema=@Schema(allowableValues={  }, minimum="1", maximum="50"
, defaultValue="20")) @Valid @RequestParam(value = "limit", required = false, defaultValue="20") Integer limit) {
        return new ResponseEntity<>(userService.getAllUsers(offset, limit), HttpStatus.OK);
    }

    public ResponseEntity<? extends Object> getUser(/*@DecimalMin("1")*/@Parameter(in = ParameterIn.PATH, description = "", required=true, schema=@Schema()) @PathVariable("id") UUID id) {
        User user = userService.getUser(id);
        return new ResponseEntity<>(user.getGetUserDTO(), HttpStatus.OK);
    }

    public ResponseEntity<? extends Object> updateUser(/*@DecimalMin("1")*/@Parameter(in = ParameterIn.PATH, description = "", required=true, schema=@Schema()) @PathVariable("id") UUID id, @Parameter(in = ParameterIn.DEFAULT, description = "", schema=@Schema()) @Valid @RequestBody PostUserDTO postUserDTO) {
        User user = userService.updateUser(postUserDTO);
        return new ResponseEntity<>(user.getPostUserDTO(), HttpStatus.OK);
    }
}
