package com.revolut.moneytransfer.rest;

import com.revolut.moneytransfer.domain.Account;
import com.revolut.moneytransfer.domain.AccountRepository;
import com.revolut.moneytransfer.domain.exception.BankAccountNotFoundException;
import com.revolut.moneytransfer.rest.dto.AccountResponse;
import com.revolut.moneytransfer.rest.dto.CreateAccountRequest;

import java.util.Optional;
import java.util.UUID;

public class AccountController {

    private final AccountRepository repository;

    public AccountController(AccountRepository repository) {
        this.repository = repository;
    }

    public AccountResponse createAccount(CreateAccountRequest request) {
        Account account = repository.createAccount(new Account(
                request.getHolder(),
                request.getInitialBalance()));
        return new AccountResponse(
                account.getId(),
                account.getHolder(),
                account.getBalance());
    }

    public AccountResponse findById(UUID id) {

        Optional<Account> optionalOfAccount = repository.findById(id);
        if (optionalOfAccount.isEmpty()) {
            throw new BankAccountNotFoundException(id);
        }
        Account account = optionalOfAccount.get();
        return new AccountResponse(
                account.getId(),
                account.getHolder(),
                account.getBalance());

    }
}
