package com.example.elms.service;

import com.example.elms.dto.LoginRequest;
import com.example.elms.dto.SignupRequest;
import com.example.elms.entity.User;
import com.example.elms.exception.EmailAreadyExisted;
import com.example.elms.exception.InvalidException;
import com.example.elms.exception.UserNotFound;
import com.example.elms.exception.UsernameAlreadyExisted;
import com.example.elms.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testRegisterUser_Success() {
        SignupRequest signupRequest = new SignupRequest("testUser", "test@example.com", "password", "Test User");
        when(userRepository.existsByUsername("testUser")).thenReturn(false);
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        User savedUser = User.builder().username("testUser").email("test@example.com").build();
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        User result = authService.registerUser(signupRequest);
        assertNotNull(result);
        assertEquals("testUser", result.getUsername());
    }

    @Test
    void testRegisterUser_UsernameExists() {
        SignupRequest signupRequest = new SignupRequest("testUser", "test@example.com", "password", "Test User");
        when(userRepository.existsByUsername("testUser")).thenReturn(true);

        assertThrows(UsernameAlreadyExisted.class, () -> authService.registerUser(signupRequest));
    }

    @Test
    void testLogin_Success() {
        LoginRequest loginRequest = new LoginRequest("testUser", "password");
        User user = User.builder().username("testUser").password("encodedPassword").build();
        when(userRepository.findByUsername("testUser")).thenReturn(user);
        when(passwordEncoder.matches("password", "encodedPassword")).thenReturn(true);

        User result = authService.login(loginRequest);
        assertNotNull(result);
        assertEquals("testUser", result.getUsername());
    }

    @Test
    void testLogin_InvalidCredentials() {
        LoginRequest loginRequest = new LoginRequest("testUser", "wrongPassword");
        User user = User.builder().username("testUser").password("encodedPassword").build();
        when(userRepository.findByUsername("testUser")).thenReturn(user);
        when(passwordEncoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);

        assertThrows(InvalidException.class, () -> authService.login(loginRequest));
    }

    @Test
    void testGetProfile_Success() {
        User user = User.builder().username("testUser").build();
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testUser");
        when(userRepository.findByUsername("testUser")).thenReturn(user);

        User result = authService.getProfile();
        assertNotNull(result);
        assertEquals("testUser", result.getUsername());
    }

    @Test
    void testGetProfile_UserNotFound() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testUser");
        when(userRepository.findByUsername("testUser")).thenReturn(null);

        assertThrows(UserNotFound.class, () -> authService.getProfile());
    }
}
