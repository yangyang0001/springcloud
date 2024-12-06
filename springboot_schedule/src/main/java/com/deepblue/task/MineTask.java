package com.deepblue.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class MineTask {

    @Scheduled(cron = "0/5 * * * * * *")
    public void sayHello() {
        System.out.println("Hello World");
    }
}
