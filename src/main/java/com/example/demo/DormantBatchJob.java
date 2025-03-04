package com.example.demo;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.example.demo.batch.BatchStatus;
import com.example.demo.batch.JobExecution;
import com.example.demo.customer.Customer;
import com.example.demo.customer.CustomerRepository;

@Component
public class DormantBatchJob {
    private final CustomerRepository customerRepository;
    private final EmailProvider emailProvider;

    public DormantBatchJob(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
        this.emailProvider = new EmailProvider.Fake();
    }

    public JobExecution execute() {
        final JobExecution jobExecution = new JobExecution();
        jobExecution.setStatus(BatchStatus.STARTING);
        jobExecution.setStartTime(LocalDateTime.now());

        int pageNo = 0;
        try {
            while (true) {
                // 1. 유저 조회
                final PageRequest pageRequest = PageRequest.of(pageNo, 1, Sort.by("id").ascending());
                final Page<Customer> page = customerRepository.findAll(pageRequest);
                
                final Customer customer;
                if (page.isEmpty()) {
                    break;
                } else {
                    pageNo++;
                    customer = page.getContent().get(0);
                }
    
                // 2. 휴면먼 계정 대상 추출 및 변환
                final boolean isDormantTarget = LocalDate.now().minusDays(365).isAfter(customer.getLoginAt());
                if (isDormantTarget) {
                    customer.setStatus(Customer.Status.DORMANT);
                } else {
                    continue;
                }
    
                // 3. 휴면 계정 변환
                customerRepository.save(customer);
    
                // 4. 이메일 발송
                emailProvider.send(customer.getEmail(), "휴면 계정 변환 완료", "휴면 계정 변환 완료");
            }
            jobExecution.setStatus(BatchStatus.COMPLETED);
        } catch (Exception e) {
            jobExecution.setStatus(BatchStatus.FAILED);
        } finally {
            jobExecution.setEndTime(LocalDateTime.now());
            emailProvider.send("admin@gmail.com", "배치 완료", "DormantBatchJob 완료. status: " + jobExecution.getStatus());
        }
        return jobExecution;
    }
}
