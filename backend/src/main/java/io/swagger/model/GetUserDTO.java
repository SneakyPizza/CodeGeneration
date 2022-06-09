package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * UserDTO
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-06-09T15:58:08.190Z[GMT]")


public class GetUserDTO   {
  @JsonProperty("userid")
  private UUID userid = null;

  @JsonProperty("username")
  private String username = null;

  @JsonProperty("password")
  private String password = null;

  @JsonProperty("email")
  private String email = null;

  @JsonProperty("firstName")
  private String firstName = null;

  @JsonProperty("lastName")
  private String lastName = null;

  @JsonProperty("street")
  private String street = null;

  @JsonProperty("city")
  private String city = null;

  @JsonProperty("zipcode")
  private String zipcode = null;

  @JsonProperty("Accounts")
  @Valid
  private List<String> accounts = null;

  /**
   * Gets or Sets userstatus
   */
  public enum UserstatusEnum {
    ACTIVE("active"),

    DISABLED("disabled");

    private String value;

    UserstatusEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static UserstatusEnum fromValue(String text) {
      for (UserstatusEnum b : UserstatusEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }
  @JsonProperty("userstatus")
  private UserstatusEnum userstatus = null;

  @JsonProperty("dayLimit")
  private BigDecimal dayLimit = null;

  @JsonProperty("transactionLimit")
  private BigDecimal transactionLimit = null;

  /**
   * Gets or Sets roles
   */
  public enum Role {
    ADMIN("admin"),

    USER("user");

    private String value;

    Role(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static Role fromValue(String text) {
      for (Role b : Role.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }
  @JsonProperty("roles")
  @Valid
  private List<Role> roles = null;

  public GetUserDTO userid(UUID userid) {
    this.userid = userid;
    return this;
  }

  /**
   * Get userid
   * @return userid
   **/
  @Schema(description = "")

  @Valid
  public UUID getUserid() {
    return userid;
  }

  public void setUserid(UUID userid) {
    this.userid = userid;
  }

  public GetUserDTO username(String username) {
    this.username = username;
    return this;
  }

  /**
   * Get username
   * @return username
   **/
  @Schema(description = "")

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public GetUserDTO password(String password) {
    this.password = password;
    return this;
  }

  /**
   * Get password
   * @return password
   **/
  @Schema(description = "")

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public GetUserDTO email(String email) {
    this.email = email;
    return this;
  }

  /**
   * Get email
   * @return email
   **/
  @Schema(description = "")

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public GetUserDTO firstName(String firstName) {
    this.firstName = firstName;
    return this;
  }

  /**
   * Get firstName
   * @return firstName
   **/
  @Schema(description = "")

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public GetUserDTO lastName(String lastName) {
    this.lastName = lastName;
    return this;
  }

  /**
   * Get lastName
   * @return lastName
   **/
  @Schema(description = "")

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public GetUserDTO street(String street) {
    this.street = street;
    return this;
  }

  /**
   * Get street
   * @return street
   **/
  @Schema(description = "")

  public String getStreet() {
    return street;
  }

  public void setStreet(String street) {
    this.street = street;
  }

  public GetUserDTO city(String city) {
    this.city = city;
    return this;
  }

  /**
   * Get city
   * @return city
   **/
  @Schema(description = "")

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public GetUserDTO zipcode(String zipcode) {
    this.zipcode = zipcode;
    return this;
  }

  /**
   * Get zipcode
   * @return zipcode
   **/
  @Schema(description = "")

  public String getZipcode() {
    return zipcode;
  }

  public void setZipcode(String zipcode) {
    this.zipcode = zipcode;
  }

  public GetUserDTO accounts(List<String> accounts) {
    this.accounts = accounts;
    return this;
  }

  public GetUserDTO addAccountsItem(String accountsItem) {
    if (this.accounts == null) {
      this.accounts = new ArrayList<String>();
    }
    this.accounts.add(accountsItem);
    return this;
  }

  /**
   * Get accounts
   * @return accounts
   **/
  @Schema(description = "")

  public List<String> getAccounts() {
    return accounts;
  }

  public void setAccounts(List<String> accounts) {
    this.accounts = accounts;
  }

  public GetUserDTO userstatus(UserstatusEnum userstatus) {
    this.userstatus = userstatus;
    return this;
  }

  /**
   * Get userstatus
   * @return userstatus
   **/
  @Schema(description = "")

  public UserstatusEnum getUserstatus() {
    return userstatus;
  }

  public void setUserstatus(UserstatusEnum userstatus) {
    this.userstatus = userstatus;
  }

  public GetUserDTO dayLimit(BigDecimal dayLimit) {
    this.dayLimit = dayLimit;
    return this;
  }

  /**
   * Get dayLimit
   * @return dayLimit
   **/
  @Schema(description = "")

  @Valid
  public BigDecimal getDayLimit() {
    return dayLimit;
  }

  public void setDayLimit(BigDecimal dayLimit) {
    this.dayLimit = dayLimit;
  }

  public GetUserDTO transactionLimit(BigDecimal transactionLimit) {
    this.transactionLimit = transactionLimit;
    return this;
  }

  /**
   * Get transactionLimit
   * @return transactionLimit
   **/
  @Schema(description = "")

  @Valid
  public BigDecimal getTransactionLimit() {
    return transactionLimit;
  }

  public void setTransactionLimit(BigDecimal transactionLimit) {
    this.transactionLimit = transactionLimit;
  }

  public GetUserDTO roles(List<Role> roles) {
    this.roles = roles;
    return this;
  }

  public GetUserDTO addRolesItem(Role rolesItem) {
    if (this.roles == null) {
      this.roles = new ArrayList<Role>();
    }
    this.roles.add(rolesItem);
    return this;
  }

  /**
   * Get roles
   * @return roles
   **/
  @Schema(description = "")

  public List<Role> getRoles() {
    return roles;
  }

  public void setRoles(List<Role> roles) {
    this.roles = roles;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetUserDTO getUserDTO = (GetUserDTO) o;
    return Objects.equals(this.userid, getUserDTO.userid) &&
            Objects.equals(this.username, getUserDTO.username) &&
            Objects.equals(this.password, getUserDTO.password) &&
            Objects.equals(this.email, getUserDTO.email) &&
            Objects.equals(this.firstName, getUserDTO.firstName) &&
            Objects.equals(this.lastName, getUserDTO.lastName) &&
            Objects.equals(this.street, getUserDTO.street) &&
            Objects.equals(this.city, getUserDTO.city) &&
            Objects.equals(this.zipcode, getUserDTO.zipcode) &&
            Objects.equals(this.accounts, getUserDTO.accounts) &&
            Objects.equals(this.userstatus, getUserDTO.userstatus) &&
            Objects.equals(this.dayLimit, getUserDTO.dayLimit) &&
            Objects.equals(this.transactionLimit, getUserDTO.transactionLimit) &&
            Objects.equals(this.roles, getUserDTO.roles);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userid, username, password, email, firstName, lastName, street, city, zipcode, accounts, userstatus, dayLimit, transactionLimit, roles);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetUserDTO {\n");

    sb.append("    userid: ").append(toIndentedString(userid)).append("\n");
    sb.append("    username: ").append(toIndentedString(username)).append("\n");
    sb.append("    password: ").append(toIndentedString(password)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    firstName: ").append(toIndentedString(firstName)).append("\n");
    sb.append("    lastName: ").append(toIndentedString(lastName)).append("\n");
    sb.append("    street: ").append(toIndentedString(street)).append("\n");
    sb.append("    city: ").append(toIndentedString(city)).append("\n");
    sb.append("    zipcode: ").append(toIndentedString(zipcode)).append("\n");
    sb.append("    accounts: ").append(toIndentedString(accounts)).append("\n");
    sb.append("    userstatus: ").append(toIndentedString(userstatus)).append("\n");
    sb.append("    dayLimit: ").append(toIndentedString(dayLimit)).append("\n");
    sb.append("    transactionLimit: ").append(toIndentedString(transactionLimit)).append("\n");
    sb.append("    roles: ").append(toIndentedString(roles)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
