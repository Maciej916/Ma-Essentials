package com.maciej916.maessentials.data;

import com.maciej916.maessentials.classes.EssentialPlayer;
import com.maciej916.maessentials.classes.Homes;
import com.maciej916.maessentials.classes.Location;
import com.maciej916.maessentials.classes.Warps;
import com.maciej916.maessentials.config.Config;
import com.maciej916.maessentials.libs.JsonMethods;
import com.maciej916.maessentials.libs.Log;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.command.CommandSource;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.entity.player.ServerPlayerEntity;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class DataManager {

    // Players

    private static HashMap<UUID, EssentialPlayer> playerData = new HashMap<>();

    public static void  cleanPlayerData() {
        playerData.clear();
    }

    public static void setPlayerData(UUID uuid, EssentialPlayer data) {
        playerData.put(uuid, data);
    }

    public static EssentialPlayer getPlayerData(ServerPlayerEntity player) {
        UUID playerUUID = player.getUniqueID();
        return getPlayerData(playerUUID);
    }

    public static EssentialPlayer getPlayerData(UUID playerUUID) {
        if (playerData.containsKey(playerUUID)) {
            return playerData.get(playerUUID);
        } else {
            EssentialPlayer essentialPlayer = new EssentialPlayer();
            essentialPlayer.setPlayerUUID(playerUUID);
            Log.debug("Creating player profile: " + playerUUID);
            playerData.put(playerUUID, essentialPlayer);
            savePlayerData(playerUUID, essentialPlayer);
            return essentialPlayer;
        }
    }

    public static void savePlayerData(UUID playerUUID, EssentialPlayer essentialPlayer) {
        Log.debug("Saving player data for player: " + playerUUID);
        JsonMethods.save(essentialPlayer, "players/" + playerUUID);
    }

    public static void savePlayerHome(UUID uuid, Homes homes) {
        Log.debug("Saving homes for player: " + uuid);
        JsonMethods.save(homes, "homes/" + uuid);
    }


    // Warps

    private static Warps warpData = new Warps();

    public static void cleanWarpData() {
        warpData.cleanWarps();
    }

    public static Warps getWarpData() {
        return warpData;
    }

    public static void saveWarp(String name, Location location) {
        Log.debug("Saving warp: " + name);
        JsonMethods.save(location, "warps/" + name);
    }

    public static void removeWarp(String name) {
        Log.debug("Removing warp: " + name);
        File file = new File(Config.getMainCatalog()+"warps/" + name + ".json");
        file.delete();
    }


}
