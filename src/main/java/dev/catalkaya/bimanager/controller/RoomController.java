package dev.catalkaya.bimanager.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.catalkaya.bimanager.JacksonMapper;
import dev.catalkaya.bimanager.model.Room;
import dev.catalkaya.bimanager.repository.RoomRepository;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

public class RoomController {
    private static final Logger logger = LoggerFactory.getLogger(RoomController.class);

    public static void create(Context context){
        try {
            ObjectMapper obm = JacksonMapper.getInstance().getObjectMapper();
            Room input = obm.readValue(context.body(), Room.class);
            Room output = RoomRepository.create(input);
            context.status(HttpStatus.OK).json(output);
        } catch (JsonProcessingException e) {
            logJsonProcessingException(e, context.body());
            context.status(HttpStatus.BAD_REQUEST).result("Bad Request");
        }
        catch (SQLException e){
            logSQLException(e);
            context.status(HttpStatus.BAD_REQUEST).result("Bad Request");
        }
    }


    public static void delete(Context context){
        try{
            ObjectMapper obm = JacksonMapper.getInstance().getObjectMapper();
            Room input = obm.readValue(context.body(), Room.class);

            RoomRepository.delete(input);

            context.status(HttpStatus.OK).result("OK");
        }
        catch (JsonProcessingException e){
            logJsonProcessingException(e, context.body());
            context.status(HttpStatus.BAD_REQUEST).result("Bad Request");
        }
        catch (SQLException e){
            logSQLException(e);
            context.status(HttpStatus.BAD_REQUEST).result("Bad Request");
        }
    }

    public static void query(Context context){
        try{
            ObjectMapper obm = JacksonMapper.getInstance().getObjectMapper();
            List<Room> rooms = RoomRepository.findAll();
            context.status(HttpStatus.OK).result(obm.writeValueAsString(rooms));
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
