package dev.catalkaya.bimanager.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.catalkaya.bimanager.JacksonMapper;
import dev.catalkaya.bimanager.model.Person;
import dev.catalkaya.bimanager.repository.AuthRepository;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    public static void auth(Context context){
        try{
            ObjectMapper obm = JacksonMapper.getInstance().getObjectMapper();
            Person person = obm.readValue(context.body(), Person.class);
            if(AuthRepository.isValid(person)){
                AuthRepository.deleteSessionId(context.req().getSession().getId());
                String sessionId = context.req().changeSessionId();
                AuthRepository.registerSessionId(sessionId, person.getPersonEmail());
                context
                    .status(HttpStatus.OK)
                    .header("Content-Type", "application/json")
                    .result("{ \"auth_token\": \"%s\" }".formatted(sessionId));
            }
            else{
                context.status(HttpStatus.BAD_REQUEST).result("Bad Request");
            }
        } catch (JsonProcessingException e) {
            logJsonProcessingException(e, context.body());
            context.status(HttpStatus.BAD_REQUEST).result("Bad Request");
        }
        catch (SQLException e){
            logSQLException(e);
            context.status(HttpStatus.BAD_REQUEST).result("Bad Request");
        }
        catch (NoSuchAlgorithmException e){
            logger.error("Could not hash user password!");
            logger.error(e.getMessage());
            context.status(HttpStatus.INTERNAL_SERVER_ERROR).result("Internal server error");
        }
    }

    public static void logout(Context context) {
        try{
            AuthRepository.deleteSessionId(context.req().getSession().getId());
            context.req().changeSessionId();
            context.status(HttpStatus.OK).result("OK");
        }
        catch (SQLException e){
            logSQLException(e);
            context.status(HttpStatus.BAD_REQUEST).result("Bad Request");
        }
    }

    private static void logJsonProcessingException(JsonProcessingException e, String msgBody){
        logger.info("Received invalid JSON: {}", msgBody);
        logger.info(e.getMessage());
    }

    private static void logSQLException(SQLException e){
        logger.info("A database exception occured!");
        logger.info(e.getMessage());
    }
}
