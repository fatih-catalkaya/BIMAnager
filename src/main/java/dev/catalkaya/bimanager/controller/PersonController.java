package dev.catalkaya.bimanager.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.catalkaya.bimanager.JacksonMapper;
import dev.catalkaya.bimanager.Utils;
import dev.catalkaya.bimanager.model.Person;
import dev.catalkaya.bimanager.repository.PersonRepository;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.List;

public class PersonController {
    private static final Logger logger = LoggerFactory.getLogger(RoomController.class);

    public static void create(Context context){
        try{
            ObjectMapper obm = JacksonMapper.getInstance().getObjectMapper();
            Person input = obm.readValue(context.body(), Person.class);
            Person output = PersonRepository.create(input);
            context.status(HttpStatus.OK).json(obm.writeValueAsString(output));
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


    public static void query(Context context){
        try{
            ObjectMapper obm = JacksonMapper.getInstance().getObjectMapper();
            Person input = obm.readValue(context.body(), Person.class);
            List<Person> personList = PersonRepository.query(input);
            if(personList.size() == 1){
                context.status(200).result(obm.writeValueAsString(personList.get(0)));
            }
            else{
                context.status(200).result(obm.writeValueAsString(personList));
            }
        } catch (JsonProcessingException e) {
            logJsonProcessingException(e, context.body());
            context.status(HttpStatus.BAD_REQUEST).result("Bad Request");
        }
        catch (SQLException e){
            logSQLException(e);
            context.status(HttpStatus.BAD_REQUEST).result("Bad Request");
        }
    }


    public static void update(Context context){
        try{
            ObjectMapper obm = JacksonMapper.getInstance().getObjectMapper();
            Person input = obm.readValue(context.body(), Person.class);
            PersonRepository.update(input);
            context.status(HttpStatus.OK).result("");
        } catch (JsonProcessingException e) {
            logJsonProcessingException(e, context.body());
            context.status(HttpStatus.BAD_REQUEST).result("Bad Request");
        } catch (SQLException e){
            logSQLException(e);
            context.status(HttpStatus.BAD_REQUEST).result("Bad Request");
        }
        catch (NoSuchAlgorithmException e){
            logger.error("Could not hash user password!");
            logger.error(e.getMessage());
            context.status(HttpStatus.INTERNAL_SERVER_ERROR).result("Internal server error");
        }
    }


    public static void delete(Context context) {
        try{
            ObjectMapper obm = JacksonMapper.getInstance().getObjectMapper();
            Person input = obm.readValue(context.body(), Person.class);
            if(input.getPersonId().equalsIgnoreCase(Utils.ADMINISTRATOR_PERSON_ID)){ // We don't allow the main admin to be deleted
                context.status(HttpStatus.BAD_REQUEST).result("Bad Request");
            }
            else{
                PersonRepository.delete(input);
                context.status(HttpStatus.OK).result("OK");
            }
        }
        catch (JsonProcessingException e) {
            logJsonProcessingException(e, context.body());
            context.status(HttpStatus.BAD_REQUEST).result("Bad Request");
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
