package com.maciej916.maessentials.common.lib.player;

import com.google.gson.*;
import com.maciej916.maessentials.common.lib.Storage;

import java.lang.reflect.Type;
import java.util.UUID;

import static com.maciej916.maessentials.common.util.JsonUtils.getJsonElement;
import static com.maciej916.maessentials.common.util.JsonUtils.getJsonObject;

public class EssentialPlayerSD implements JsonDeserializer<EssentialPlayer>, JsonSerializer<EssentialPlayer> {

    @Override
    public EssentialPlayer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();

        UUID uuid = new Gson().fromJson(object.get("uuid"), UUID.class);
        String username = new Gson().fromJson(object.get("username"), String.class);
        PlayerData data = new Gson().fromJson(object.get("data"), PlayerData.class);
        PlayerUsage last_usage = new Gson().fromJson(object.get("last_usage"), PlayerUsage.class);
        PlayerRestrictions restrictions = new Gson().fromJson(object.get("restrictions"), PlayerRestrictions.class);
        Storage custom_data = new Gson().fromJson(getJsonObject(object.get("custom_data")), Storage.class);
        EssentialPlayer eslPlayer = new EssentialPlayer(uuid, username, data, restrictions, last_usage, custom_data);
        return eslPlayer;
    }

    @Override
    public JsonElement serialize(EssentialPlayer src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();

        object.add("uuid", getJsonElement(src.getPlayerUUID().toString()));
        object.add("username", getJsonElement(src.getUsername()));
        object.add("data", getJsonElement(src.getData()));
        object.add("last_usage", getJsonElement(src.getUsage()));
        object.add("restrictions", getJsonElement(src.getRestrictions()));
        object.add("custom_data", getJsonElement(src.getCustomData().getData()));

        return object;
    }
}
