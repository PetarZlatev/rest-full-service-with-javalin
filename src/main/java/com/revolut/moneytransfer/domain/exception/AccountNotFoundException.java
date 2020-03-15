package com.revolut.moneytransfer.domain.exception;

import java.util.UUID;

import static java.lang.String.format;

public class AccountNotFoundException extends RuntimeException {

    public AccountNotFoundException(UUID accountId) {
        super(format("There is no account with the id:'%s'", accountId.toString()));
    }
}
