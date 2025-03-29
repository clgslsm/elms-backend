package com.example.elms.util;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    @InjectMocks
    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtUtil = new JwtUtil();
        jwtUtil.setSecret("test-secret-key-test-secret-key-test-secret-key");
        jwtUtil.setExpiration(3600000L);
    }

    @Test
    void testGenerateToken() {
        String token = jwtUtil.generateToken("testUser");
        assertNotNull(token);
    }

    @Test
    void testValidateToken() {
        String token = jwtUtil.generateToken("testUser");
        assertTrue(jwtUtil.validateToken(token, "testUser"));
    }

    @Test
    void testExtractUsername() {
        String token = jwtUtil.generateToken("testUser");
        String username = jwtUtil.extractUsername(token);
        assertEquals("testUser", username);
    }

    @Test
    void testExtractClaims() {
        String token = jwtUtil.generateToken("testUser");
        Claims claims = jwtUtil.getClaims(token);
        assertNotNull(claims);
        assertEquals("testUser", claims.getSubject());
    }
}
