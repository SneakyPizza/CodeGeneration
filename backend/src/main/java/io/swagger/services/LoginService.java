package io.swagger.services;

import io.swagger.repositories.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    @Autowired
    private LoginRepository loginRepository;

//    public boolean login(String username, String password) {
//        return loginRepository.login(username, password);
//    }
}
