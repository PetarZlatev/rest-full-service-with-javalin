package com.revolut.moneytransfer;

import com.revolut.moneytransfer.domain.Account;
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

    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    public MoneyTransferService() {
    }

    public MoneyTransfer transferMoney(final MoneyTransfer transfer) {

        if (transfer.getAmount() < 1) {
            throw new TransferAmountShouldBePositive();
        }

        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        Optional<Account> optionalOfPayer = Optional.ofNullable(session.find(Account.class, transfer.getPayerAccountId()));
        if (optionalOfPayer.isEmpty()) {
            transaction.rollback();
            throw new AccountNotFoundException(transfer.getPayerAccountId());
        }

        Optional<Account> optionalOfBeneficiary = Optional.ofNullable(session.find(Account.class, transfer.getBeneficiaryAccountId()));
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
            return transfer;
        } catch (OptimisticLockException e) {
            transaction.rollback();
            e.printStackTrace();
            throw new RuntimeException();
        }

    }
}
