package io.swagger.model.entities;

import javax.persistence.*;

import io.swagger.model.UserDTO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
@Data
public class User<list> {
    @Id
    @GeneratedValue
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

    @OneToMany(cascade=CascadeType.ALL)
    private List<Account> accounts;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<Role> roles;

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
    	userDTO.setRoles(this.roles);
    	return userDTO;
    }
}
