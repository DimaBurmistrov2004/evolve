package com.evolve.service;

import com.evolve.dto.JwtAuthenticationDto;
import com.evolve.dto.RefreshTokenDto;
import com.evolve.dto.UserCredintialsDto;
import com.evolve.model.User;
import com.evolve.repository.UserRepository;
import com.evolve.security.jwt.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public JwtAuthenticationDto signIn(UserCredintialsDto userCredintialsDto) {
        User user = findByCredentials(userCredintialsDto);
        return jwtService.generateAuthToken(user.getEmail());
    }

    public JwtAuthenticationDto refreshToken(RefreshTokenDto refreshTokenDto) throws Exception {
        String refreshToken = refreshTokenDto.getRefreshToken();
        if (refreshToken != null && jwtService.validateJwtToken(refreshToken)) {
            User user = findByEmail(jwtService.getEmailFromToken(refreshToken));
            return jwtService.refreshBaseToken(user.getEmail(), refreshToken);
        }
        throw new ArithmeticException("Invalid refresh token");
    }

    public JwtAuthenticationDto signUp(UserCredintialsDto userCredintialsDto) {
        // Проверка, существует ли пользователь с таким email
        if (userRepository.findByEmail(userCredintialsDto.getEmail()).isPresent()) {
            throw new RuntimeException("User with this email already exists");
        }

        User user = new User();
        user.setUsername(userCredintialsDto.getUsername());
        user.setPassword(passwordEncoder.encode(userCredintialsDto.getPassword()));
        user.setEmail(userCredintialsDto.getEmail());

        user = userRepository.save(user);

        return jwtService.generateAuthToken(user.getEmail());
    }

    private User findByCredentials(UserCredintialsDto userCredintialsDto) throws AuthenticationException{
        Optional<User> optionalUser = userRepository.findByEmail(userCredintialsDto.getEmail());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (passwordEncoder.matches(userCredintialsDto.getPassword(), user.getPassword())) {
                return user;
            }
        }
        throw new AuthenticationCredentialsNotFoundException("Email or password is not correct");
    }

    private User findByEmail(String email) throws Exception {
        return userRepository.findByEmail(email).orElseThrow(() ->
                new Exception(String.format("User with email not found", email)));
    }
}