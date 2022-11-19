package dev.catalkaya.bimanager;

import dev.catalkaya.bimanager.controller.RoomController;
import io.javalin.Javalin;

public class Main {
    public static void main(String[] args) {
        Javalin app = Javalin.create()
                .put("/room/create", RoomController::create)
                .start(8080);
    }
}