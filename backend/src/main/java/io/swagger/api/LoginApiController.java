package io.swagger.api;

import io.swagger.annotations.Api;
import io.swagger.model.dto.ErrorDTO;
import io.swagger.model.dto.JWT_DTO;
import io.swagger.model.dto.LoginDTO;

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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-05-04T11:04:07.506Z[GMT]")
@RestController
@Api(tags = {"Login"}, description = "the login API")
public class LoginApiController implements LoginApi {

    private static final Logger log = LoggerFactory.getLogger(LoginApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @org.springframework.beans.factory.annotation.Autowired
    public LoginApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<? extends Object> login(@Parameter(in = ParameterIn.DEFAULT, description = "", schema=@Schema()) @Valid @RequestBody LoginDTO loginDTO) {
        try {
            if (loginDTO == null) {
                // checks if loginDTO is null
                return new ResponseEntity<ErrorDTO>(new ErrorDTO(LocalDateTime.now().toString(), "Bad request: login credentials were null", HttpStatus.BAD_REQUEST.value(), "BAD_REQUEST"), HttpStatus.BAD_REQUEST);
            } else {
                if (loginDTO.getUsername() == null || loginDTO.getPassword() == null) {
                    // checks if username or password is null
                    return new ResponseEntity<ErrorDTO>(new ErrorDTO(LocalDateTime.now().toString(), "Bad request: login credentials were null", HttpStatus.BAD_REQUEST.value(), "BAD_REQUEST"), HttpStatus.BAD_REQUEST);
                } else {
                    if (userService.findByUsername(loginDTO.getUsername()) == null) {
                        // checks if user exists
                        return new ResponseEntity<ErrorDTO>(new ErrorDTO(LocalDateTime.now().toString(), "Bad request: user does not exist", HttpStatus.BAD_REQUEST.value(), "BAD_REQUEST"), HttpStatus.BAD_REQUEST);
                    } else {
                        if (authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword())).isAuthenticated()) {
                            // logs the user in and returns a JWT
                            JWT_DTO jwt_dto = userService.login(loginDTO.getUsername(), loginDTO.getPassword());
                            return new ResponseEntity<JWT_DTO>(jwt_dto, HttpStatus.OK);
                        } else {
                            // if login fails
                            return new ResponseEntity<ErrorDTO>(new ErrorDTO(LocalDateTime.now().toString(), "Unauthorized: login credentials were invalid", HttpStatus.UNAUTHORIZED.value(), "UNAUTHORIZED"), HttpStatus.UNAUTHORIZED);
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("Internal server error", e);
            return new ResponseEntity<ErrorDTO>(new ErrorDTO(LocalDateTime.now().toString(), "An internal server error had occured!", 500, "INTERNAL_SERVER_ERROR"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
