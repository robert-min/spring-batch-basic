package com.example.demo.application;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.demo.batch.Job;

@Configuration
public class DormantBatchConfiguraion {
    
    @Bean
    public Job dormantBatchJob(DormantBatchTasklet dormantBatchTasklet, DormantBatchJobExecutionListener dormantBatchJobExecutionListener) {
        return new Job(dormantBatchTasklet, dormantBatchJobExecutionListener);
    }
}
