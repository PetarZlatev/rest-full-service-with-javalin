package com.revolut.moneytransfer;

import com.revolut.moneytransfer.domain.AccountRepository;
import com.revolut.moneytransfer.rest.AccountController;

public class AppStarter {

    public static void main(String[] args) {
        AccountRepository accountRepo = new AccountRepository(HibernateUtil.getSessionFactory());
        AccountController accountController = new AccountController(accountRepo);
        App app = new App(accountController);
        app.start(8080);
    }
}
