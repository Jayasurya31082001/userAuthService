package dev.jayasurya.userauthservice.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class LoginResponseDTO {
    private UUID id;
    private String username;
    private String email;
}
