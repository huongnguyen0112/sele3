package com.internet.utils;

import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;
import java.io.FileReader;

@Slf4j
public class JsonHelper {
    public static <T> T fromJsonFile(String jsonFile, Class<T> clazz) {
        log.debug("Loading data from json file {}", jsonFile);
        JsonReader reader;
        try {
            reader = new JsonReader(new FileReader(jsonFile));
        } catch (FileNotFoundException e) {
            log.error(e.getMessage());
            throw new RuntimeException(jsonFile + "does not exist");
        }

        GsonBuilder builder = new GsonBuilder();

        return builder.create().fromJson(reader, clazz);
    }
}
