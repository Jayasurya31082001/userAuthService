package dev.jayasurya.userauthservice.Controller;

import dev.jayasurya.userauthservice.DTO.LoginRequestDTO;
import dev.jayasurya.userauthservice.DTO.LoginResponseDTO;
import dev.jayasurya.userauthservice.DTO.RegisterUserDTO;
import dev.jayasurya.userauthservice.DTO.UserDTO;
import dev.jayasurya.userauthservice.Models.User;
import dev.jayasurya.userauthservice.Repository.UserRepository;
import dev.jayasurya.userauthservice.Service.IAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private IAuthService authService;
    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@RequestBody RegisterUserDTO registerUserDTO) {
        User user = authService.registerUser(registerUserDTO);
        if (user != null) {
            return new ResponseEntity<>(user.toUserDTO(), HttpStatus.CREATED);
        } else {
            return ResponseEntity.badRequest().build();

        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> loginUser(@RequestBody LoginRequestDTO loginRequestDTO) {
        User user = authService.loginUser(loginRequestDTO.getEmail(),loginRequestDTO.getPassword());
        if(user!=null){
            return  new ResponseEntity<>(user.toLoginResponseDTO(),HttpStatus.OK);
        }else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
