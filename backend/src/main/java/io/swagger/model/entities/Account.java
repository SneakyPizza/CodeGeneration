package io.swagger.model.entities;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
@Data
public class Account<Users> {

  public enum ActiveEnum {
    ACTIVE("active"),

    DISABLED("disabled");

    private String value;

    ActiveEnum(String value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

  }

  public enum AccountTypeEnum {
    SAVINGS("savings"),
    
    CURRENT("current");

    private String value;

    AccountTypeEnum(String value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

  }

  @Id
  @GeneratedValue
  private UUID id;

  private AccountTypeEnum accountType;

  private UUID userid;

  private String IBAN;

  private BigDecimal balance;

  private ActiveEnum active;

  private BigDecimal absoluteLimit;

//  private Users user;

//  private UserDTO user;

}
