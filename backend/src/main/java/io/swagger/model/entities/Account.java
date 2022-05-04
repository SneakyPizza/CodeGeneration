package io.swagger.model.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Entity
@Data
public class Account {

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

  private UUID userid = null;

  private String IBAN = null;

  private BigDecimal balance = null;

  private ActiveEnum active;

  private BigDecimal absoluteLimit;

//  private UserDTO user;

}
