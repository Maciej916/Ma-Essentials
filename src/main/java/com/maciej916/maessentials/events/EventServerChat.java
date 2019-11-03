package com.maciej916.maessentials.events;

import com.maciej916.maessentials.data.DataManager;
import com.maciej916.maessentials.data.PlayerData;
import com.maciej916.maessentials.libs.Methods;
import com.maciej916.maessentials.libs.Time;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.ServerChatEvent;

public class EventServerChat {

    public static void event(ServerChatEvent event) {
        ServerPlayerEntity player = event.getPlayer();
        PlayerData playerData = DataManager.getPlayerData(player);
        if (playerData.isPlayerMuted()) {
            if (playerData.getMuteTime() == -1) {
                player.sendMessage(Methods.formatText("mute.maessentials.success.perm.target"));
            } else {
                long currentTime = System.currentTimeMillis() / 1000;
                String displayTime = Time.formatDate(playerData.getMuteTime() - currentTime);
                player.sendMessage(Methods.formatText("mute.maessentials.success.target", displayTime));
            }
            player.sendMessage(Methods.formatText("mute.maessentials.success.target.reason", playerData.getMuteReason()));
            event.setCanceled(true);
        }
    }
}
