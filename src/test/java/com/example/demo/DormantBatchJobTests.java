package com.example.demo;

import java.time.LocalDate;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.batch.BatchStatus;
import com.example.demo.batch.Job;
import com.example.demo.batch.JobExecution;
import com.example.demo.batch.TaskletJob;
import com.example.demo.customer.Customer;
import com.example.demo.customer.CustomerRepository;

@SpringBootTest
public class DormantBatchJobTests {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TaskletJob dormantBatchJob;

    @BeforeEach
    public void setUp() {
        customerRepository.deleteAll();
    }

    @Test
    @DisplayName("로그인 시간이 일년을 경과한 고객이 세명이고, 일년 이내에 로그인한 고객이 5명이면 3명의 고객이 휴먼전환 대상")
    void test1() {
        // given
        saveCustomer(376);
        saveCustomer(376);
        saveCustomer(376);
    
        saveCustomer(364);
        saveCustomer(363);
        saveCustomer(362);
        saveCustomer(361);
        saveCustomer(360);

        // when
        final JobExecution jobExecution = dormantBatchJob.execute();

        // then
        final long dormantCount = customerRepository.findAll().stream().filter(it -> it.getStatus() == Customer.Status.DORMANT).count();
        Assertions.assertThat(dormantCount).isEqualTo(3);
        Assertions.assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
    }

    @Test
    @DisplayName("고객이 5명이 있지만, 모두 다 휴면 전환 대상이 아니면 휴먼 전환 대상은 0명이다.")
    void test2() {
        // given
        saveCustomer(340);
        saveCustomer(340);
        saveCustomer(340);
        saveCustomer(340);
        saveCustomer(340);

        // when
        final JobExecution jobExecution = dormantBatchJob.execute();

        // then
        final long dormantCount = customerRepository.findAll().stream().filter(it -> it.getStatus() == Customer.Status.DORMANT).count();
        Assertions.assertThat(dormantCount).isEqualTo(0);
        Assertions.assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
    }

    @Test
    @DisplayName("고객이 없는 경우에도 배치는 정상동작해야 한다.")
    void test3() {
        // when
        final JobExecution jobExecution = dormantBatchJob.execute();

        // then
        final long dormantCount = customerRepository.findAll().stream().filter(it -> it.getStatus() == Customer.Status.DORMANT).count();
        Assertions.assertThat(dormantCount).isEqualTo(0);
        Assertions.assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
    }

    @Test
    @DisplayName("배치가 실패하면 BatchStatus는 FAILED를 반환")
    void test4() {
        // given
        final TaskletJob dormantBatchJob = new Job(null, null);

        // when
        final JobExecution jobExecution = dormantBatchJob.execute();

        // then
        Assertions.assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.FAILED);
    }

    private void saveCustomer(long loginMinusDays) {
        final String uuid = UUID.randomUUID().toString();
        final Customer customer1 = new Customer(uuid, uuid + "@gmail.com");
        customer1.setLoginAt(LocalDate.now().minusDays(loginMinusDays));
        customerRepository.save(customer1);
    }

}
