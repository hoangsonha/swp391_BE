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

//1. chạy vào MainSchedule class để start Scheduler bởi @PostConstruct, mỗi Scheduler sẽ có Trigger và JobDetail,
//   có thể tạo ra nhiều Scheduler, trong đây là 1 Scheduler với TriggerInfo và String cron để set time
//2. chạy đến service để run job bởi @PostConstruct, gọi tới CommonUtils để get ìnformation of Trigger và JobDetail, sau
//   đó trả về TriggerInfo và tên class để run và gọi hàm thực thi Scheduler ở MainSchedule class
//3. ở hàm thực thi Scheduler ở MainSchedule class sẽ lấy ra Trigger và JobDetail dựa theo tên class và TriggerInfo
//   mà đc trả về trước đó từ class JobRun
//  trong hàm getJobDetail sẽ map class vào JobData với tên là class và value là TriggerInfo đc truyền vào
//  trong hàm getTrigger sẽ thiết lập các thuộc tính của TriggerInfo đc truyền vào
//4. chạy vào Job class mà implement interface Job

// package job là tạo ra các jobs