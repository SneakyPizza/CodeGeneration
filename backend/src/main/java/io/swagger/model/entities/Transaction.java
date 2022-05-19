package io.swagger.model.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;


@Entity
@Data
public class Transaction   {

  @Id
  @GeneratedValue
  private UUID id;

  private String fromIBAN;

  private String toIBAN;

  private String pincode;

  private Integer amount;

  private String timestamp;

  private UUID fromUserId;


}
