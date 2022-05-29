package io.swagger.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

/**
 * PostTransactionDTO
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-05-28T11:30:29.125Z[GMT]")


public class PostTransactionDTO   {
  @JsonProperty("fromIBAN")
  private String fromIBAN = null;

  @JsonProperty("toIBAN")
  private String toIBAN = null;

  @JsonProperty("pincode")
  private String pincode = null;

  @JsonProperty("amount")
  private BigDecimal amount = null;

  @JsonProperty("timestamp")
  private String timestamp = null;

  @JsonProperty("fromUserId")
  private UUID fromUserId = null;

  public PostTransactionDTO fromIBAN(String fromIBAN) {
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

  public PostTransactionDTO toIBAN(String toIBAN) {
    this.toIBAN = toIBAN;
    return this;
  }

  /**
   * Get toIBAN
   * @return toIBAN
   **/
  @Schema(description = "")
  
    public String getToIBAN() {
    return toIBAN;
  }

  public void setToIBAN(String toIBAN) {
    this.toIBAN = toIBAN;
  }

  public PostTransactionDTO pincode(String pincode) {
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

  public PostTransactionDTO amount(BigDecimal amount) {
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

  public PostTransactionDTO timestamp(String timestamp) {
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

  public PostTransactionDTO fromUserId(UUID fromUserId) {
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
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PostTransactionDTO postTransactionDTO = (PostTransactionDTO) o;
    return Objects.equals(this.fromIBAN, postTransactionDTO.fromIBAN) &&
        Objects.equals(this.toIBAN, postTransactionDTO.toIBAN) &&
        Objects.equals(this.pincode, postTransactionDTO.pincode) &&
        Objects.equals(this.amount, postTransactionDTO.amount) &&
        Objects.equals(this.timestamp, postTransactionDTO.timestamp) &&
        Objects.equals(this.fromUserId, postTransactionDTO.fromUserId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fromIBAN, toIBAN, pincode, amount, timestamp, fromUserId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PostTransactionDTO {\n");
    
    sb.append("    fromIBAN: ").append(toIndentedString(fromIBAN)).append("\n");
    sb.append("    toIBAN: ").append(toIndentedString(toIBAN)).append("\n");
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
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
