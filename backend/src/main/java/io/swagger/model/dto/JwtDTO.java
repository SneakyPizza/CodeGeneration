package io.swagger.model.dto;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;

/**
 * InlineResponse200
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-06-08T13:48:19.049Z[GMT]")


public class JwtDTO {
  @JsonProperty("JWTtoken")
  private String jwtToken = null;

  @JsonProperty("Id")
  private String id = null;

  public JwtDTO jwTtoken(String jwTtoken) {
    this.jwtToken = jwTtoken;
    return this;
  }

  /**
   * Get jwTtoken
   * @return jwTtoken
   **/
  @Schema(description = "")

  public String getJwtToken() {
    return jwtToken;
  }

  public void setJwtToken(String jwtToken) {
    this.jwtToken = jwtToken;
  }

  public JwtDTO id(String id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
   **/
  @Schema(description = "")

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    JwtDTO inlineResponse200 = (JwtDTO) o;
    return Objects.equals(this.jwtToken, inlineResponse200.jwtToken) &&
            Objects.equals(this.id, inlineResponse200.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(jwtToken, id);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InlineResponse200 {\n");

    sb.append("    jwTtoken: ").append(toIndentedString(jwtToken)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
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
}
