package com.example.elms.controller;

import com.example.elms.dto.*;
import com.example.elms.entity.User;
import com.example.elms.exception.EmailAreadyExisted;
import com.example.elms.exception.InvalidException;
import com.example.elms.exception.UserNotFound;
import com.example.elms.exception.UsernameAlreadyExisted;
import com.example.elms.repository.UserRepository;
import com.example.elms.service.AuthService;
import com.example.elms.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Authentication API")
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;
    @Operation(summary = "Register a new user", description = "Creates a new user account")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User registered successfully"),
        @ApiResponse(responseCode = "400", description = "Username or email already exists")
    })
    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> registerUser(@RequestBody SignupRequest signupRequest) {
        try{
            return ResponseEntity.ok(
                SignupResponse.builder()
                    .message("Register Successfully !!")
                    .user(authService.registerUser(signupRequest))
                    .build()
            );
        } catch (UsernameAlreadyExisted | EmailAreadyExisted e) {
            return ResponseEntity
                .status(HttpStatusCode.valueOf(HttpStatus.BAD_REQUEST.value()))
                .body(
                    SignupResponse.builder()
                        .message(e.getMessage())
                        .user(null)
                        .build()
                );
        }
    }

    @Operation(summary = "Login", description = "Authenticates user credentials and returns JWT token")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully authenticated",
                     content = @Content(schema = @Schema(implementation = JwtResponse.class))),
        @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest loginRequest) {
        try{
            User user = authService.login(loginRequest);
            return ResponseEntity.ok(
                JwtResponse.builder()
                    .message("Login Successfully !!")
                    .token(jwtUtil.generateToken(user.getUsername()))
                    .idRole(user.getIdRole())
                    .build()
            );
        }catch (InvalidException e){
            return ResponseEntity
                .status(HttpStatusCode.valueOf(HttpStatus.BAD_REQUEST.value()))
                .body(
                    JwtResponse.builder()
                        .message(e.getMessage())
                        .token(null)
                        .build()
                );
        }
    }

    @Operation(summary = "Get user profile", description = "Returns the authenticated user's profile information",
               security = { @SecurityRequirement(name = "bearerAuth") })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Profile retrieved successfully",
                     content = @Content(schema = @Schema(implementation = UserProfileResponse.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/profile")
    public ResponseEntity<UserProfileResponse> getProfile() {
        try {
            return ResponseEntity.ok(
                UserProfileResponse.builder()
                    .message("Get user successfully !!")
                    .user(authService.getProfile())
                    .role(1L)
                    .build()
            );
        } catch (UserNotFound e){
            return ResponseEntity
                .status(HttpStatusCode.valueOf(HttpStatus.FORBIDDEN.value()))
                .body(
                    UserProfileResponse.builder()
                        .message(e.getMessage())
                        .user(null)
                        .role(0L)
                        .build()
                );
        }
    }
}
