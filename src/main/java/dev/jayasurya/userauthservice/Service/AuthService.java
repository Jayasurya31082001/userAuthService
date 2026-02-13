package dev.jayasurya.userauthservice.Service;

import ch.qos.logback.core.joran.sanity.Pair;
import dev.jayasurya.userauthservice.DTO.RegisterUserDTO;
import dev.jayasurya.userauthservice.Exception.IncorrectPasswordException;
import dev.jayasurya.userauthservice.Exception.UserAlreadyExistException;
import dev.jayasurya.userauthservice.Exception.UserNotRegisteredException;
import dev.jayasurya.userauthservice.Models.Role;
import dev.jayasurya.userauthservice.Models.Session;
import dev.jayasurya.userauthservice.Models.State;
import dev.jayasurya.userauthservice.Models.User;
import dev.jayasurya.userauthservice.POJO.LoginToken;
import dev.jayasurya.userauthservice.Repository.RoleRepository;
import dev.jayasurya.userauthservice.Repository.SessionRepository;
import dev.jayasurya.userauthservice.Repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.swing.*;
import java.util.*;

@Service

public class AuthService implements  IAuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private SessionRepository sessionRepository;
   @Autowired
   private BCryptPasswordEncoder passwordEncoder;

  @Autowired
  private SecretKey secretKey;
    @Override
    public User registerUser(RegisterUserDTO registerUserDTO) {
        try {
            Optional<User> optionalUser = userRepository.findByEmail(registerUserDTO.getEmail());
            if (optionalUser.isPresent()) {
                throw new UserAlreadyExistException("User already exist with email: " + registerUserDTO.getEmail());
            }
            User user = new User();
            user.setEmail(registerUserDTO.getEmail());
            user.setPassword(passwordEncoder.encode(registerUserDTO.getPassword()));
            user.setUsername(registerUserDTO.getUsername());
            user.setState(State.ACTIVE);
            Optional<Role> optionalRole = roleRepository.findByName("DEFAULT_USER");
            Role role;
            if (optionalRole.isEmpty()) {
                role = new Role();
                role.setName("DEFAULT_USER");
                roleRepository.save(role);
            } else {
                role = optionalRole.get();
            }
            List<Role> roles = new ArrayList<>();
            roles.add(role);
            user.setRoles(roles);

            return userRepository.save(user);
        } catch (Exception e) {
            return null;
        }
    }


    @Override
    public LoginToken loginUser(String email, String password) {
        try {

            Optional<User> optionalUser = userRepository.findByEmail(email);
            if (optionalUser.isEmpty()) {
                throw  new UserNotRegisteredException("User not registered with email: " + email);
            }
            User user = optionalUser.get();
            if (!passwordEncoder.matches(password, user.getPassword())) {
                throw  new IncorrectPasswordException("Incorrect password for email: " + email);
            }

            Map<String,Object> payload = new HashMap<>();
            Long currentTime = System.currentTimeMillis();
            payload.put("iat",currentTime);
            payload.put("exp",currentTime + 1000000);
            payload.put("iss","UserAuthService");
            payload.put("userId",user.getId());
            payload.put("scope",user.getRoles());

            String token = Jwts.builder().claims(payload).signWith(secretKey).compact();
            Session session = new Session(token,user);
             session.setState(State.ACTIVE);
            sessionRepository.save(session);
            return new LoginToken(token,user);
        } catch (Exception e) {
            return null;
        }

    }

    @Override
    public boolean validateToken(String token) {
        try{
            Optional<Session> optionalSession = sessionRepository.findByToken(token);
            if(optionalSession.isEmpty()){
                return false;
            }
            Session session = optionalSession.get();

            JwtParser jwtParser = Jwts.parser().verifyWith(secretKey).build();
            Claims claims = jwtParser.parseSignedClaims(token).getPayload();

            Long expireTime = System.currentTimeMillis();
            if((Long)claims.get("exp")<  expireTime){
                session.setState(State.INACTIVE);
                sessionRepository.save(session);
                return false;
            }
            return  true;
        } catch (Exception e) {
            return false;
        }

    }
}

