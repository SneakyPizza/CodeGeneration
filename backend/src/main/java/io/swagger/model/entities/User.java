package io.swagger.model.entities;

import javax.persistence.*;

import lombok.Data;
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

    @OneToMany(mappedBy = "user")
    private List<Account> accounts;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<Role> roles;

}
