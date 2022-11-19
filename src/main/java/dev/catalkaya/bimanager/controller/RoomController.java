package dev.catalkaya.bimanager.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import dev.catalkaya.bimanager.JacksonMapper;
import dev.catalkaya.bimanager.model.Room;
import dev.catalkaya.bimanager.repository.RoomRepository;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.UUID;

public class RoomController {
    private static final Logger logger = LoggerFactory.getLogger(RoomController.class);

    public static void create(Context context){
        try {
            ObjectMapper obm = JacksonMapper.getInstance().getObjectMapper();
            Room input = obm.readValue(context.body(), Room.class);

            Room output = RoomRepository.create(input);

            output.setRoomId(UUID.randomUUID().toString());
            obm.writeValueAsString(output);

            context.status(200).json(output);
        } catch (JsonProcessingException e) {
            logger.info("Received invalid JSON: {}", context.body());
            logger.info(e.getMessage());
            context.status(HttpStatus.BAD_REQUEST).result("Bad Request");
        }
        catch (SQLException e){
            logger.info("A database exception occured!");
            logger.info(e.getMessage());
            context.status(HttpStatus.BAD_REQUEST).result("Bad Request");
        }
    }
}
