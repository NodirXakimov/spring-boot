package com.amigoscode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@EnableScheduling
@SpringBootApplication
public class SpringAndSpringBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(
                SpringAndSpringBootApplication.class,
                args
        );
    }

    @Scheduled(
//            fixedRate = 5,
//            timeUnit = TimeUnit.SECONDS
            cron = "*/5 */2 * * * *"
    )
    public void sendEmail() throws InterruptedException {
        System.out.println("Start sending email...");
        Thread.sleep(2000);
        System.out.println("End sending email...");
    }

}
