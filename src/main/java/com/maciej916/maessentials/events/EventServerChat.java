package com.maciej916.maessentials.events;

import com.maciej916.maessentials.classes.player.EssentialPlayer;
import com.maciej916.maessentials.classes.player.PlayerRestriction;
import com.maciej916.maessentials.data.DataManager;
import com.maciej916.maessentials.libs.Methods;
import com.maciej916.maessentials.libs.Time;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.ServerChatEvent;

import static com.maciej916.maessentials.libs.Methods.currentTimestamp;

public class EventServerChat {

    public static void event(ServerChatEvent event) {
        ServerPlayerEntity player = event.getPlayer();
        EssentialPlayer eslPlayer = DataManager.getPlayer(player);
        PlayerRestriction mute = eslPlayer.getRestrictions().getMute();
        if (mute != null) {
            if (mute.getTime() == -1 || mute.getTime() > currentTimestamp()) {
                if (mute.getTime() == -1) {
                    player.sendMessage(Methods.formatText("mute.maessentials.success.perm.target"));
                } else {
                    String displayTime = Time.formatDate(mute.getTime() - currentTimestamp());
                    player.sendMessage(Methods.formatText("mute.maessentials.success.target", displayTime));
                }
                event.setCanceled(true);
            }
        }
    }
}
