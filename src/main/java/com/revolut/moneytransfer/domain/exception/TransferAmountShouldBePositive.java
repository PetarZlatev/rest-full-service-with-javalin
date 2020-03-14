package com.revolut.moneytransfer.domain.exception;

public class TransferAmountShouldBePositive extends RuntimeException {
    public TransferAmountShouldBePositive() {
        super("the amount of transfer should be positive");
    }
}
