package io.swagger.model.entities;

import javax.persistence.*;
import javax.xml.bind.DatatypeConverter;

import io.swagger.model.GetUserDTO;
import io.swagger.model.UserDTO;
import io.swagger.model.dto.NameSearchAccountDTO;
import lombok.Data;
import org.hibernate.annotations.Type;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Entity
@Data
public class User<list> {
    @Id
    @GeneratedValue
    @Type(type="uuid-char")
    private UUID id;

    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String street;
    private String city;
    private String zipcode;
    private BigDecimal dayLimit;
    private BigDecimal transactionLimit;
    private String Pincode;

    @OneToMany(cascade=CascadeType.PERSIST)
    private List<Account> accounts;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<Role> roles;

    public UserDTO toUserDTO(){
        UserDTO dto = new UserDTO();
        //missing username
        dto.setEmail(email);
        dto.setPassword(password);
        dto.setFirstName(firstName);
        dto.setLastName(lastName);
        dto.setStreet(street);
        dto.setCity(city);
        dto.setZipcode(zipcode);
        dto.setDayLimit(dayLimit);
        dto.setTransactionLimit(transactionLimit);
        return dto;
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
        //user.setRoles(userDTO.getRoles());
        return user;
    }

    public NameSearchAccountDTO toNameSearchAccountDTO(String iban){
        NameSearchAccountDTO dto = new NameSearchAccountDTO();
        dto.setFirstName(this.getFirstName());
        dto.setLastName(this.getLastName());
        dto.setIBAN(iban);
        return dto;
      }
}
