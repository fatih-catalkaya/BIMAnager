package dev.catalkaya.bimanager.auth;

import dev.catalkaya.bimanager.model.Person;
import dev.catalkaya.bimanager.repository.AuthRepository;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.security.RouteRole;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.Set;

public class AccessManager implements io.javalin.security.AccessManager {
    @Override
    public void manage(@NotNull Handler handler, @NotNull Context context, @NotNull Set<? extends RouteRole> routeRoles) throws Exception {
        Optional<Person> optionalPerson = AuthRepository.getPersonBySessionId(context.req().getSession().getId());
        if(optionalPerson.isEmpty()){
            if(routeRoles.contains(Role.ANYONE)){
                handler.handle(context);
                return;
            }
            context.status(401).result("Unauthorized");
        }
        else{
            Person person = optionalPerson.get();
            if(person.isPersonIsAdministrator()){
                handler.handle(context);
                return;
            }

            if(routeRoles.contains(Role.ANYONE) || routeRoles.contains(Role.USER)){
                handler.handle(context);
                return;
            }

            context.status(401).result("Unauthorized");
        }
    }
}
