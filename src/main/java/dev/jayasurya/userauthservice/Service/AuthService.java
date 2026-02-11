package dev.jayasurya.userauthservice.Service;

import dev.jayasurya.userauthservice.DTO.RegisterUserDTO;
import dev.jayasurya.userauthservice.Exception.IncorrectPasswordException;
import dev.jayasurya.userauthservice.Exception.UserAlreadyExistException;
import dev.jayasurya.userauthservice.Exception.UserNotRegisteredException;
import dev.jayasurya.userauthservice.Models.Role;
import dev.jayasurya.userauthservice.Models.State;
import dev.jayasurya.userauthservice.Models.User;
import dev.jayasurya.userauthservice.Repository.RoleRepository;
import dev.jayasurya.userauthservice.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class AuthService implements  IAuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public User registerUser(RegisterUserDTO registerUserDTO) {
        try {
            Optional<User> optionalUser = userRepository.findByEmail(registerUserDTO.getEmail());
            if (optionalUser.isPresent()) {
                throw new UserAlreadyExistException("User already exist with email: " + registerUserDTO.getEmail());
            }
            User user = new User();
            user.setEmail(registerUserDTO.getEmail());
            user.setPassword(registerUserDTO.getPassword());
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
    public User loginUser(String email, String password) {
        try {

            Optional<User> optionalUser = userRepository.findByEmail(email);
            if (optionalUser.isEmpty()) {
                throw  new UserNotRegisteredException("User not registered with email: " + email);
            }
            User user = optionalUser.get();
            if (!user.getPassword().equals(password)) {
                throw  new IncorrectPasswordException("Incorrect password for email: " + email);
            }
            return user;
        } catch (Exception e) {
            return null;
        }

    }
}

