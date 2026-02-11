package dev.jayasurya.userauthservice.Service;

import dev.jayasurya.userauthservice.DTO.RegisterUserDTO;
import dev.jayasurya.userauthservice.Models.User;

public interface IAuthService {
    public User registerUser(RegisterUserDTO registerUserDTO);
    public  User loginUser(String email, String password);
}
