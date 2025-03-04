package com.example.demo.application;

import org.springframework.stereotype.Component;

import com.example.demo.EmailProvider;
import com.example.demo.batch.ItemWriter;
import com.example.demo.customer.Customer;
import com.example.demo.customer.CustomerRepository;

@Component
public class DormantBatchItemWriter implements ItemWriter<Customer> {

    private final CustomerRepository customerRepository;
    private final EmailProvider emailProvider;

    public DormantBatchItemWriter(CustomerRepository customerRepository, EmailProvider emailProvider) {
        this.customerRepository = customerRepository;
        this.emailProvider = emailProvider;
    }

    @Override
    public void write(Customer item) {
        customerRepository.save(item);
        emailProvider.send(item.getEmail(), "휴면 계정 변환 완료", "휴면 계정 변환 완료");
    }
}