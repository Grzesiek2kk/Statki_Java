package com.example.ships.service;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PortApiService
{
    public Map<String, Object> convertStringToMap(String jsonString) {
        Gson gson = new Gson();
        try {
            return gson.fromJson(jsonString, new TypeToken<Map<String, Object>>(){}.getType());
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }
}
