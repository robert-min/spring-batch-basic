package com.example.demo.customer;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Entity
@NoArgsConstructor
@Getter
@ToString
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String email;
    private LocalDate createdAt;
    private LocalDate loginAt;
    private Status status;

    public Customer(String name, String email) {
        this.name = name;
        this.email = email;
        this.createdAt = LocalDate.now();
        this.loginAt = LocalDate.now();
        this.status = Status.NORMAL;
    }

    public void setLoginAt(LocalDate loginAt) {
        this.loginAt = loginAt;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public enum Status {
        NORMAL,
        DORMANT,
    }
}
