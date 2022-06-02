package io.swagger.model.entities;

import javax.persistence.*;
import javax.xml.bind.DatatypeConverter;

import io.swagger.model.UserDTO;
import lombok.Data;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
    private String Pincode;

    @OneToMany(cascade=CascadeType.ALL)
    private List<Account> accounts;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<UserDTO.Role> roles;
    
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
        user.setRoles(userDTO.getRoles());
        return user;
    }

    public void setPassword(String password) { // needs better hashing algorithm
        try {
            // encrypts password in MD5
            MessageDigest md = null;
            md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            byte[] digest = md.digest();
            String hash = DatatypeConverter
                    .printHexBinary(digest).toUpperCase();
            this.password = hash;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public void setPincode(String Pincode) {
        try {
            // encrypts password in MD5
            MessageDigest md = null;
            md = MessageDigest.getInstance("MD5");
            md.update(Pincode.getBytes());
            byte[] digest = md.digest();
            String hash = DatatypeConverter
                    .printHexBinary(digest).toUpperCase();
            this.Pincode = hash;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
