package com.example.demo.application;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.example.demo.EmailProvider;
import com.example.demo.batch.Tasklet;
import com.example.demo.customer.Customer;
import com.example.demo.customer.CustomerRepository;


@Component
public class DormantBatchTasklet implements Tasklet {
    private final CustomerRepository customerRepository;
    private final EmailProvider emailProvider;

    public DormantBatchTasklet(CustomerRepository customerRepository, EmailProvider emailProvider) {
        this.customerRepository = customerRepository;
        this.emailProvider = emailProvider;
    }

    @Override
    public void execute() {
        int pageNo = 0;
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
    }
}
