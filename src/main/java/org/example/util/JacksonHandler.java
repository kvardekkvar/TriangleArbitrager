package org.example.util;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonHandler implements JSONHandler {

    ObjectMapper mapper = new ObjectMapper();

    public JsonNode readEntireJSON(String message){
        try {
            return mapper.readTree(message);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public <T> String toJSON(T pojo){
        try {
            return mapper.writeValueAsString(pojo);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public <T> T fromJSON(String message, Class<T> classname){
        try {
            return mapper.readValue(message, classname);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
