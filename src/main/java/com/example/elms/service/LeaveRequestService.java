package com.example.elms.service;


import com.example.elms.entity.LeaveRequest;
import com.example.elms.entity.User;
import com.example.elms.exception.UnknownRoleException;
import com.example.elms.repository.LeaveRequestRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class LeaveRequestService {
    public final int EMPLOYEE = 1;
    public final int MANAGER = 2;
    private final LeaveRequestRepository leaveRequestRepository;
    public List<LeaveRequest> getAllLeaveRequest(){
        var auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        switch (user.getIdRole().intValue()){
            case EMPLOYEE :
                return leaveRequestRepository.findAllByIdUserSend(user.getId());
            case MANAGER:
                return leaveRequestRepository.findAllByIdUserReceive(user.getId());
            default:
                throw new UnknownRoleException("Unknown this role");
        }
    };
}
