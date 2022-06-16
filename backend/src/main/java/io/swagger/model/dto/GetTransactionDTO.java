package io.swagger.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

/**
 * GetTransactionDTO
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-05-28T11:30:29.125Z[GMT]")


public class GetTransactionDTO   {
  @JsonProperty("fromIBAN")
  private String fromIBAN = null;

  @JsonProperty("toIBAN")
  private String toIBAN = null;

  @JsonProperty("amount")
  private BigDecimal amount = null;

  @JsonProperty("timestamp")
  private String timestamp = null;

  @JsonProperty("fromUserId")
  private UUID fromUserId = null;

  @JsonProperty("type")
  private String type = null;

  public GetTransactionDTO fromIBAN(String fromIBAN) {
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

  public GetTransactionDTO toIBAN(String toIBAN) {
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

  public GetTransactionDTO amount(BigDecimal amount) {
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

  public GetTransactionDTO timestamp(String timestamp) {
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

  public GetTransactionDTO fromUserId(UUID fromUserId) {
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

  public GetTransactionDTO type(String type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
   **/
  @Schema(description = "")

  @Valid
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetTransactionDTO getTransactionDTO = (GetTransactionDTO) o;
    return Objects.equals(this.fromIBAN, getTransactionDTO.fromIBAN) &&
        Objects.equals(this.toIBAN, getTransactionDTO.toIBAN) &&
        Objects.equals(this.amount, getTransactionDTO.amount) &&
        Objects.equals(this.timestamp, getTransactionDTO.timestamp) &&
        Objects.equals(this.fromUserId, getTransactionDTO.fromUserId) &&
            Objects.equals(this.type, getTransactionDTO.type);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fromIBAN, toIBAN, amount, timestamp, fromUserId, type);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetTransactionDTO {\n");
    
    sb.append("    fromIBAN: ").append(toIndentedString(fromIBAN)).append("\n");
    sb.append("    toIBAN: ").append(toIndentedString(toIBAN)).append("\n");
    sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    fromUserId: ").append(toIndentedString(fromUserId)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
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
