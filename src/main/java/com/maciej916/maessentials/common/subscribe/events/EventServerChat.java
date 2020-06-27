package com.maciej916.maessentials.common.subscribe.events;

import com.maciej916.maessentials.MaEssentials;
import com.maciej916.maessentials.common.lib.player.EssentialPlayer;
import com.maciej916.maessentials.common.lib.player.PlayerRestriction;
import com.maciej916.maessentials.common.data.DataManager;
import com.maciej916.maessentials.common.util.TimeUtils;
import com.maciej916.maessentials.common.util.TextUtils;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.maciej916.maessentials.common.util.TimeUtils.currentTimestamp;

@Mod.EventBusSubscriber(modid = MaEssentials.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EventServerChat {

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onPlayerChat(ServerChatEvent event) {
        ServerPlayerEntity player = event.getPlayer();
        EssentialPlayer eslPlayer = DataManager.getPlayer(player);
        PlayerRestriction mute = eslPlayer.getRestrictions().getMute();
        if (mute != null) {
            if (mute.getTime() == -1 || mute.getTime() > currentTimestamp()) {
                if (mute.getTime() == -1) {
                    TextUtils.sendChatMessage(player, "mute.maessentials.success.perm.target");
                } else {
                    String displayTime = TimeUtils.formatDate(mute.getTime() - currentTimestamp());
                    TextUtils.sendChatMessage(player, "mute.maessentials.success.target", displayTime);
                }
                TextUtils.sendChatMessage(player, "mute.maessentials.success.target.reason", mute.getReason());
                event.setCanceled(true);
            }
        }
    }
}
