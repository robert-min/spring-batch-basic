package com.example.demo.application;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import com.example.demo.batch.ItemProcessor;
import com.example.demo.customer.Customer;

@Component
public class DormantBatchItemProcessor implements ItemProcessor<Customer, Customer> {

    @Override
    public Customer process(Customer item) {
        final boolean isDormantTarget = LocalDate.now().minusDays(365).isAfter(item.getLoginAt());
        if (isDormantTarget) {
            item.setStatus(Customer.Status.DORMANT);
            return item;
        } else {
            return null;
        }
    }
}
