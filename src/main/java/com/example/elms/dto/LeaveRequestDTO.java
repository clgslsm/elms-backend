package com.example.elms.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeaveRequestDTO {
    private Long idUserReceive;
    private LocalDate startDate;
    private LocalDate endDate;
    private String reason;
    private String leaveType;
    private List<String> attachmentUrls;
}