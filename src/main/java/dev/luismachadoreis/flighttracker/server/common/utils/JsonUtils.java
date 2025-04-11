package dev.luismachadoreis.flighttracker.server.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
/**
 * Utility class for JSON serialization and deserialization.
 */
@Component
public class JsonUtils {

    private final ObjectMapper objectMapper;

    public JsonUtils(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Serializes an object to a JSON string.
     * @param object the object to serialize
     * @return the JSON string
     */
    public String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing object to JSON", e);
        }
    }

}