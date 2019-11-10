package com.maciej916.maessentials.events;

import com.maciej916.maessentials.classes.Location;
import com.maciej916.maessentials.classes.kit.Kit;
import com.maciej916.maessentials.classes.player.EssentialPlayer;
import com.maciej916.maessentials.config.ConfigValues;
import com.maciej916.maessentials.data.DataManager;
import com.maciej916.maessentials.libs.Log;
import com.maciej916.maessentials.libs.Methods;
import com.maciej916.maessentials.libs.Teleport;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class EventPlayerLoggedIn {

    public static void event(PlayerEvent.PlayerLoggedInEvent event) {
        ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
        EssentialPlayer eslPlayer = DataManager.newPlayer(player);

        if (eslPlayer != null) {
            Log.debug("New player " + player.getDisplayName().getString() + " joined");

            if (player.getServer().isDedicatedServer()) {
                // Add variable spawn force new
                Location spawnLocation = DataManager.getWorld().getSpawn();
                if (spawnLocation != null) {
                    Teleport.doTeleport(player, spawnLocation, true, false);
                }
            }

            if (ConfigValues.kits_starting) {
                Kit kit = DataManager.getKit().getKit(ConfigValues.kits_starting_name);
                if (Methods.giveKit(player, kit)) {
                    eslPlayer.getUsage().setKitUssage(ConfigValues.kits_starting_name);
                    eslPlayer.saveData();
                }
            }
        } else {
            Log.debug("Player " + player.getDisplayName().getString() + " joined");
        }
    }
}