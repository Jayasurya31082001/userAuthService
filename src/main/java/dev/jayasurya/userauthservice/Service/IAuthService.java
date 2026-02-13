package dev.jayasurya.userauthservice.Service;

import ch.qos.logback.core.joran.sanity.Pair;
import dev.jayasurya.userauthservice.DTO.RegisterUserDTO;
import dev.jayasurya.userauthservice.Models.User;
import dev.jayasurya.userauthservice.Models.ValidateTokenDTO;
import dev.jayasurya.userauthservice.POJO.LoginToken;

public interface IAuthService {
    public User registerUser(RegisterUserDTO registerUserDTO);
    public LoginToken loginUser(String email, String password);
    public  boolean validateToken(String token);
}
