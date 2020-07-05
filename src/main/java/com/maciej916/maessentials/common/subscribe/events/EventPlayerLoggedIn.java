package com.maciej916.maessentials.common.subscribe.events;

import com.maciej916.maessentials.MaEssentials;
import com.maciej916.maessentials.common.lib.Location;
import com.maciej916.maessentials.common.lib.kit.Kit;
import com.maciej916.maessentials.common.lib.player.EssentialPlayer;
import com.maciej916.maessentials.common.lib.player.PlayerData;
import com.maciej916.maessentials.common.lib.player.PlayerRestriction;
import com.maciej916.maessentials.common.config.ModConfig;
import com.maciej916.maessentials.common.data.DataManager;
import com.maciej916.maessentials.common.util.*;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.maciej916.maessentials.common.util.TimeUtils.currentTimestamp;


@Mod.EventBusSubscriber(modid = MaEssentials.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EventPlayerLoggedIn {

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
        EssentialPlayer eslPlayer = DataManager.newPlayer(player);

        if (eslPlayer != null) {
            LogUtils.debug("New player " + player.getDisplayName().getString() + " joined");

            if (player.getServer().isDedicatedServer()) {
                // Add variable spawn force new
                Location spawnLocation = DataManager.getWorld().getSpawn();
                if (spawnLocation != null) {
                    TeleportUtils.doTeleport(player, spawnLocation, true, false);
                }
            }

            if (ModConfig.kits_starting) {
                Kit kit = DataManager.getKit().getKit(ModConfig.kits_starting_name);
                if (ModUtils.giveKit(player, kit)) {
                    eslPlayer.getUsage().setKitUssage(ModConfig.kits_starting_name);
                    eslPlayer.saveData();
                }
            }
        } else {
            EssentialPlayer eslPlayerExisted = DataManager.getPlayer(player);
            PlayerRestriction ban = eslPlayerExisted.getRestrictions().getBan();
            if (ban != null) {
                if (ban.getTime() == -1) {
                    player.connection.disconnect(new TranslationTextComponent("tempban.maessentials.success.perm.target", player.getDisplayName(), ban.getReason()));
                } else {
                   if (ban.getTime() > currentTimestamp()) {
                       String displayTime = TimeUtils.formatDate(ban.getTime() - currentTimestamp());
                       player.connection.disconnect(new TranslationTextComponent("tempban.maessentials.success.target", player.getDisplayName(), displayTime, ban.getReason()));
                   }
                }
            }

            PlayerData playerData = eslPlayerExisted.getData();
            PlayerUtils.setFlying(player, playerData.getFlyEnabled());
            PlayerUtils.setGod(player, playerData.getGodEnabled());

            LogUtils.debug("Player " + player.getDisplayName().getString() + " joined");
        }

        EssentialPlayer eslPlayerNew = DataManager.getPlayer(player);
        eslPlayerNew.setUsername(player.getDisplayName().getString());
        eslPlayerNew.saveData();
    }
}