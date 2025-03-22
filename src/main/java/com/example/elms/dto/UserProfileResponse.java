package com.example.elms.dto;

import com.example.elms.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class UserProfileResponse {
    
//    @Schema(description = "User's ID", example = "1")
//    private Long id;
//
//    @Schema(description = "User's username", example = "john.doe")
//    private String username;
//
//    @Schema(description = "User's email address", example = "john.doe@example.com")
//    private String email;
//
//    @Schema(description = "User's full name", example = "John Doe")
//    private String fullName;
//
//    @Schema(description = "User's role", example = "USER")
//    private String role;
//
//    @Schema(description = "Account creation date")
//    private LocalDateTime createdAt;

    private User user;

    private String message;

    private Long role;
    // Constructors, getters and setters
//    public UserProfileResponse() {}
    
//    public UserProfileResponse(Long id, String username, String email, String fullName, String role, LocalDateTime createdAt) {
//        this.id = id;
//        this.username = username;
//        this.email = email;
//        this.fullName = fullName;
//        this.role = role;
//        this.createdAt = createdAt;
//    }
    
//    // Getters and setters
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public String getFullName() {
//        return fullName;
//    }
//
//    public void setFullName(String fullName) {
//        this.fullName = fullName;
//    }
//
//    public String getRole() {
//        return role;
//    }
//
//    public void setRole(String role) {
//        this.role = role;
//    }
//
//    public LocalDateTime getCreatedAt() {
//        return createdAt;
//    }
//
//    public void setCreatedAt(LocalDateTime createdAt) {
//        this.createdAt = createdAt;
//    }
}