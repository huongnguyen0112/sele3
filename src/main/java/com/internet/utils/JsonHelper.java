package com.internet.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonParseException;
import com.google.gson.stream.JsonReader;
import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.Duration;

@Slf4j
public class JsonHelper {
    /**
     * Reads a JSON file and deserializes it into the requested type.
     *
     * @param jsonFile path to the JSON file
     * @param clazz target class for deserialization
     * @param <T> target object type
     * @return deserialized object
     * @throws RuntimeException when the file does not exist
     */
    public static <T> T fromJsonFile(String jsonFile, Class<T> clazz) {
        log.debug("Loading data from json file {}", jsonFile);
        JsonReader reader;
        try {
            reader = new JsonReader(new FileReader(jsonFile));
        } catch (FileNotFoundException e) {
            log.error(e.getMessage());
            throw new RuntimeException(jsonFile + " does not exist");
        }

        GsonBuilder builder = new GsonBuilder()
                .registerTypeAdapter(Duration.class, new DurationDeserializer());

        Gson gson = builder.create();
        return gson.fromJson(reader, clazz);
    }

    private static class DurationDeserializer implements JsonDeserializer<Duration> {
        @Override
        public Duration deserialize(com.google.gson.JsonElement json,
                                    java.lang.reflect.Type type,
                                    com.google.gson.JsonDeserializationContext context)
                throws JsonParseException {
            if (json.isJsonPrimitive() && json.getAsJsonPrimitive().isNumber()) {
                return Duration.ofMillis(json.getAsLong());
            }
            if (json.isJsonPrimitive() && json.getAsJsonPrimitive().isString()) {
                return Duration.parse(json.getAsString());
            }
            throw new JsonParseException("Duration must be milliseconds or ISO-8601 text");
        }
    }
}
