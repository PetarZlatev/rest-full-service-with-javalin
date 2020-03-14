package com.revolut.moneytransfer.domain;

import com.revolut.moneytransfer.HibernateUtil;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AccountRepositoryTest {

    private SessionFactory session;

    @BeforeEach
    void setUpBefore() {
        session = HibernateUtil.getSessionFactory();
    }


    @AfterEach
    void afterEach() {
        HibernateUtil.shutdown();
    }


    @Test
    void createAccountAndGetId() {
        AccountRepository accountRepo = new AccountRepository(session);
        Account newAccount = accountRepo.createAccount(new Account("Max", 100));
        Assertions.assertNotNull(newAccount.getId());
    }
}