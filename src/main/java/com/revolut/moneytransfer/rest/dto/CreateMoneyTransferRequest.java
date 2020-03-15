package com.revolut.moneytransfer.rest.dto;

import java.util.UUID;

public class CreateMoneyTransferRequest {

    private UUID payerAccountId;
    private UUID beneficiaryAccountId;
    private int amount;

    public UUID getPayerAccountId() {
        return payerAccountId;
    }

    public void setPayerAccountId(UUID payerAccountId) {
        this.payerAccountId = payerAccountId;
    }

    public UUID getBeneficiaryAccountId() {
        return beneficiaryAccountId;
    }

    public void setBeneficiaryAccountId(UUID beneficiaryAccountId) {
        this.beneficiaryAccountId = beneficiaryAccountId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
