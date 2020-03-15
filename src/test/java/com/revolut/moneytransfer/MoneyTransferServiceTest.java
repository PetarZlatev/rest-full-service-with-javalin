package com.revolut.moneytransfer;

import com.revolut.moneytransfer.domain.Account;
import com.revolut.moneytransfer.domain.AccountRepository;
import com.revolut.moneytransfer.domain.MoneyTransfer;
import com.revolut.moneytransfer.domain.exception.BankAccountNotFoundException;
import com.revolut.moneytransfer.domain.exception.InsufficientBalanceException;
import com.revolut.moneytransfer.domain.exception.TransferAmountShouldBePositive;
import com.revolut.moneytransfer.service.MoneyTransferService;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;

class MoneyTransferServiceTest {

    private SessionFactory sessionFactory;
    private MoneyTransferService tested;
    private AccountRepository accountRepository;

    @BeforeEach
    void setUpBefore() {
        sessionFactory = HibernateUtil.getSessionFactory();
        accountRepository = new AccountRepository(sessionFactory);
        tested = new MoneyTransferService();
    }

    @AfterEach
    void afterEach() {
        HibernateUtil.shutdown();
    }


    @Test
    void transferAmountShouldBePositive() {

        UUID payerUUID = UUID.fromString("2a0c736c-6220-43fa-b49e-eec65736d491");
        UUID beneficiaryUUID = UUID.fromString("916237b6-6329-11ea-bc55-0242ac130003");
        MoneyTransfer transferInitialization = new MoneyTransfer(payerUUID, beneficiaryUUID, 0);
        assertThrows(TransferAmountShouldBePositive.class, () -> tested.transferMoney(transferInitialization));

    }

    @Test
    void unknownPayerAccountThrowException() {

        UUID payerUUID = UUID.fromString("2a0c736c-6220-43fa-b49e-eec65736d491");
        UUID beneficiaryUUID = UUID.fromString("916237b6-6329-11ea-bc55-0242ac130003");
        MoneyTransfer transferInitialization = new MoneyTransfer(payerUUID, beneficiaryUUID, 10);
        assertThrows(BankAccountNotFoundException.class, () -> tested.transferMoney(transferInitialization));
    }

    @Test
    void unknownBeneficiaryAccountThrowsException() {

        Account payerAccount = accountRepository.createAccount(new Account("Payer", 0));
        UUID payerUUID = payerAccount.getId();
        UUID unknownBeneficiaryId = UUID.fromString("916237b6-6329-11ea-bc55-0242ac130003");
        MoneyTransfer transferInitialization = new MoneyTransfer(payerUUID, unknownBeneficiaryId, 10);

        assertThrows(BankAccountNotFoundException.class, () -> tested.transferMoney(transferInitialization));
    }

    @Test
    void insufficientBalanceExceptionIsThrown() {

        Account payerAccount = accountRepository.createAccount(new Account("Payer", 0));
        Account beneficiaryAccount = accountRepository.createAccount(new Account("Receiver", 0));
        MoneyTransfer transferInitialization = new MoneyTransfer(payerAccount.getId(), beneficiaryAccount.getId(), 10);

        Assertions.assertThrows(InsufficientBalanceException.class, () -> tested.transferMoney(transferInitialization));

    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Test
    void transferMoney() {

        Account payerAccount = accountRepository.createAccount(new Account("Payer", 100));
        Account beneficiaryAccount = accountRepository.createAccount(new Account("Receiver", 0));
        MoneyTransfer transferInitialization = new MoneyTransfer(payerAccount.getId(), beneficiaryAccount.getId(), 10);
        tested.transferMoney(transferInitialization);

        Account payerAccountAfter = accountRepository.findById(payerAccount.getId()).get();
        Account beneficiaryAccountAfter = accountRepository.findById(beneficiaryAccount.getId()).get();

        Assertions.assertEquals(90, payerAccountAfter.getBalance());

        Assertions.assertEquals(10, beneficiaryAccountAfter.getBalance());

    }
}