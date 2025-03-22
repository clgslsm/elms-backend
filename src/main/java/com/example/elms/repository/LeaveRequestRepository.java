package com.example.elms.repository;

import com.example.elms.entity.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest,Long> {
    List<LeaveRequest> findAllByIdUserSend(Long id);
    List<LeaveRequest> findAllByIdUserReceive(Long id);
}
