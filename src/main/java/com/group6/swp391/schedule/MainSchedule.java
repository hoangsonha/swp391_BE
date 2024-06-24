package com.group6.swp391.schedule;

import com.group6.swp391.commonUtils.CommonUtils;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.AllArgsConstructor;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MainSchedule {
    private final Scheduler scheduler;
    private final CommonUtils commonUtils;

    @PostConstruct
    public void startSchedule() throws SchedulerException {
        scheduler.start();
    }

    public void scheduler(Class className, int hour, int minute) throws SchedulerException {
        JobDetail jobDetail = commonUtils.getJobDetail(className);
        Trigger triggerDetail = commonUtils.getTriggerByCronExpression(className, hour, minute);
        scheduler.scheduleJob(jobDetail, triggerDetail);
    }

    @PreDestroy
    public void stopSchedule() throws SchedulerException {
        scheduler.shutdown();
    }
}
