package dev.jayasurya.userauthservice.Controller;

import dev.jayasurya.userauthservice.DTO.LoginRequestDTO;
import dev.jayasurya.userauthservice.DTO.LoginResponseDTO;
import dev.jayasurya.userauthservice.DTO.RegisterUserDTO;
import dev.jayasurya.userauthservice.DTO.UserDTO;
import dev.jayasurya.userauthservice.Models.User;
import dev.jayasurya.userauthservice.Models.ValidateTokenDTO;
import dev.jayasurya.userauthservice.POJO.LoginToken;
import dev.jayasurya.userauthservice.Repository.UserRepository;
import dev.jayasurya.userauthservice.Service.IAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
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
        LoginToken loginToken = authService.loginUser(loginRequestDTO.getEmail(),loginRequestDTO.getPassword());
        if(loginToken!=null){
            MultiValueMap<String,String> multiValueMap = new LinkedMultiValueMap<>();
            multiValueMap.add(HttpHeaders.COOKIE,loginToken.getToken());
            HttpHeaders httpHeaders = new HttpHeaders(multiValueMap);

            return  new ResponseEntity<>(loginToken.getUser().toLoginResponseDTO(),httpHeaders,HttpStatus.OK);
        }else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/validateToken")
    public  ResponseEntity<String> validateToken(@RequestBody ValidateTokenDTO validateTokenDTO){
        boolean isValid = authService.validateToken(validateTokenDTO.getToken());
        if(isValid){
            return ResponseEntity.ok("Token is valid");
        }else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
    }
}
