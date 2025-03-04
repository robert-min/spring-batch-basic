package com.example.demo;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
public interface EmailProvider {
    
    void send(String emialAddress, String title, String body);

    @Slf4j
    @Component
    class Fake implements EmailProvider {
        @Override
        public void send(String emialAddress, String title, String body) {
            log.info("{} email 전송 완료! {} : {}", emialAddress, title, body);
        }
    }
    
}
