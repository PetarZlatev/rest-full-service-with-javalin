package com.revolut.moneytransfer.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

@Entity
public class MoneyTransfer {

    @Id
    @GeneratedValue
    private UUID id;
    private UUID payerAccountId;
    private UUID beneficiaryAccountId;
    private Integer amount;

    public MoneyTransfer() {
    }

    public MoneyTransfer(UUID payerAccountId, UUID beneficiaryAccount, Integer amount) {

        this.payerAccountId = payerAccountId;
        this.beneficiaryAccountId = beneficiaryAccount;
        this.amount = amount;
    }

    public UUID getId() {
        return id;
    }

    public UUID getPayerAccountId() {
        return payerAccountId;
    }

    public UUID getBeneficiaryAccountId() {
        return beneficiaryAccountId;
    }

    public Integer getAmount() {
        return amount;
    }
}
