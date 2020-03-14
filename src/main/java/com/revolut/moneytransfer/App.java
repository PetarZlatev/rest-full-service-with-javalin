package com.revolut.moneytransfer;

import com.revolut.moneytransfer.rest.AccountController;
import com.revolut.moneytransfer.rest.CreateAccountRequest;
import io.javalin.Javalin;
import io.javalin.core.validation.Validator;

import java.util.UUID;

import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.post;

public class App {

    private Javalin app;

    public App(AccountController accountController) {
        app = Javalin.create();


        app.routes(() -> path("accounts", () -> {
            post(ctx -> {
                Validator<CreateAccountRequest> validator = ctx.bodyValidator(CreateAccountRequest.class)
                        .check(obj -> !obj.getHolder().isEmpty())
                        .check(obj -> obj.getInitialBalance() > 0);

                CreateAccountRequest request = validator.get();
                ctx.json(accountController.createAccount(request));
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
