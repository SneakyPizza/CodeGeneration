package io.swagger.model.entities;

import javax.persistence.*;

import io.swagger.model.dto.GetUserDTO;
import io.swagger.model.dto.PostAsUserDTO;
import io.swagger.model.dto.PostUserDTO;
import io.swagger.model.dto.NameSearchAccountDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.annotations.Type;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class Users {
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

    @OneToMany()
    @JoinColumn(name = "USERS_ID")
    private List<Account> accounts;

    @ElementCollection(fetch = FetchType.EAGER)
    @NonNull
    private List<Role> roles;

    // all args constructor
    public Users(String username, String password, String email, String firstName, String lastName, String street, String city, String zipcode, UserStatus userstatus, BigDecimal dayLimit, BigDecimal transactionLimit, String pincode, List<Account> accounts, List<Role> roles) {
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
    
    public PostUserDTO getPostUserDTO() {
    	PostUserDTO postUserDTO = new PostUserDTO();
    	postUserDTO.setUsername(this.username);
    	postUserDTO.setPassword(this.password);
    	postUserDTO.setEmail(this.email);
    	postUserDTO.setFirstName(this.firstName);
    	postUserDTO.setLastName(this.lastName);
    	postUserDTO.setStreet(this.street);
    	postUserDTO.setCity(this.city);
    	postUserDTO.setZipcode(this.zipcode);
    	postUserDTO.setDayLimit(this.dayLimit);
    	postUserDTO.setTransactionLimit(this.transactionLimit);

        if (this.userstatus == UserStatus.ACTIVE) {
            postUserDTO.setUserstatus(PostUserDTO.UserstatusEnum.ACTIVE);
        }
        else if (this.userstatus == UserStatus.DISABLED) {
            postUserDTO.setUserstatus(PostUserDTO.UserstatusEnum.DISABLED);
        }

        if (this.roles.contains(Role.ROLE_ADMIN) && this.roles.contains(Role.ROLE_USER)) {
            postUserDTO.setRoles(List.of(PostUserDTO.Role.ADMIN, PostUserDTO.Role.USER));
        } else if(this.roles.contains(Role.ROLE_ADMIN)) {
            postUserDTO.setRoles(List.of(PostUserDTO.Role.ADMIN));
        } else if(this.roles.contains(Role.ROLE_USER)) {
            postUserDTO.setRoles(List.of(PostUserDTO.Role.USER));
        } else {
            postUserDTO.setRoles(Collections.emptyList());
        }
    	return postUserDTO;
    }

    public PostAsUserDTO getPostAsUserDTO() {
        PostAsUserDTO postAsUserDTO = new PostAsUserDTO();
        postAsUserDTO.setUsername(this.username);
        postAsUserDTO.setPassword(this.password);
        postAsUserDTO.setEmail(this.email);
        postAsUserDTO.setFirstName(this.firstName);
        postAsUserDTO.setLastName(this.lastName);
        postAsUserDTO.setStreet(this.street);
        postAsUserDTO.setCity(this.city);
        postAsUserDTO.setZipcode(this.zipcode);
        postAsUserDTO.setDayLimit(this.dayLimit);
        postAsUserDTO.setTransactionLimit(this.transactionLimit);
        return postAsUserDTO;
    }

    public GetUserDTO getGetUserDTO() {
        GetUserDTO getUserDTO = new GetUserDTO();
        getUserDTO.setUserid(this.id);
        getUserDTO.setUsername(this.username);
        getUserDTO.setPassword(this.password);
        getUserDTO.setEmail(this.email);
        getUserDTO.setFirstName(this.firstName);
        getUserDTO.setLastName(this.lastName);
        getUserDTO.setStreet(this.street);
        getUserDTO.setCity(this.city);
        getUserDTO.setZipcode(this.zipcode);

        List<String> list = new ArrayList<>();
        for (Account account : this.accounts) {
            list.add(account.getIBAN());
        }
        getUserDTO.setAccounts(list);



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

    public Users setPropertiesFromPostUserDTO(PostUserDTO postUserDTO) {
        Users users = new Users();
        users.setUsername(postUserDTO.getUsername());
        users.setPassword(postUserDTO.getPassword());
        users.setEmail(postUserDTO.getEmail());
        users.setFirstName(postUserDTO.getFirstName());
        users.setLastName(postUserDTO.getLastName());
        users.setStreet(postUserDTO.getStreet());
        users.setCity(postUserDTO.getCity());
        users.setZipcode(postUserDTO.getZipcode());
        users.setDayLimit(postUserDTO.getDayLimit());
        users.setTransactionLimit(postUserDTO.getTransactionLimit());
        if (postUserDTO.getUserstatus() == PostUserDTO.UserstatusEnum.ACTIVE) {
            users.setUserstatus(UserStatus.ACTIVE);
        }
        else if (postUserDTO.getUserstatus() == PostUserDTO.UserstatusEnum.DISABLED) {
            users.setUserstatus(UserStatus.DISABLED);
        }

        if (postUserDTO.getRoles().contains(PostUserDTO.Role.ADMIN) && postUserDTO.getRoles().contains(PostUserDTO.Role.USER)) {
            users.setRoles(List.of(Role.ROLE_ADMIN, Role.ROLE_USER));
        } else if(postUserDTO.getRoles().contains(PostUserDTO.Role.ADMIN)) {
            users.setRoles(List.of(Role.ROLE_ADMIN));
        } else if(postUserDTO.getRoles().contains(PostUserDTO.Role.USER)) {
            users.setRoles(List.of(Role.ROLE_USER));
        } else {
            users.setRoles(Collections.emptyList());
        }
        return users;
    }

    public Users setPropertiesFromPostAsUserDTO(PostAsUserDTO postAsUserDTO) {
        Users users = new Users();
        users.setUsername(postAsUserDTO.getUsername());
        users.setPassword(postAsUserDTO.getPassword());
        users.setEmail(postAsUserDTO.getEmail());
        users.setFirstName(postAsUserDTO.getFirstName());
        users.setLastName(postAsUserDTO.getLastName());
        users.setStreet(postAsUserDTO.getStreet());
        users.setCity(postAsUserDTO.getCity());
        users.setZipcode(postAsUserDTO.getZipcode());
        users.setDayLimit(postAsUserDTO.getDayLimit());
        users.setTransactionLimit(postAsUserDTO.getTransactionLimit());
        return users;
    }

    public Users setPropertiesFromGetUserDTO(GetUserDTO getUserDTO) {
        Users users = new Users();
        users.setId(getUserDTO.getUserid());
        users.setUsername(getUserDTO.getUsername());
        users.setPassword(getUserDTO.getPassword());
        users.setEmail(getUserDTO.getEmail());
        users.setFirstName(getUserDTO.getFirstName());
        users.setLastName(getUserDTO.getLastName());
        users.setStreet(getUserDTO.getStreet());
        users.setCity(getUserDTO.getCity());
        users.setZipcode(getUserDTO.getZipcode());
        users.setDayLimit(getUserDTO.getDayLimit());
        users.setTransactionLimit(getUserDTO.getTransactionLimit());
        if (getUserDTO.getUserstatus() == GetUserDTO.UserstatusEnum.ACTIVE) {
            users.setUserstatus(UserStatus.ACTIVE);
        } else if (getUserDTO.getUserstatus() == GetUserDTO.UserstatusEnum.DISABLED) {
            users.setUserstatus(UserStatus.DISABLED);
        }
        if (getUserDTO.getRoles().contains(GetUserDTO.Role.ADMIN) && getUserDTO.getRoles().contains(GetUserDTO.Role.USER)) {
            users.setRoles(List.of(Role.ROLE_ADMIN, Role.ROLE_USER));
        } else if (getUserDTO.getRoles().contains(GetUserDTO.Role.ADMIN)) {
            users.setRoles(List.of(Role.ROLE_ADMIN));
        } else if (getUserDTO.getRoles().contains(GetUserDTO.Role.USER)) {
            users.setRoles(List.of(Role.ROLE_USER));
        } else {
            users.setRoles(Collections.emptyList());
        }
        return users;
    }

    public NameSearchAccountDTO toNameSearchAccountDTO(String iban){
        NameSearchAccountDTO dto = new NameSearchAccountDTO();
        dto.setFirstName(this.getFirstName());
        dto.setLastName(this.getLastName());
        dto.setIBAN(iban);
        return dto;
    }
}
