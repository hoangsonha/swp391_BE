package com.group6.swp391.service;

import com.group6.swp391.commonUtils.CommonUtils;
import com.group6.swp391.jobs.SecondJob;
import com.group6.swp391.schedule.MainSchedule;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.quartz.SchedulerException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SecondJobRun {
    private final MainSchedule schedule;
    private final CommonUtils commonUtils;

    @PostConstruct
    public void init() throws SchedulerException {
        schedule.scheduler(SecondJob.class, 8, 0);
        //"0/10 * * * * ?" // 10s
    }
}
