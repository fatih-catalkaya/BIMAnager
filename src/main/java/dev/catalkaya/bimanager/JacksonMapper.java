package dev.catalkaya.bimanager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class JacksonMapper {
    private static JacksonMapper instance;
    private final ObjectMapper obm;

    public static JacksonMapper getInstance() {
        if(instance == null){
            instance = new JacksonMapper();
        }
        return instance;
    }

    private JacksonMapper(){
        this.obm = new ObjectMapper();
        this.obm.registerModule(new Jdk8Module());
        this.obm.registerModule(new JavaTimeModule());
        this.obm.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public ObjectMapper getObjectMapper() {
        return obm;
    }
}
