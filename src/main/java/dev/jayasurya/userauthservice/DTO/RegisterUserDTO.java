package dev.jayasurya.userauthservice.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterUserDTO {
    private  String username;
    private String email;
    private String password;
}
