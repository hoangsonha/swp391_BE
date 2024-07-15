package com.group6.swp391.jobs;

import com.group6.swp391.model.CrawledDataProperties;
import com.group6.swp391.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ThirdJob implements Job {

    @Autowired
    private UserService userService;

    @Autowired private CrawledDataProperties price;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            boolean check = userService.crawlData();
            if(check) {
                System.out.println("Crawl Data successfully");
                System.out.println("Data cao ve la : " + price.getDola());
            }
            else
                System.out.println("Crawl Data failed");
        } catch (Exception e) {
            log.error("Can not crawl data : {}", e.getMessage());
        }
    }
}
