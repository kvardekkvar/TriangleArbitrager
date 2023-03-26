package org.example.util;

import com.fasterxml.jackson.databind.JsonNode;

public interface JSONHandler {

    public <T> String toJSON(T pojo);

    public <T> T fromJSON(String message, Class<T> classname);

    JsonNode readEntireJSON(String message);
}
