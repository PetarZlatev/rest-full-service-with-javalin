package com.revolut.moneytransfer.rest;

public class CreateAccountRequest {
    private String holder;
    private int initialBalance = 10;

    public String getHolder() {
        return holder;
    }

    public void setHolder(String holder) {
        this.holder = holder;
    }

    public int getInitialBalance() {
        return initialBalance;
    }

    public void setInitialBalance(int initialBalance) {
        this.initialBalance = initialBalance;
    }
}
