package com.example.demo.application;

import org.springframework.stereotype.Component;

import com.example.demo.EmailProvider;
import com.example.demo.batch.JobExecution;
import com.example.demo.batch.JobExecutionListener;

@Component
public class DormantBatchJobExecutionListener implements JobExecutionListener {

    private final EmailProvider emailProvider;

    public DormantBatchJobExecutionListener(EmailProvider emailProvider) {
        this.emailProvider = emailProvider;
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {
        System.out.println("DormantBatchJobExecutionListener 실행");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        emailProvider.send("admin@gmail.com", "배치 완료", "DormantBatchJob 완료. status: " + jobExecution.getStatus());
    }
}
