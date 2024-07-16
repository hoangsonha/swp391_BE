package com.group6.swp391.jobs;

import com.group6.swp391.service.UserService;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.UnsupportedEncodingException;
import java.util.List;

@Component
@Slf4j
public class FirstJob implements Job {

    @Autowired private UserService userService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            List<Integer> check = userService.sendNotificationEmail();
            if(check.size() > 0)
                for(Integer i : check) {
                    System.out.println("Email offline sent to " + i);
                }
            else
                System.out.println("Error email offline");
        } catch (Exception e) {
            log.error("Can not send mail : {}", e.getMessage());
        }
    }

    // khi taạo ra 1 job thì method execute sẽ chạy

    // nếu muốn thêm thông tin có thể tạo ra some method and call them in execute
}
