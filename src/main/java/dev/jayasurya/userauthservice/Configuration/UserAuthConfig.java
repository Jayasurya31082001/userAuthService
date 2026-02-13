package dev.jayasurya.userauthservice.Configuration;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.MacAlgorithm;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.SecretKey;


@Configuration
@EnableWebSecurity
public class UserAuthConfig {

@Bean
    public  BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // For typical stateless REST APIs; if you use browser sessions, revisit this.
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/register", "/auth/login","/auth/validateToken").permitAll()
                        .anyRequest().authenticated()
                )

                // Turn off default login prompts if you use JWT, etc.
                .httpBasic(Customizer.withDefaults())   // or .httpBasic(b -> b.disable())
                .formLogin(f -> f.disable())
                .build();
    }

    @Bean
    public SecretKey secretKey() {
        MacAlgorithm algorithm = Jwts.SIG.HS256;
        SecretKey secretKey = algorithm.key().build();
        return  secretKey;
    }



}
