package com.group6.swp391.service;

import com.group6.swp391.commonUtils.CommonUtils;
import com.group6.swp391.jobs.ThirdJob;
import com.group6.swp391.schedule.MainSchedule;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ThirdJobRun {

    @Autowired private MainSchedule schedule;

    @Value("${job.third.minute}")
    private int minute;

    @PostConstruct
    public void init() throws SchedulerException {
        schedule.scheduler(ThirdJob.class, minute);
    }
}
