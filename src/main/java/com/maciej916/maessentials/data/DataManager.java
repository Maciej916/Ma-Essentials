package com.maciej916.maessentials.data;

import com.maciej916.maessentials.classes.kit.KitData;
import com.maciej916.maessentials.classes.player.EssentialPlayer;
import com.maciej916.maessentials.classes.world.WorldData;
import com.maciej916.maessentials.classes.warp.WarpData;
import com.maciej916.maessentials.libs.Log;
import net.minecraft.entity.player.ServerPlayerEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DataManager {

    private static WorldData worldData = new WorldData();
    private static WarpData warpData = new WarpData();
    private static KitData kitData = new KitData();
    private static HashMap<UUID, EssentialPlayer> playerData = new HashMap<>();

     public static void cleanData() {
         worldData = new WorldData();
         warpData = new WarpData();
         kitData = new KitData();
         playerData = new HashMap<>();
     }

    public static void setWorldData(WorldData worldData) {
        DataManager.worldData = worldData;
    }

    public static void setWarpData(WarpData warpData) {
        DataManager.warpData = warpData;
    }

    public static void setKitData(KitData kitData) {
        DataManager.kitData = kitData;
    }

    public static void setPlayerData(EssentialPlayer eslPlayer) {
        DataManager.playerData.put(eslPlayer.getPlayerUUID(), eslPlayer);
    }

    public static WorldData getWorld() {
        return worldData;
    }

    public static WarpData getWarp() {
        return warpData;
    }

    public static KitData getKit() {
        return kitData;
    }

    public static EssentialPlayer newPlayer(ServerPlayerEntity player) {
        UUID playerUUID = player.getUniqueID();
        if (playerData.containsKey(playerUUID)) {
            return null;
        }
        Log.debug("Create profile for player: " + playerUUID.toString());
        return getPlayer(playerUUID);
    }

    public static EssentialPlayer getPlayer(ServerPlayerEntity player) {
        UUID playerUUID = player.getUniqueID();
        return getPlayer(playerUUID);
    }

    public static EssentialPlayer getPlayer(UUID playerUUID) {
        if (playerData.containsKey(playerUUID)) {
            return playerData.get(playerUUID);
        } else {
            EssentialPlayer eslPlayer = new EssentialPlayer(playerUUID);
            playerData.put(playerUUID, eslPlayer);
            eslPlayer.saveData();
            return eslPlayer;
        }
    }

    public static EssentialPlayer getPlayer(String findPlayer) {
        for(Map.Entry<UUID, EssentialPlayer> entry : playerData.entrySet()) {
            EssentialPlayer eslPlayer = entry.getValue();
            if ((eslPlayer.getUsername() != null && eslPlayer.getUsername().equals(findPlayer)) || eslPlayer.getPlayerUUID().toString().equals(findPlayer)) {
                return eslPlayer;
            }
        }
        return null;
    }

    public static HashMap<UUID, EssentialPlayer> getPlayers() {
        return playerData;
    }
}
