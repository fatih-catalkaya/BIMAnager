package dev.catalkaya.bimanager;

import dev.catalkaya.bimanager.controller.PersonController;
import dev.catalkaya.bimanager.controller.RoomController;
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
            .start(8080);
    }
}