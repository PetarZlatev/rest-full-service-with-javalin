package com.revolut.moneytransfer;

import com.revolut.moneytransfer.domain.exception.BankAccountNotFoundException;
import com.revolut.moneytransfer.domain.exception.InsufficientBalanceException;
import com.revolut.moneytransfer.rest.AccountController;
import com.revolut.moneytransfer.rest.ErrorResponse;
import com.revolut.moneytransfer.rest.MoneyTransferController;
import com.revolut.moneytransfer.rest.dto.CreateAccountRequest;
import com.revolut.moneytransfer.rest.dto.CreateMoneyTransferRequest;
import com.revolut.moneytransfer.service.PersistenceConflictException;
import io.javalin.Javalin;
import io.javalin.core.validation.Validator;

import java.util.UUID;

import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.post;

public class App {

    private final Javalin app;

    public App(AccountController accountController, MoneyTransferController transferController) {

        app = Javalin.create();
        // handlers
        app.routes(() -> path("accounts", () -> {
            post(ctx -> {
                Validator<CreateAccountRequest> body = ctx.bodyValidator(CreateAccountRequest.class);
                CreateAccountRequest createAccountRequest = ctx.bodyValidator(CreateAccountRequest.class)
                        .check(obj -> obj.getHolder() != null && !obj.getHolder().isBlank())
                        .check(obj -> obj.getInitialBalance() > 0, "the amount of transfer should be positive")
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

        // Error Mapping
        app.exception(BankAccountNotFoundException.class, (e, ctx) -> {
            ctx.status(404);
            ctx.json(new ErrorResponse(e.getMessage(), 404));
        });
        app.exception(InsufficientBalanceException.class, (e, ctx) -> {
            ctx.status(400);
            ctx.json(new ErrorResponse(e.getMessage(), 400));
        });

        app.exception(PersistenceConflictException.class, (e, ctx) -> ctx.status(409));

    }

    public void start(int port) {
        app.start(port);
    }

    public void stop() {
        HibernateUtil.shutdown();
        app.stop();
    }
}