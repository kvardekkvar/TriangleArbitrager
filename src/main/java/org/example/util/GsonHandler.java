package org.example.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonHandler implements JSONHandler {
    Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();


    public <T> String toJSON(T pojo){
        return gson.toJson(pojo);
    }
    public <T> T fromJSON(String message, Class<T> classname){
        return gson.fromJson(message, classname);
    }

    @Override
    public JsonNode readEntireJSON(String message) {
        return null;
    }
}
