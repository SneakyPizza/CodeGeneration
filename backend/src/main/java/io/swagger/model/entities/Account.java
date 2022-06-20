package io.swagger.model.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;

import io.swagger.model.AccountDTO;
import org.hibernate.annotations.Type;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
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

    public static ActiveEnum fromValue(String text) {
      for (ActiveEnum b : ActiveEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
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

    public static AccountTypeEnum fromValue(String text) {
      for (AccountTypeEnum b : AccountTypeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }

  }

  @Id
  @GeneratedValue
  @Type(type="uuid-char")
  @NonNull
  private UUID id;

  private AccountTypeEnum accountType;

  @ManyToOne
  private io.swagger.model.entities.Users user;

  @NonNull
  private String IBAN;
  @NonNull
  private BigDecimal balance;

  private ActiveEnum active;

  @NonNull
  private BigDecimal absoluteLimit;


  public AccountDTO toAccountDTO(){
    AccountDTO dto = new AccountDTO();
    dto.setAbsoluteLimit(absoluteLimit);
    //String acct = accountType.value;
    AccountDTO.AccountTypeEnum accounttype_value = AccountDTO.AccountTypeEnum.fromValue(accountType.value);
    //= AccountDTO.AccountTypeEnum.valueOf(accountType.toString());
    dto.setAccountType(accounttype_value);
    dto.setUserid(user.getId());
    String acct = active.value;
    AccountDTO.ActiveEnum accountactive_value = AccountDTO.ActiveEnum.fromValue(acct);

    dto.setActive(accountactive_value);
    dto.setBalance(balance);
    dto.setIBAN(IBAN);

    //dto.setUser(user.toUserDTO());
    //Set userId from user

    return dto;
  }

  public void setBalance(BigDecimal balance){
    if(balance.compareTo(BigDecimal.ZERO) < 0){
      throw new IllegalArgumentException("Balance must be postive");
    } else {
      this.balance = balance;
    }
  }

  public void setAbsoluteLimit(BigDecimal limit){
    if(limit.compareTo(BigDecimal.ZERO) < 0){
      throw new IllegalArgumentException("Balance must be postive");
    } else {
      this.absoluteLimit = limit;
    }
  }

}
