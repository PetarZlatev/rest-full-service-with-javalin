package com.revolut.moneytransfer;

import com.revolut.moneytransfer.rest.AccountController;
import com.revolut.moneytransfer.rest.CreateAccountRequest;
import com.revolut.moneytransfer.rest.CreateMoneyTransferRequest;
import com.revolut.moneytransfer.rest.MoneyTransferController;
import io.javalin.Javalin;

import java.util.UUID;

import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.post;

public class App {

    private final Javalin app;

    public App(AccountController accountController, MoneyTransferController transferController) {
        app = Javalin.create();

        app.routes(() -> path("accounts", () -> {
            post(ctx -> {
                CreateAccountRequest createAccountRequest = ctx.bodyValidator(CreateAccountRequest.class)
                        .check(obj -> !obj.getHolder().isEmpty())
                        .check(obj -> obj.getInitialBalance() > 0)
                        .get();
                ctx.json(accountController.createAccount(createAccountRequest));
                ctx.status(201);
            });
            path("/:id", () -> get(ctx -> {
                String id = ctx.pathParam("id");
                ctx.json(accountController.findById(UUID.fromString(id)));
                ctx.status(200);
            }));
        }));

        app.routes(() -> path("transfers", () -> {
            post(ctx -> {
                CreateMoneyTransferRequest createTransferRequest = ctx.bodyValidator(CreateMoneyTransferRequest.class)
                        .check(obj -> obj.getAmount() > 0)
                        .get();
                ctx.json(transferController.transferMoney(createTransferRequest));
                ctx.status(201);
            });
            path("/:id", () -> get(ctx -> {
                String id = ctx.pathParam("id");
                ctx.json(accountController.findById(UUID.fromString(id)));
                ctx.status(200);
            }));
        }));
    }

    public void start(int port) {
        app.start(port);
    }

    public void stop() {
        HibernateUtil.shutdown();
        app.stop();
    }
}
