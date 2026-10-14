package com.internet.utils;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import lombok.extern.slf4j.Slf4j;

/**
 * Converts JSON duration values into {@link Duration} objects.
 * Supports both millisecond numbers and ISO-8601 duration strings,
 * so configuration values can be represented consistently in test settings.
 */
import java.lang.reflect.Type;
import java.time.Duration;

@Slf4j
public class DurationDeserializer implements JsonDeserializer<Duration> {
    @Override
    public Duration deserialize(JsonElement json,
                                Type type,
                                JsonDeserializationContext context)
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
