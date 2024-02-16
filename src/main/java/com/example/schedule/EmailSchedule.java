package com.example.schedule;

import com.example.entity.UserPo;
import com.example.service.ReceiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

import java.util.concurrent.ScheduledFuture;

public class EmailSchedule {
    @Autowired
    private TaskScheduler taskScheduler;

    private ScheduledFuture<?> scheduledFuture;

    @Autowired
    private ReceiveService receiveService;
    public void startScheduledTask(UserPo userPo) {
        scheduledFuture = taskScheduler.schedule(() -> {
            // 执行任务
            receiveService.checkForNewEmail(userPo);
        }, new CronTrigger("0 * * * * *")); // 每分钟执行一次
    }

    public void stopScheduledTask() {
        if (scheduledFuture != null) {
            scheduledFuture.cancel(false);
        }
    }

}
