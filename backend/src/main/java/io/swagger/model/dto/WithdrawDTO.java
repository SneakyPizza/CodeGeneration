package io.swagger.model.dto;

import java.math.BigDecimal;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

import lombok.Data;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * WithdrawDTO
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-06-01T14:27:29.019Z[GMT]")

@Data
public class WithdrawDTO   {
  @JsonProperty("fromIBAN")
  private String fromIBAN = null;

  @JsonProperty("pincode")
  private String pincode = null;

  @JsonProperty("amount")
  private BigDecimal amount = null;

  @JsonProperty("timestamp")
  private String timestamp = null;

  @JsonProperty("fromUserId")
  private UUID fromUserId = null;

  public WithdrawDTO fromIBAN(String fromIBAN) {
    this.fromIBAN = fromIBAN;
    return this;
  }

  /**
   * Get fromIBAN
   * @return fromIBAN
   **/
  @Schema(description = "")
  
    public String getFromIBAN() {
    return fromIBAN;
  }

  public void setFromIBAN(String fromIBAN) {
    this.fromIBAN = fromIBAN;
  }

  public WithdrawDTO pincode(String pincode) {
    this.pincode = pincode;
    return this;
  }

  /**
   * Get pincode
   * @return pincode
   **/
  @Schema(description = "")
  
    public String getPincode() {
    return pincode;
  }

  public void setPincode(String pincode) {
    this.pincode = pincode;
  }

  public WithdrawDTO amount(BigDecimal amount) {
    this.amount = amount;
    return this;
  }

  /**
   * Get amount
   * @return amount
   **/
  @Schema(description = "")
  
    public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public WithdrawDTO timestamp(String timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  /**
   * Get timestamp
   * @return timestamp
   **/
  @Schema(description = "")
  
    public String getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }

  public WithdrawDTO fromUserId(UUID fromUserId) {
    this.fromUserId = fromUserId;
    return this;
  }

  /**
   * Get fromUserId
   * @return fromUserId
   **/
  @Schema(description = "")
  
    @Valid
    public UUID getFromUserId() {
    return fromUserId;
  }

  public void setFromUserId(UUID fromUserId) {
    this.fromUserId = fromUserId;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    WithdrawDTO withdrawDTO = (WithdrawDTO) o;
    return Objects.equals(this.fromIBAN, withdrawDTO.fromIBAN) &&
        Objects.equals(this.pincode, withdrawDTO.pincode) &&
        Objects.equals(this.amount, withdrawDTO.amount) &&
        Objects.equals(this.timestamp, withdrawDTO.timestamp) &&
        Objects.equals(this.fromUserId, withdrawDTO.fromUserId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fromIBAN, pincode, amount, timestamp, fromUserId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class WithdrawDTO {\n");
    
    sb.append("    fromIBAN: ").append(toIndentedString(fromIBAN)).append("\n");
    sb.append("    pincode: ").append(toIndentedString(pincode)).append("\n");
    sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    fromUserId: ").append(toIndentedString(fromUserId)).append("\n");
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

  public PostTransactionDTO toPostTransactionDTO(String iban) {
    PostTransactionDTO postTransactionDTO = new PostTransactionDTO();
    postTransactionDTO.setAmount(this.amount);
    postTransactionDTO.setFromIBAN(this.fromIBAN);
    postTransactionDTO.setToIBAN(iban);
    postTransactionDTO.setTimestamp(this.timestamp);
    postTransactionDTO.setPincode(this.pincode);
    postTransactionDTO.setFromUserId(this.fromUserId);
    return postTransactionDTO;
  }

}
