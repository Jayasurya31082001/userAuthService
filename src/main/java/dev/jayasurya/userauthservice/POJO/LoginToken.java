package dev.jayasurya.userauthservice.POJO;

import dev.jayasurya.userauthservice.Models.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
public class LoginToken {
    private String token;
    private User user;

}
