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
public class FirstJob implements Job {

    @Autowired private UserService userService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            boolean check = userService.sendNotificationEmail();
            if(check) {
                System.out.println("Email offline sent");
            } else {
                System.out.println("Error email offline");
            }
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    // khi taạo ra 1 job thì method execute sẽ chạy

    // nếu muốn thêm thông tin có thể tạo ra some method and call them in execute
}
