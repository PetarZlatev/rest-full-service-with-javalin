package com.revolut.moneytransfer.domain;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.Optional;
import java.util.UUID;

public class AccountRepository {
    private final SessionFactory sessionFactory;

    public AccountRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Account createAccount(final Account newAccount) {

        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(newAccount);
        session.getTransaction().commit();
        session.close();

        return newAccount;
    }

    public Optional<Account> findById(UUID id) {
        Session session = sessionFactory.openSession();
        return Optional.ofNullable(session.find(Account.class, id));
    }
}
