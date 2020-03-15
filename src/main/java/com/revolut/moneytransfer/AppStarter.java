package com.revolut.moneytransfer;

import com.revolut.moneytransfer.domain.AccountRepository;
import com.revolut.moneytransfer.rest.AccountController;
import com.revolut.moneytransfer.rest.MoneyTransferController;
import com.revolut.moneytransfer.service.MoneyTransferService;

public class AppStarter {

    public static void main(String[] args) {
        AccountRepository accountRepo = new AccountRepository(HibernateUtil.getSessionFactory());
        AccountController accountController = new AccountController(accountRepo);
        MoneyTransferService transferService = new MoneyTransferService();
        MoneyTransferController transferController = new MoneyTransferController(transferService);
        App app = new App(accountController, transferController);
        app.start(8080);
    }
}
