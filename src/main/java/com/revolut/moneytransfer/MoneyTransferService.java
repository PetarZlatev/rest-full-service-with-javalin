package com.revolut.moneytransfer;

import com.revolut.moneytransfer.domain.Account;
import com.revolut.moneytransfer.domain.AccountRepository;
import com.revolut.moneytransfer.domain.MoneyTransfer;
import com.revolut.moneytransfer.domain.exception.AccountNotFoundException;
import com.revolut.moneytransfer.domain.exception.InsufficientBalanceException;
import com.revolut.moneytransfer.domain.exception.TransferAmountShouldBePositive;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.OptimisticLockException;
import java.util.Optional;

public class MoneyTransferService {

    private AccountRepository accountRepository;
    private SessionFactory sessionFactory = HibernateUtil.getSessionFactory();


    public MoneyTransferService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public void transferMoney(MoneyTransfer transfer) {

        if (transfer.getAmount() < 1) {
            throw new TransferAmountShouldBePositive();
        }

        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        Optional<Account> optionalOfPayer = accountRepository.findById(transfer.getPayerAccountId());
        if (optionalOfPayer.isEmpty()) {
            transaction.rollback();
            throw new AccountNotFoundException(transfer.getPayerAccountId());
        }

        Optional<Account> optionalOfBeneficiary = accountRepository.findById(transfer.getBeneficiaryAccountId());
        if (optionalOfBeneficiary.isEmpty()) {
            transaction.rollback();
            throw new AccountNotFoundException(transfer.getBeneficiaryAccountId());
        }

        Account payerAccount = optionalOfPayer.get();

        if (payerAccount.getBalance() < transfer.getAmount()) {
            transaction.rollback();
            throw new InsufficientBalanceException("Insufficient balance exception");
        }

        Account beneficiaryAccount = optionalOfBeneficiary.get();
        payerAccount.setBalance(payerAccount.getBalance() - transfer.getAmount());
        beneficiaryAccount.setBalance(beneficiaryAccount.getBalance() + transfer.getAmount());

        try {
            session.merge(payerAccount);
            session.merge(beneficiaryAccount);
            session.save(transfer);
            session.getTransaction().commit();
        } catch (OptimisticLockException e) {
            transaction.rollback();
            e.printStackTrace();
            throw new RuntimeException();
        }

    }
}
