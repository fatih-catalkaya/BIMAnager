package dev.catalkaya.bimanager;

import dev.catalkaya.bimanager.auth.AccessManager;
import dev.catalkaya.bimanager.auth.Role;
import dev.catalkaya.bimanager.controller.*;
import io.javalin.Javalin;

public class Main {
    public static void main(String[] args) {

        Javalin app = Javalin.create(config -> {
                    config.accessManager(new AccessManager());
                    config.jetty.sessionHandler(SessionUtil::fileSessionHandler);
                })
                .put("/room/create", RoomController::create, Role.ADMINISTRATOR)
                .get("/room/query", RoomController::query, Role.ADMINISTRATOR)
                .delete("/room/delete", RoomController::delete, Role.ADMINISTRATOR)
                .put("/person/create", PersonController::create, Role.ADMINISTRATOR)
                .get("/person/query", PersonController::query, Role.ADMINISTRATOR)
                .post("/person/update", PersonController::update, Role.ADMINISTRATOR)
                .delete("/person/delete", PersonController::delete, Role.ADMINISTRATOR)
                .put("/product/create", ProductController::create, Role.ADMINISTRATOR)
                .get("/product/query", ProductController::query, Role.USER)
                .post("/product/update", ProductController::update, Role.ADMINISTRATOR)
                .delete("/product/delete", ProductController::delete, Role.ADMINISTRATOR)
                .put("/purchase/create", PurchaseController::create, Role.ADMINISTRATOR)
                .get("/purchase/query", PurchaseController::query, Role.ADMINISTRATOR)
                .delete("/purchase/delete", PurchaseController::delete, Role.ADMINISTRATOR)
                .put("/deposit/create", DepositController::create, Role.ADMINISTRATOR)
                .get("/deposit/query", DepositController::query, Role.USER)
                .delete("/deposit/delete", DepositController::delete, Role.ADMINISTRATOR)
                .post("/auth", AuthController::auth, Role.ANYONE)
                .get("/logout", AuthController::logout, Role.USER)
            .start(8080);
    }
}