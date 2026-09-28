package com.eventpulse.authservice.service;

import com.eventpulse.authservice.dto.AuthResponse;
import com.eventpulse.authservice.dto.LoginRequest;
import com.eventpulse.authservice.dto.SignupRequest;
import com.eventpulse.authservice.entity.User;
import com.eventpulse.authservice.repository.UserRepository;
import com.eventpulse.authservice.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public AuthResponse signup(SignupRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new IllegalStateException("username already exist");
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalStateException("email already exist");
        }

        User user = new User(
                request.username(),
                request.email(),
                passwordEncoder.encode(request.password()),
                "USER"
        );
        userRepository.save(user);

        String token = jwtService.generateToken(user.getUsername(), user.getRole());
        return new AuthResponse(token, user.getUsername(), user.getRole());
    }

    public AuthResponse login(LoginRequest request) {
    
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        User user = userRepository.findByUsername(request.username()).orElseThrow();
        String token = jwtService.generateToken(user.getUsername(), user.getRole());
        return new AuthResponse(token, user.getUsername(), user.getRole());
    }
}