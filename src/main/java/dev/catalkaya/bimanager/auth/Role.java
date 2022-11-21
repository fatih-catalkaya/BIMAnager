package dev.catalkaya.bimanager.auth;

import io.javalin.security.RouteRole;

public enum Role implements RouteRole {
    ANYONE,
    USER,
    ADMINISTRATOR
}
