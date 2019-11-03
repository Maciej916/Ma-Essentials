package com.maciej916.maessentials.events;

import com.maciej916.maessentials.libs.Log;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.entity.player.PlayerEvent;

import java.util.UUID;

public class EventPlayerLoggedOut {

    public static void event(PlayerEvent.PlayerLoggedOutEvent event) {
        ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
        UUID playerUUID = player.getUniqueID();
        Log.debug("Player " + player.getDisplayName() + " leave");
    }
}
