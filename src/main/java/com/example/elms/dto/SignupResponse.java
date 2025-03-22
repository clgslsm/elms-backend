package com.example.elms.dto;

import com.example.elms.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class SignupResponse {
    String message;
    User user;
}
