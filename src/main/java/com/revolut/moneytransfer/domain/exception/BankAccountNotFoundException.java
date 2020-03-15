package com.revolut.moneytransfer.domain.exception;

import java.util.UUID;

import static java.lang.String.format;

public class BankAccountNotFoundException extends RuntimeException {

    public BankAccountNotFoundException(UUID accountId) {
        super(format("There is no account with the id:'%s'", accountId.toString()));
    }
}
