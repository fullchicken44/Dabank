package com.quan.bank.dabank.utils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class JsonUtils {
    private static final Gson GSON = new Gson();

    // Json to formatted string
    public static String toJson(Object obj) {
        return GSON.toJson(obj);
    }

    // Check for valid JSON formatted string
    public static boolean isJSONValid(String jsonInString) {
        try {
            GSON.fromJson(jsonInString, Object.class);
            return true;
        } catch (JsonSyntaxException e) {
            return false;
        }
    }
}
