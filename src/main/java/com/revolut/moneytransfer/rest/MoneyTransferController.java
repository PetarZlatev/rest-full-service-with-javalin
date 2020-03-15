package com.revolut.moneytransfer.rest;

import com.revolut.moneytransfer.MoneyTransferService;
import com.revolut.moneytransfer.domain.MoneyTransfer;
import com.revolut.moneytransfer.rest.dto.CreateMoneyTransferRequest;
import com.revolut.moneytransfer.rest.dto.MoneyTransferResponse;


public class MoneyTransferController {

    private final MoneyTransferService transferService;

    public MoneyTransferController(MoneyTransferService transferService) {
        this.transferService = transferService;

    }

    public MoneyTransferResponse transferMoney(CreateMoneyTransferRequest request) {
        MoneyTransfer transfer = transferService.transferMoney(new MoneyTransfer(request.getPayerAccountId(), request.getBeneficiaryAccountId(), request.getAmount()));
        return new MoneyTransferResponse(transfer.getId(), transfer.getPayerAccountId(), transfer.getBeneficiaryAccountId(), transfer.getAmount());
    }
}
