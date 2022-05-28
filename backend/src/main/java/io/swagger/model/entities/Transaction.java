package io.swagger.model.entities;

import io.swagger.model.dto.GetTransactionDTO;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.util.UUID;


@Entity
@Data
public class Transaction   {

  @Id
  @GeneratedValue
  private UUID id;

  private String IBAN;

  @OneToOne
  private Account Target;

  private String pincode;

  private Integer amount;

  private String timestamp;

  private UUID fromUserId;

  public GetTransactionDTO toGetTransactionDTO() {
    GetTransactionDTO transactionDTO = new GetTransactionDTO();
    transactionDTO.setFromIBAN(IBAN);
    transactionDTO.setToIBAN(Target.getIBAN());
    transactionDTO.setAmount(amount);
    transactionDTO.setTimestamp(timestamp);
    transactionDTO.setFromUserId(fromUserId);
    return transactionDTO;
  }

}
