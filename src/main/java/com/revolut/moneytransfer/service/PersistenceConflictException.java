package com.revolut.moneytransfer.service;

public class PersistenceConflictException extends RuntimeException {

    public PersistenceConflictException() {
        super("unable to transfer money due to a conflict during persisting");
    }
}
