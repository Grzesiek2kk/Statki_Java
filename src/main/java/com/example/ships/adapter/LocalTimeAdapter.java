package com.example.ships.config;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class LocalTimeAdapter implements JsonSerializer<LocalTime>, JsonDeserializer<LocalTime> {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Override
    public JsonElement serialize(LocalTime time, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(TIME_FORMATTER.format(time));
    }

    @Override
    public LocalTime deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return LocalTime.parse(json.getAsJsonPrimitive().getAsString(), TIME_FORMATTER);
    }
}

