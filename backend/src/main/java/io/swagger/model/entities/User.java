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

}
