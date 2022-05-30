package io.swagger.model.entities;

import javax.persistence.*;

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
    private BigDecimal dayLimit;;
    private BigDecimal transactionLimit;
    private String Pincode;

    @OneToMany(cascade=CascadeType.ALL)
    private List<Account> accounts;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<Role> roles;

}
