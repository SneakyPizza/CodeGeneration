package io.swagger.model.entities;

import javax.persistence.*;
import javax.xml.bind.DatatypeConverter;

import io.swagger.model.GetUserDTO;
import io.swagger.model.UserDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.annotations.Type;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue
    @Type(type="uuid-char")
    @NonNull
    private UUID id;
    @NonNull
    private String username;
    @NonNull
    private String password;
    @NonNull
    private String email;
    @NonNull
    private String firstName;
    @NonNull
    private String lastName;
    @NonNull
    private String street;
    @NonNull
    private String city;
    @NonNull
    private String zipcode;
    @NonNull
    private UserStatus userstatus;
    @NonNull
    private BigDecimal dayLimit;
    @NonNull
    private BigDecimal transactionLimit;
    @NonNull
    private String pincode;

    @OneToMany(cascade=CascadeType.PERSIST)
    private List<Account> accounts;

    @ElementCollection(fetch = FetchType.EAGER)
    @NonNull
    private List<Role> roles;

    // all args constructor
    public User(String username, String password, String email, String firstName, String lastName, String street, String city, String zipcode, UserStatus userstatus, BigDecimal dayLimit, BigDecimal transactionLimit, String pincode, List<Account> accounts, List<Role> roles) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.street = street;
        this.city = city;
        this.zipcode = zipcode;
        this.userstatus = userstatus;
        this.dayLimit = dayLimit;
        this.transactionLimit = transactionLimit;
        this.pincode = pincode;
        this.accounts = accounts;
        this.roles = roles;
    }

    public void setDayLimit(BigDecimal dayLimit) {
        // check if dayLimit is positive
        if (dayLimit.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Day limit must be positive"); // throws exception if dayLimit is negative
        }
        else {
            this.dayLimit = dayLimit;
        }
    }

    public void setTransactionLimit(BigDecimal transactionLimit) {
        // check if transactionLimit is positive
        if (transactionLimit.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Transaction limit must be positive"); // throws exception if transactionLimit is negative
        }
        else {
            this.transactionLimit = transactionLimit;
        }
    }

    public UserDTO getUserDTO() {
    	UserDTO userDTO = new UserDTO();
    	userDTO.setUserid(this.id);
    	userDTO.setUsername(this.username);
    	userDTO.setPassword(this.password);
    	userDTO.setEmail(this.email);
    	userDTO.setFirstName(this.firstName);
    	userDTO.setLastName(this.lastName);
    	userDTO.setStreet(this.street);
    	userDTO.setCity(this.city);
    	userDTO.setZipcode(this.zipcode);
        if (this.userstatus == UserStatus.ACTIVE) {
            userDTO.setUserstatus(UserDTO.UserstatusEnum.ACTIVE);
        }
        else if (this.userstatus == UserStatus.DISABLED) {
            userDTO.setUserstatus(UserDTO.UserstatusEnum.DISABLED);
        }
    	userDTO.setDayLimit(this.dayLimit);
    	userDTO.setTransactionLimit(this.transactionLimit);
        if (this.roles.contains(Role.ROLE_ADMIN) && this.roles.contains(Role.ROLE_USER)) {
            userDTO.setRoles(List.of(UserDTO.Role.ADMIN, UserDTO.Role.USER));
        } else if(this.roles.contains(Role.ROLE_ADMIN)) {
            userDTO.setRoles(List.of(UserDTO.Role.ADMIN));
        } else if(this.roles.contains(Role.ROLE_USER)) {
            userDTO.setRoles(List.of(UserDTO.Role.USER));
        } else {
            userDTO.setRoles(Collections.emptyList());
        }
    	//userDTO.setRoles(Collections.singletonList(UserDTO.Role.fromValue(this.roles.toString())));
    	return userDTO;
    }

    public GetUserDTO getGetUserDTO() {
        GetUserDTO getUserDTO = new GetUserDTO();
        getUserDTO.setUserid(this.id);
        getUserDTO.setUsername(this.username);
        getUserDTO.setEmail(this.email);
        getUserDTO.setFirstName(this.firstName);
        getUserDTO.setLastName(this.lastName);
        getUserDTO.setStreet(this.street);
        getUserDTO.setCity(this.city);
        getUserDTO.setZipcode(this.zipcode);
        if (this.userstatus == UserStatus.ACTIVE) {
            getUserDTO.setUserstatus(GetUserDTO.UserstatusEnum.ACTIVE);
        }
        else if (this.userstatus == UserStatus.DISABLED) {
            getUserDTO.setUserstatus(GetUserDTO.UserstatusEnum.DISABLED);
        }
        getUserDTO.setDayLimit(this.dayLimit);
        getUserDTO.setTransactionLimit(this.transactionLimit);
        if (this.roles.contains(Role.ROLE_ADMIN) && this.roles.contains(Role.ROLE_USER)) {
            getUserDTO.setRoles(List.of(GetUserDTO.Role.ADMIN, GetUserDTO.Role.USER));
        } else if(this.roles.contains(Role.ROLE_ADMIN)) {
            getUserDTO.setRoles(List.of(GetUserDTO.Role.ADMIN));
        } else if(this.roles.contains(Role.ROLE_USER)) {
            getUserDTO.setRoles(List.of(GetUserDTO.Role.USER));
        } else {
            getUserDTO.setRoles(Collections.emptyList());
        }
        return getUserDTO;
    }

    public User getUserModel(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getUserid());
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        user.setEmail(userDTO.getEmail());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setStreet(userDTO.getStreet());
        user.setCity(userDTO.getCity());
        user.setZipcode(userDTO.getZipcode());
        if (userDTO.getUserstatus() == UserDTO.UserstatusEnum.ACTIVE) {
            user.setUserstatus(UserStatus.ACTIVE);
        }
        else if (userDTO.getUserstatus() == UserDTO.UserstatusEnum.DISABLED) {
            user.setUserstatus(UserStatus.DISABLED);
        }
        user.setDayLimit(userDTO.getDayLimit());
        user.setTransactionLimit(userDTO.getTransactionLimit());
        if (userDTO.getRoles().contains(GetUserDTO.Role.ADMIN) && userDTO.getRoles().contains(GetUserDTO.Role.USER)) {
            user.setRoles(List.of(Role.ROLE_ADMIN, Role.ROLE_USER));
        } else if(userDTO.getRoles().contains(GetUserDTO.Role.ADMIN)) {
            user.setRoles(List.of(Role.ROLE_ADMIN));
        } else if(userDTO.getRoles().contains(GetUserDTO.Role.USER)) {
            user.setRoles(List.of(Role.ROLE_USER));
        } else {
            user.setRoles(Collections.emptyList());
        }
        return user;
    }
}
