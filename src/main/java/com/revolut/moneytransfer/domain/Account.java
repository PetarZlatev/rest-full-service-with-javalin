package com.revolut.moneytransfer.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Version;
import java.util.UUID;

@Entity
public class Account {

    @Id
    @GeneratedValue
    private UUID id;

    @Version
    private int version;

    private String holder;

    private int balance;

    public Account() {
    }

    public Account(String holder, int balance) {
        this.holder = holder;
        this.balance = balance;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getHolder() {
        return holder;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }
}


