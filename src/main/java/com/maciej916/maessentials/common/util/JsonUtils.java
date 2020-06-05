package com.maciej916.maessentials.common.util;

import com.google.gson.*;

import java.io.*;

public final class JsonUtils {

    public static JsonObject getJsonObject(JsonElement data) {
        JsonObject json = new JsonObject();
        json.add("data", data);
        return json;
    }

    public static JsonElement getJsonElement(Object data) {
        String json = new Gson().toJson(data);
        JsonParser parser = new JsonParser();
        return parser.parse(json);
    }

    public static void save(String catalog, String fileName, Object saveClass) {
        try (Writer writer = new FileWriter(catalog + fileName + ".json")) {
//            Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(saveClass, writer);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public static Object load(String catalog, String fileName, Object object) {
        Gson gson = new Gson();
        try (Reader reader = new FileReader(catalog + fileName + ".json")) {
            return gson.fromJson(reader, object.getClass());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
