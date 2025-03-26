package com.example.elms.repository;

import com.example.elms.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    List<Attachment> findAllByLeaveRequestId(Long leaveRequestId);
    void deleteAllByLeaveRequestId(Long leaveRequestId);
}