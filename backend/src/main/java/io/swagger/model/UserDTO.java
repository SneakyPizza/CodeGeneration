package io.swagger.model.dto;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.UUID;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * UserDTO
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-05-04T11:04:07.506Z[GMT]")


public class UserDTO   {
  @JsonProperty("userid")
  private UUID userid = null;

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
   * Gets or Sets role
   */
  public enum RoleEnum {
    ADMIN("admin"),
    
    USER("user");

    private String value;

    RoleEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static RoleEnum fromValue(String text) {
      for (RoleEnum b : RoleEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }
  @JsonProperty("role")
  private RoleEnum role = null;

  public UserDTO userid(UUID userid) {
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

  public UserDTO password(String password) {
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

  public UserDTO email(String email) {
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

  public UserDTO firstName(String firstName) {
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

  public UserDTO lastName(String lastName) {
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

  public UserDTO street(String street) {
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

  public UserDTO city(String city) {
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

  public UserDTO zipcode(String zipcode) {
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

  public UserDTO userstatus(UserstatusEnum userstatus) {
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

  public UserDTO dayLimit(BigDecimal dayLimit) {
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

  public UserDTO transactionLimit(BigDecimal transactionLimit) {
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

  public UserDTO role(RoleEnum role) {
    this.role = role;
    return this;
  }

  /**
   * Get role
   * @return role
   **/
  @Schema(description = "")
  
    public RoleEnum getRole() {
    return role;
  }

  public void setRole(RoleEnum role) {
    this.role = role;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserDTO userDTO = (UserDTO) o;
    return Objects.equals(this.userid, userDTO.userid) &&
        Objects.equals(this.password, userDTO.password) &&
        Objects.equals(this.email, userDTO.email) &&
        Objects.equals(this.firstName, userDTO.firstName) &&
        Objects.equals(this.lastName, userDTO.lastName) &&
        Objects.equals(this.street, userDTO.street) &&
        Objects.equals(this.city, userDTO.city) &&
        Objects.equals(this.zipcode, userDTO.zipcode) &&
        Objects.equals(this.userstatus, userDTO.userstatus) &&
        Objects.equals(this.dayLimit, userDTO.dayLimit) &&
        Objects.equals(this.transactionLimit, userDTO.transactionLimit) &&
        Objects.equals(this.role, userDTO.role);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userid, password, email, firstName, lastName, street, city, zipcode, userstatus, dayLimit, transactionLimit, role);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserDTO {\n");
    
    sb.append("    userid: ").append(toIndentedString(userid)).append("\n");
    sb.append("    password: ").append(toIndentedString(password)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    firstName: ").append(toIndentedString(firstName)).append("\n");
    sb.append("    lastName: ").append(toIndentedString(lastName)).append("\n");
    sb.append("    street: ").append(toIndentedString(street)).append("\n");
    sb.append("    city: ").append(toIndentedString(city)).append("\n");
    sb.append("    zipcode: ").append(toIndentedString(zipcode)).append("\n");
    sb.append("    userstatus: ").append(toIndentedString(userstatus)).append("\n");
    sb.append("    dayLimit: ").append(toIndentedString(dayLimit)).append("\n");
    sb.append("    transactionLimit: ").append(toIndentedString(transactionLimit)).append("\n");
    sb.append("    role: ").append(toIndentedString(role)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
