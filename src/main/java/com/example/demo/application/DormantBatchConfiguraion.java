package com.example.demo.application;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.demo.batch.Job;

@Configuration
public class DormantBatchConfiguraion {
    
    @Bean
    public Job dormantBatchJob(
        DormantBatchItemReader itemReader,
        DormantBatchItemProcessor itemProcessor,
        DormantBatchItemWriter itemWriter,
        DormantBatchJobExecutionListener jobExecutionListener) {
        return Job.builder()
                .itemReader(itemReader)
                .itemProcessor(itemProcessor)
                .itemWriter(itemWriter)
                .jobExecutionListener(jobExecutionListener)
                .build();
    }
}
