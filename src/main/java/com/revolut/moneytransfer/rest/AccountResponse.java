package com.revolut.moneytransfer.rest;

import java.util.UUID;

public class AccountResponse {

    private UUID id;
    private String holder;
    private int balance;

    public AccountResponse(UUID id, String holder, int balance) {
        this.id = id;
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

    public void setHolder(String holder) {
        this.holder = holder;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }
}
