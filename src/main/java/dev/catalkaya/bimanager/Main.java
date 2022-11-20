package dev.catalkaya.bimanager;

import dev.catalkaya.bimanager.controller.*;
import io.javalin.Javalin;

public class Main {
    public static void main(String[] args) {
        Javalin app = Javalin.create()
                .put("/room/create", RoomController::create)
                .get("/room/query", RoomController::query)
                .delete("/room/delete", RoomController::delete)
                .put("/person/create", PersonController::create)
                .get("/person/query", PersonController::query)
                .post("/person/update", PersonController::update)
                .delete("/person/delete", PersonController::delete)
                .put("/product/create", ProductController::create)
                .get("/product/query", ProductController::query)
                .post("/product/update", ProductController::update)
                .delete("/product/delete", ProductController::delete)
                .put("/purchase/create", PurchaseController::create)
                .get("/purchase/query", PurchaseController::query)
                .delete("/purchase/delete", PurchaseController::delete)
                .put("/deposit/create", DepositController::create)
                .get("/deposit/query", DepositController::query)
                .delete("/deposit/delete", DepositController::delete)
            .start(8080);
    }
}