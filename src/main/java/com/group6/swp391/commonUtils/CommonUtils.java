package com.group6.swp391.commonUtils;

import org.quartz.*;
import org.springframework.stereotype.Service;

import java.util.Date;

import static org.quartz.SimpleScheduleBuilder.simpleSchedule;

@Service
public class CommonUtils {

    // Schedule với String cron thì k phải set JobData

    public JobDetail getJobDetail(Class className) {
        return JobBuilder.newJob(className)
                .withIdentity(className.getSimpleName(), "grp1")    // job dc identify ntn
                .build();
    }


    // get Trigger với String cron

    public Trigger getTriggerByCronExpression(Class className, int hour, int minute) {
        return TriggerBuilder
                .newTrigger()
                .withIdentity(className.getSimpleName())
                .withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(hour, minute))
                .build();
    }

    public Trigger getTriggerByCronExpression(Class className, int minute) {
        return TriggerBuilder
                .newTrigger()
                .withIdentity(className.getSimpleName())
                .withSchedule(simpleSchedule()
                        .withIntervalInMinutes(minute)
                        .repeatForever())
                .build();
    }

}
