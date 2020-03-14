package com.revolut.moneytransfer;

import io.javalin.Javalin;

public class App {

    private Javalin app;

    public void start(int port) {
        app = Javalin.create().start(port);
        app.get("/hello", ctx -> ctx.result("Hello World"));
    }

    public void stop() {
        if (app == null) return;
        app.stop();
    }
}
