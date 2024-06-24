package com.group6.swp391.jobs;

import com.group6.swp391.service.UserService;
import jakarta.mail.MessagingException;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

@Component
public class SecondJob implements Job {

    @Autowired
    private UserService userService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            boolean check = userService.sendNotificationEmailHappyBirthDay();
            if(check) {
                System.out.println("Mail happy birthday sent");
            } else {
                System.out.println("Error mail happy birthday sent");
            }
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
