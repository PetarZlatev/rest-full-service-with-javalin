package com.revolut.moneytransfer.rest;

import java.util.UUID;

public class MoneyTransferResponse {

    private UUID id;
    private UUID payerAccountId;
    private UUID beneficiaryAccountId;
    private int amount;

    public MoneyTransferResponse(UUID id, UUID payerAccountId, UUID beneficiaryAccountId, int amount) {
        this.id = id;
        this.payerAccountId = payerAccountId;
        this.beneficiaryAccountId = beneficiaryAccountId;
        this.amount = amount;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

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
