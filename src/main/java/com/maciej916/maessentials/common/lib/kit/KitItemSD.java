package com.maciej916.maessentials.common.lib.kit;

import com.google.gson.*;
import com.maciej916.maessentials.common.util.LogUtils;
import com.mojang.brigadier.StringReader;
import net.minecraft.command.arguments.ItemInput;
import net.minecraft.command.arguments.ItemParser;

import java.lang.reflect.Type;

public class KitItemSD implements JsonDeserializer<KitItem>, JsonSerializer<KitItem> {

    @Override
    public KitItem deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();
        try {
            StringReader stringReader = new StringReader(object.get("name").getAsString() + object.get("nbt").getAsString());
            ItemParser itemParser = (new ItemParser(stringReader, false)).parse();
            ItemInput input = new ItemInput(itemParser.getItem(), itemParser.getNbt());
            return new KitItem(object.get("name").getAsString(), object.get("quantity").getAsInt(), object.get("nbt").getAsString(), input);
        } catch (Exception e) {
            LogUtils.err(e.toString());
            throw new JsonParseException("Failed to parse item for kit. Item: " + object.toString());
        }
    }

    @Override
    public JsonElement serialize(KitItem src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        return object;
    }
}

