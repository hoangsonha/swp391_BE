package com.group6.swp391.service;

import com.group6.swp391.jobs.FirstJob;
import com.group6.swp391.schedule.MainSchedule;
import jakarta.annotation.PostConstruct;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FirstJobRun {
    @Autowired
    private MainSchedule schedule;

    @Value("${job.first.hour}")
    private int hour;

    @Value("${job.first.minute}")
    private int minute;
    @PostConstruct
    public void init() throws SchedulerException {
        schedule.scheduler(FirstJob.class, hour, minute);
    }
}
