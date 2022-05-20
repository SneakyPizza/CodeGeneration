package io.swagger.model.dto;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * InlineResponse200
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-05-04T11:04:07.506Z[GMT]")


public class InlineResponse200   {
  @JsonProperty("JWTtoken")
  private String jwTtoken = null;

  public InlineResponse200 jwTtoken(String jwTtoken) {
    this.jwTtoken = jwTtoken;
    return this;
  }

  /**
   * Get jwTtoken
   * @return jwTtoken
   **/
  @Schema(description = "")
  
    public String getJwTtoken() {
    return jwTtoken;
  }

  public void setJwTtoken(String jwTtoken) {
    this.jwTtoken = jwTtoken;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InlineResponse200 inlineResponse200 = (InlineResponse200) o;
    return Objects.equals(this.jwTtoken, inlineResponse200.jwTtoken);
  }

  @Override
  public int hashCode() {
    return Objects.hash(jwTtoken);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InlineResponse200 {\n");
    
    sb.append("    jwTtoken: ").append(toIndentedString(jwTtoken)).append("\n");
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
