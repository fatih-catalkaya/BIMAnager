package dev.catalkaya.bimanager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;

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
    }

    public ObjectMapper getObjectMapper() {
        return obm;
    }
}
