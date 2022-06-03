package io.swagger.model.entities;

import io.swagger.model.dto.GetTransactionDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Data
@NoArgsConstructor
public class Transaction   {

  @Id
  @GeneratedValue
  @NonNull
  private UUID id;
  @NonNull()
  private TransactionType type;
  @NonNull
  private String IBAN;
  @NonNull
  @OneToOne
  @Cascade(CascadeType.SAVE_UPDATE )
  private Account Target;
  @NonNull
  private String pincode;
  @NonNull
  private BigDecimal amount;

  @NonNull
  private LocalDateTime timestamp;
  @NonNull
  @OneToOne
  @Cascade(CascadeType.SAVE_UPDATE )
  private Account Origin;
  @NonNull
  @OneToOne
  private User Performer;

  //all args constructor
    public Transaction(TransactionType type, String IBAN, Account Target, String pincode, BigDecimal amount, LocalDateTime timestamp, Account Origin, User Performer) {
        this.type = type;
        this.IBAN = IBAN;
        this.Target = Target;
        this.pincode = pincode;
        this.amount = amount;
        this.timestamp = timestamp;
        this.Origin = Origin;
        this.Performer = Performer;
    }

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
  //setter for amount
    public void setAmount(BigDecimal amount) {
        //check if amount is positive
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }//if null throw exception
        else {
            this.amount = amount;
        }

    }

  //execute transaction
  public void execute(){
    this.Origin.setBalance(this.Origin.getBalance().subtract(this.amount));
    this.Target.setBalance(this.Target.getBalance().add(this.amount));
  }

}
