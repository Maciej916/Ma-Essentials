package com.maciej916.maessentials;

import com.maciej916.maessentials.classes.Location;
import com.maciej916.maessentials.config.ConfigValues;
import com.maciej916.maessentials.data.DataManager;
import com.maciej916.maessentials.data.PlayerData;
import com.maciej916.maessentials.libs.Log;
import com.maciej916.maessentials.libs.Methods;
import com.maciej916.maessentials.libs.Teleport;
import com.maciej916.maessentials.libs.Time;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.UUID;

public class Events {
    private static int tickCounter = 0;

    @SubscribeEvent
    public static void onDeath(LivingDeathEvent event) {
        if (ConfigValues.back_death_enable) {
            if (event.getEntity() instanceof PlayerEntity) {
                ServerPlayerEntity player = (ServerPlayerEntity) event.getEntity();
                player.sendMessage(Methods.formatText("event.maessentials.back.death", TextFormatting.WHITE));
                Location deathLocation = new Location(player);
                deathLocation.y++;
                DataManager.getPlayerData(player).setLastLocation(deathLocation);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
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

            Teleport.doTeleport(player, DataManager.getModData().getSpawnPoint(), true);
        }
    }

    @SubscribeEvent
    public static void onPlayerLeave(PlayerEvent.PlayerLoggedOutEvent event) {
        ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
        UUID playerUUID = player.getUniqueID();
        Log.debug("Player " + player.getDisplayName() + " leave");
    }

    @SubscribeEvent
    public static void opPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
        Teleport.doTeleport(player, DataManager.getModData().getSpawnPoint(), true);
    }

    @SubscribeEvent
    public static void worldTick(TickEvent.WorldTickEvent event) {
        if (event.phase == TickEvent.Phase.END){
            if (tickCounter == 20) {
                Teleport.checkTeleports();
                tickCounter = 0;
            } else {
                tickCounter++;
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerChat(ServerChatEvent event) {
        ServerPlayerEntity player = event.getPlayer();
        PlayerData playerData = DataManager.getPlayerData(player);
        if (playerData.isPlayerMuted()) {
            if (playerData.getMuteTime() == -1) {
                player.sendMessage(Methods.formatText("event.maessentials.mute.perm", TextFormatting.RED, playerData.getMuteReason()));
            } else {
                long currentTime = System.currentTimeMillis() / 1000;
                String displayTime = Time.formatDate(playerData.getMuteTime() - currentTime);
                player.sendMessage(Methods.formatText("event.maessentials.mute", TextFormatting.RED, displayTime, playerData.getMuteReason()));
            }
            event.setCanceled(true);
        }
    }

}
