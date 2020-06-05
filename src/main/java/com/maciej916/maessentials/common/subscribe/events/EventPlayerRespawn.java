package com.maciej916.maessentials.common.subscribe.events;

import com.maciej916.maessentials.MaEssentials;
import com.maciej916.maessentials.common.lib.Location;
import com.maciej916.maessentials.common.config.ModConfig;
import com.maciej916.maessentials.common.data.DataManager;
import com.maciej916.maessentials.common.util.TeleportUtils;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MaEssentials.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EventPlayerRespawn {

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (ModConfig.spawn_force_on_death) {
            ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
            Location location = DataManager.getWorld().getSpawn();
            TeleportUtils.doTeleport(player, location, true, false);
        }
    }
}
