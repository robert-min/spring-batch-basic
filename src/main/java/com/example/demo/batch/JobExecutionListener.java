package com.example.demo.batch;

public interface JobExecutionListener {
    void beforeJob(JobExecution jobExecution);

    void afterJob(JobExecution jobExecution);
}
