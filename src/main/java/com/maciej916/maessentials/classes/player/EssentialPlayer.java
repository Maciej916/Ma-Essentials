package com.maciej916.maessentials.classes.player;

import com.google.gson.annotations.JsonAdapter;
import com.maciej916.maessentials.classes.Storage;
import com.maciej916.maessentials.classes.home.HomeData;
import com.maciej916.maessentials.config.ConfigValues;
import com.maciej916.maessentials.libs.Json;

import java.util.UUID;

@JsonAdapter(EssentialPlayerSD.class)
public class EssentialPlayer {
    private UUID playerUUID;
    private String username;
    private HomeData home_data = new HomeData();

    private PlayerData data = new PlayerData();
    private PlayerUsage last_usage = new PlayerUsage();
    private PlayerRestrictions restrictions = new PlayerRestrictions();
    private PlayerTemp temp_data = new PlayerTemp();

    private Storage custom_data = new Storage();

    public void setHomeData(HomeData home_data) {
        this.home_data = home_data;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public String getUsername() {
        return username;
    }

    public PlayerData getData() {
        return data;
    }

    public PlayerUsage getUsage() {
        return last_usage;
    }

    public PlayerRestrictions getRestrictions() {
        return restrictions;
    }

    public PlayerTemp getTemp() {
        return temp_data;
    }

    public Storage getCustomData() {
        return custom_data;
    }

    public HomeData getHomeData() {
        return home_data;
    }

    public EssentialPlayer(UUID playerUUID) {
        this.playerUUID = playerUUID;
    }

    public EssentialPlayer (UUID playerUUID, String username, PlayerData data, PlayerRestrictions restrictions, PlayerUsage last_usage, Storage custom_data) {
        this.playerUUID = playerUUID;
        this.username = username;
        this.data = data;
        this.restrictions = restrictions;
        this.last_usage = last_usage;
        this.custom_data = custom_data;
    }

    public void cleanTemp() {
       temp_data = new PlayerTemp();
    }

    public void saveData() {
        Json.save(ConfigValues.worldCatalog + "players/", playerUUID.toString(), this);
    }

    public void saveHomes() {
        Json.save(ConfigValues.worldCatalog + "homes/", playerUUID.toString(), home_data);
    }
}