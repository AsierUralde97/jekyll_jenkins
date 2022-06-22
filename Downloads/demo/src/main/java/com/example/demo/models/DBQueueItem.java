package com.example.demo.models;

public class DBQueueItem {

    private String jobName;
    private Integer jobBuildNumber;
    private Long timeInQueue;
    private String jobBuildURL;
    private int week;
    private int month;
    private int year;

    public DBQueueItem() {
        super();
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public Integer getJobBuildNumber() {
        return jobBuildNumber;
    }

    public void setJobBuildNumber(int jobBuildNumber) {
        this.jobBuildNumber = jobBuildNumber;
    }

    public Long getTimeInQueue() {
        return timeInQueue;
    }

    public void setTimeInQueue(Long timeInQueue) {
        this.timeInQueue = timeInQueue;
    }

    public String getJobBuildURL() {
        return jobBuildURL;
    }

    public void setJobBuildURL(String jobBuildURL) {
        this.jobBuildURL = jobBuildURL;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public Integer getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
