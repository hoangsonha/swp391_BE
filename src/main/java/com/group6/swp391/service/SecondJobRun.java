package com.group6.swp391.service;

import com.group6.swp391.commonUtils.CommonUtils;
import com.group6.swp391.jobs.SecondJob;
import com.group6.swp391.schedule.MainSchedule;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SecondJobRun {

    @Autowired
    private MainSchedule schedule;

    @Value("${job.second.hour}")
    private int hour;

    @Value("${job.second.minute}")
    private int minute;

    @PostConstruct
    public void init() throws SchedulerException {
        schedule.scheduler(SecondJob.class, hour, minute);
        //"0/10 * * * * ?" // 10s
    }
}
