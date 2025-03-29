package com.example.elms.service;


import com.example.elms.entity.User;
import com.example.elms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UpdateLeaveDaysService {
    public final Integer extraDays = 12;
    private final UserRepository userRepository;
    @Scheduled(cron = "0 22 09 * * ?")
    void updateLeaveDays(){
        List<User> users = userRepository.findAll();
        for (User user : users){
            user.setLeaveDaysRemain(user.getLeaveDaysRemain() + extraDays);
        }
        userRepository.saveAll(users);
        System.out.println("UPDATE LEAVE DAYS REMAIN OF USERS");
    }
}
