package com.maciej916.maessentials.events;

import com.maciej916.maessentials.classes.Location;
import com.maciej916.maessentials.config.ConfigValues;
import com.maciej916.maessentials.data.DataManager;
import com.maciej916.maessentials.data.PlayerData;
import com.maciej916.maessentials.libs.Log;
import com.maciej916.maessentials.libs.Methods;
import com.maciej916.maessentials.libs.Teleport;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.entity.player.PlayerEvent;

import java.util.UUID;

public class EventPlayerLoggedIn {

    public static void event(PlayerEvent.PlayerLoggedInEvent event) {
        ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
        UUID playerUUID = player.getUniqueID();
        if (DataManager.checkPlayerData(playerUUID)) {
            Log.debug("Player " + player.getDisplayName() + " joined");
        } else {
            Log.debug("New player " + player.getDisplayName() + " joined");

            PlayerData playerData = new PlayerData();
            playerData.setLastLocation(new Location(player));
            playerData.setPlayerUUID(playerUUID);
            DataManager.savePlayerData(playerData);

            if (ConfigValues.kits_starting) {
                Methods.giveKit(player, ConfigValues.kits_starting_name);
            }

            if (player.getServer().isDedicatedServer()) {
                Location spawnLocation = DataManager.getModData().getSpawnPoint();
                if (spawnLocation != null) {
                    Teleport.doTeleport(player, DataManager.getModData().getSpawnPoint(), true, false);
                }
            }
        }
    }
}
