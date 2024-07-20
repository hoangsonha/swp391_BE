package com.group6.swp391.service;

import com.group6.swp391.commonUtils.CommonUtils;
import com.group6.swp391.jobs.FirstJob;
import com.group6.swp391.schedule.MainSchedule;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.quartz.SchedulerException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FirstJobRun {
    private final MainSchedule schedule;
    private final CommonUtils commonUtils;
    @PostConstruct
    public void init() throws SchedulerException {
        schedule.scheduler(FirstJob.class, 8, 0);
    }
}
