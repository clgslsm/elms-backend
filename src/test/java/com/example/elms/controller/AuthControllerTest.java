package com.example.elms.controller;

import com.example.elms.dto.*;
import com.example.elms.entity.User;
import com.example.elms.service.AuthService;
import com.example.elms.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private AuthService authService;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthController authController;

    @Test
    void testRegisterUser() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
//        SignupRequest signupRequest = new SignupRequest("john.doe", "john.doe@example.com", "password123", "John Doe");
        User user = User.builder().username("john.doe").email("john.doe@example.com").fullName("John Doe").build();
        Mockito.when(authService.registerUser(any(SignupRequest.class))).thenReturn(user);

        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "username": "john.doe",
                            "email": "john.doe@example.com",
                            "password": "password123",
                            "fullName": "John Doe"
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Register Successfully !!"))
                .andExpect(jsonPath("$.user.username").value("john.doe"));
    }

    @Test
    void testLogin() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
//        LoginRequest loginRequest = new LoginRequest("john.doe", "password123");
        User user = User.builder().username("john.doe").idRole(1L).build();
        Mockito.when(authService.login(any(LoginRequest.class))).thenReturn(user);
        Mockito.when(jwtUtil.generateToken("john.doe")).thenReturn("mocked-jwt-token");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "username": "john.doe",
                            "password": "password123"
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Login Successfully !!"))
                .andExpect(jsonPath("$.token").value("mocked-jwt-token"));
    }

    @Test
    void testGetProfile() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
        User user = User.builder().username("john.doe").email("john.doe@example.com").fullName("John Doe").idRole(1L).build();
        Mockito.when(authService.getProfile()).thenReturn(user);

        mockMvc.perform(get("/api/auth/profile"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Get user successfully !!"))
                .andExpect(jsonPath("$.user.username").value("john.doe"));
    }
}
