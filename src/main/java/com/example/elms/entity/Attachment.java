package com.example.elms.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "attachments")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Attachment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "leave_request_id")
    private Long leaveRequestId;
    
    @Column(name = "url", nullable = false)
    private String url;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}