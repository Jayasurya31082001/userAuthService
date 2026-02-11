package dev.jayasurya.userauthservice.Models;

import dev.jayasurya.userauthservice.DTO.LoginResponseDTO;
import dev.jayasurya.userauthservice.DTO.UserDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;

import java.net.URI;
import java.util.List;

@Entity
@Getter
@Setter
public class User extends BaseModel{
    private String username;
    private String password;
    private String email;
    @ManyToMany
    private List<Role> roles;

    public UserDTO toUserDTO() {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(this.getId());
        userDTO.setUsername(this.username);
        userDTO.setEmail(this.email);
        return  userDTO;
    }

    public LoginResponseDTO toLoginResponseDTO() {
        LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
        loginResponseDTO.setId(this.getId());
        loginResponseDTO.setUsername(this.username);
        loginResponseDTO.setEmail(this.email);
        return loginResponseDTO;
    }
}
