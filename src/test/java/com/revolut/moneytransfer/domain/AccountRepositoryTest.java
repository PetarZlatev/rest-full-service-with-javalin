package com.revolut.moneytransfer.domain;

import com.revolut.moneytransfer.HibernateUtil;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AccountRepositoryTest {

    private SessionFactory session;
    private AccountRepository accountRepo;

    @BeforeEach
    void setUpBefore() {
        session = HibernateUtil.getSessionFactory();
        accountRepo = new AccountRepository(session);
    }

    @AfterEach
    void afterEach() {
        HibernateUtil.shutdown();
    }

    @Test
    void createAccountAndGetId() {
        accountRepo = new AccountRepository(session);
        Account newAccount = accountRepo.createAccount(new Account("Max", 100));

        assertNotNull(newAccount.getId());
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Test
    void findAccountById() {
        AccountRepository accountRepo = new AccountRepository(session);
        Account accountToCreate = new Account("Max", 100);
        accountRepo.createAccount(accountToCreate);

        UUID uuid = accountToCreate.getId();
        Account account = accountRepo.findById(uuid).get();

        assertEquals(account.getId(), uuid);
        assertEquals(account.getHolder(), "Max");
        assertEquals(account.getBalance(), 100);
    }

    @Test
    void findUnknownAccountReturnsEmptyOptional() {

        Optional<Account> optional = accountRepo.findById(UUID.fromString("7c8e239e-a79a-4b70-916a-4e28e41e997e"));

        assertTrue(optional.isEmpty());

    }

}