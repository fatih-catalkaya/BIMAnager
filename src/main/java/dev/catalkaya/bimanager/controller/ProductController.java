package dev.catalkaya.bimanager.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.catalkaya.bimanager.JacksonMapper;
import dev.catalkaya.bimanager.model.Product;
import dev.catalkaya.bimanager.repository.ProductRepository;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

public class ProductController {
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    public static void create(Context context){
        try {
            ObjectMapper obm = JacksonMapper.getInstance().getObjectMapper();
            Product input = obm.readValue(context.body(), Product.class);
            Product output = ProductRepository.create(input);
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

    public static void query(Context context){
        try {
            ObjectMapper obm = JacksonMapper.getInstance().getObjectMapper();
            Product input = obm.readValue(context.body(), Product.class);
            List<Product> productList = ProductRepository.query(input);
            if(productList.size() == 1){
                context.status(HttpStatus.OK).json(productList.get(0));
            }
            else{
                context.status(HttpStatus.OK).json(productList);
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
        try {
            ObjectMapper obm = JacksonMapper.getInstance().getObjectMapper();
            Product input = obm.readValue(context.body(), Product.class);
            ProductRepository.update(input);
            context.status(HttpStatus.OK).result("OK");
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
        try {
            ObjectMapper obm = JacksonMapper.getInstance().getObjectMapper();
            Product input = obm.readValue(context.body(), Product.class);
            ProductRepository.delete(input);
            context.status(HttpStatus.OK).result("OK");
        } catch (JsonProcessingException e) {
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
