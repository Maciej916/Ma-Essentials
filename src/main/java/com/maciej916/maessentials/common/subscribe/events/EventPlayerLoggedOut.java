package com.maciej916.maessentials.common.subscribe.events;

import com.maciej916.maessentials.MaEssentials;
import com.maciej916.maessentials.common.lib.player.EssentialPlayer;
import com.maciej916.maessentials.common.data.DataManager;
import com.maciej916.maessentials.common.util.LogUtils;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MaEssentials.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EventPlayerLoggedOut {

    @SubscribeEvent
    public static void onPlayerLeave(PlayerEvent.PlayerLoggedOutEvent event) {
        ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
        EssentialPlayer eslPlayer = DataManager.getPlayer(player);
        eslPlayer.cleanTemp();
        LogUtils.debug("Player " + player.getDisplayName().getString() + " leave");
    }
}
