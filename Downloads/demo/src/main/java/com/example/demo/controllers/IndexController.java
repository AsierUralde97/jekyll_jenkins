package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.codec.binary.Base64;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.example.demo.mappers.JobMapper;
import com.example.demo.models.DBQueueItem;

@RestController
@RequestMapping("/api")
@EnableScheduling
public class IndexController {

    @Autowired
    JobMapper jobMapper;

    @Value("${com.iberdrola.jenkins.host}")
    private String jenkinsHost;

    @Value("${com.iberdrola.jenkins.moma}")
    private String jenkinsMomaHost;

    @Value("${com.iberdrola.jenkins.host}")
    private String jenkinsPort;

    @Value("${com.iberdrola.jenkins.username}")
    private String username;

    @Value("${com.iberdrola.jenkins.password}")
    private String password;

    @Value("${com.iberdrola.jenkins.queueURL}")
    private String queueURL;

    @Value("${com.iberdrola.jenkins.buildURL}")
    private String buildURL;

    @EventListener(ContextStartedEvent.class)
    @Scheduled(fixedDelay = 1000, initialDelay = 1000)
    public void getQueue() {
        String authString = username + ":" + password;
        String encodedString = new String(Base64.encodeBase64(authString.getBytes()));

        System.out.println("-> Getting queue...");

        try {
            Document jenkinsQueue = Jsoup.connect(queueURL).header("Authorization", "Basic" + encodedString).get();

            jenkinsQueue.getElementsByClass("model.link inside tl-tr").forEach(item -> {
                String buildJobURL = item.attr("href").toString();

                // Meterlo en la BBDD
                if (buildJobURL.split("/").length > 3) {

                    String jobName = buildJobURL.split("/")[2];
                    int jobBuildNumber = Integer.valueOf(buildJobURL.split("/")[3]);
                    Long actualTime = System.currentTimeMillis();

                    if (jobMapper.queueItemExists(buildJobURL) == 0) {
                        // Insert new job

                        LocalDate date = LocalDate.now();
                        int day = date.getDayOfMonth();
                        int month = date.getMonthValue();
                        int year = date.getYear();

                        DBQueueItem queueItem = new DBQueueItem();
                        queueItem.setJobName(jobName);
                        queueItem.setJobBuildNumber(jobBuildNumber);
                        queueItem.setTimeInQueue(actualTime);
                        queueItem.setJobBuildURL(buildJobURL);
                        queueItem.setDay(day);
                        queueItem.setWeek(0);
                        queueItem.setMonth(month);
                        queueItem.setYear(year);

                        jobMapper.insertJobIntoBD(queueItem);
                        System.out.println("-Job inserted:" + buildJobURL);

                    } else {
                        // Update job

                        DBQueueItem dbJob = jobMapper.getJob(buildJobURL);
                        Long timeInQueue = System.currentTimeMillis() - dbJob.getTimeInQueue();

                        jobMapper.updateJob(buildJobURL, timeInQueue);
                        System.out.println("-Job updated:" + buildJobURL);

                    }

                }

            });

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
