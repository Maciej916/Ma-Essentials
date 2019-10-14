package com.maciej916.maessentials.data;

import com.maciej916.maessentials.classes.EssentialPlayer;
import com.maciej916.maessentials.classes.Homes;
import com.maciej916.maessentials.libs.JsonMethods;
import com.maciej916.maessentials.libs.Log;
import net.minecraft.entity.player.ServerPlayerEntity;

import java.util.HashMap;
import java.util.UUID;

public class DataManager {

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

}
