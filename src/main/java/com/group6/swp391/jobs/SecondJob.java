package com.group6.swp391.jobs;

import com.group6.swp391.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SecondJob implements Job {

    @Autowired
    private UserService userService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            boolean check = userService.sendNotificationEmailHappyBirthDay();
            if(check)
                System.out.println("Mail happy birthday sent");
             else
                System.out.println("Error mail happy birthday sent");
        } catch (Exception e) {
            log.error("Can not send mail : {}", e.getMessage());
        }
    }
}
