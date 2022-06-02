package io.swagger.model.entities;

import javax.persistence.*;

import io.swagger.model.UserDTO;
import io.swagger.model.dto.LoginDTO;
import lombok.Data;

@Entity
@Data
public class Login<list> {
  private String email;
  private String password;

  public LoginDTO getLoginDTO() {
    LoginDTO loginDTO = new LoginDTO();
    loginDTO.setUsername(this.email);
    loginDTO.setPassword(this.password);
    return loginDTO;
  }

  public Login getLoginModel(LoginDTO loginDTO) {
    Login login = new Login();
    login.setEmail(loginDTO.getUsername());
    login.setPassword(loginDTO.getPassword());
    return login;
  }
}
