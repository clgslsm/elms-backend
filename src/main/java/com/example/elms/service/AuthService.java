package com.example.elms.service;

import com.example.elms.dto.JwtResponse;
import com.example.elms.dto.LoginRequest;
import com.example.elms.dto.SignupRequest;
import com.example.elms.dto.UserProfileResponse;
import com.example.elms.entity.User;
import com.example.elms.exception.EmailAreadyExisted;
import com.example.elms.exception.InvalidException;
import com.example.elms.exception.UserNotFound;
import com.example.elms.exception.UsernameAlreadyExisted;
import com.example.elms.repository.UserRepository;
import com.example.elms.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User registerUser(SignupRequest signupRequest){
        // Check if username is already taken
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            throw new UsernameAlreadyExisted("Username have already existed");
        }

        // Check if email is already in use
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new EmailAreadyExisted("Emai have already existed");
        }


        User user = User.builder()
            .username(signupRequest.getUsername())
            .email(signupRequest.getEmail())
            .fullName(signupRequest.getFullName())
            .password(passwordEncoder.encode(signupRequest.getPassword()))
            .idRole(1L)
            .build();

        return userRepository.save(user);
    }

    public User login (LoginRequest loginRequest){
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        // Find user in database
        User user = userRepository.findByUsername(username);

        // Check if user exists and password matches
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            // Generate JWT token upon successful authentication
            return user;
        } else {
            throw new InvalidException("Username or password wrong");
        }
    }

    public User getProfile(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UserNotFound("User not found");
        }

        return User.builder()
            .id(user.getId())
            .username(user.getUsername())
            .email(user.getEmail())
            .fullName(user.getFullName())
            .idRole(user.getIdRole())
            .leaveDaysRemain(user.getLeaveDaysRemain())
            .build();
    }
}
