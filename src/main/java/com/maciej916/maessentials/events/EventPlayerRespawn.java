package com.maciej916.maessentials.events;

import com.maciej916.maessentials.config.ConfigValues;
import com.maciej916.maessentials.data.DataManager;
import com.maciej916.maessentials.libs.Teleport;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class EventPlayerRespawn {

    public static void event(PlayerEvent.PlayerRespawnEvent event) {
        if (ConfigValues.spawn_force_on_death) {
            ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
            Teleport.doTeleport(player, DataManager.getModData().getSpawnPoint(), true, false);
        }
    }
}
