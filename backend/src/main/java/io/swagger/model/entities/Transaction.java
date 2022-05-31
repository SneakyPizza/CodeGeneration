package io.swagger.model.entities;

import io.swagger.model.dto.GetTransactionDTO;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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

  private BigDecimal amount;

  private LocalDateTime timestamp;

  @OneToOne
  private Account Origin;

  @OneToOne
  private User Performer;

  public GetTransactionDTO toGetTransactionDTO() {
    GetTransactionDTO transactionDTO = new GetTransactionDTO();
    transactionDTO.setFromIBAN(IBAN);
    transactionDTO.setToIBAN(Target.getIBAN());
    transactionDTO.setAmount(amount);
    //set timestamp is current datetime
    transactionDTO.setTimestamp(LocalDateTime.now().toString());

    transactionDTO.setFromUserId(Origin.getUser().getId());
    return transactionDTO;
  }

}
