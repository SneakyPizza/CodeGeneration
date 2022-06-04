package io.swagger.model.entities;

import javax.persistence.*;
import javax.xml.bind.DatatypeConverter;

import io.swagger.model.UserDTO;
import io.swagger.model.dto.NameSearchAccountDTO;
import lombok.Data;
import org.hibernate.annotations.Type;

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
    private List<UserDTO.Role> roles;

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

    public NameSearchAccountDTO toNameSearchAccountDTO(String iban){
        NameSearchAccountDTO dto = new NameSearchAccountDTO();
        dto.setFirstName(this.getFirstName());
        dto.setLastName(this.getLastName());
        dto.setIBAN(iban);
        return dto;
      }

    public void setPassword(String password) {
        try {
            // encrypts password in MD5
            MessageDigest md = null;
            md = MessageDigest.getInstance("SHA-512");
            md.update(password.getBytes());
            byte[] digest = md.digest();
            String hash = DatatypeConverter
                    .printHexBinary(digest).toUpperCase();
            this.password = hash;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
