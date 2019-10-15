package com.maciej916.maessentials;

import com.maciej916.maessentials.classes.Location;
import com.maciej916.maessentials.config.ConfigValues;
import com.maciej916.maessentials.data.DataManager;
import com.maciej916.maessentials.data.PlayerData;
import com.maciej916.maessentials.libs.Log;
import com.maciej916.maessentials.libs.Teleport;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.UUID;

public class Events {
    private static int tickCounter = 0;

    @SubscribeEvent
    public static void onDeath(LivingDeathEvent event) {
        if (ConfigValues.enableBack) {
            if (event.getEntity() instanceof PlayerEntity) {
                ServerPlayerEntity player = (ServerPlayerEntity) event.getEntity();
                player.sendMessage(new TranslationTextComponent("event.maessentials.back.death"));
                DataManager.getPlayerData(player).setLastLocation(new Location(player));
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
        UUID playerUUID = player.getUniqueID();
        Log.debug("Player " + player.getDisplayName() + " joined");
        if (DataManager.checkPlayerData(playerUUID)) {
            if (player.world.isRemote()) {
                player.sendMessage(new TranslationTextComponent("event.maessentials.player.join", player.getDisplayName(), true));
            }
        } else {
            PlayerData playerData = new PlayerData();
            playerData.setLastLocation(new Location(player));
            playerData.setPlayerUUID(playerUUID);
            DataManager.savePlayerData(playerUUID, playerData);
            if (player.world.isRemote()) {
                player.sendMessage(new TranslationTextComponent("event.maessentials.newplayer.join", player.getDisplayName(), true));
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerLeave(PlayerEvent.PlayerLoggedOutEvent event) {
        ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
        UUID playerUUID = player.getUniqueID();
        Log.debug("Player " + player.getDisplayName() + " leave");

        if (player.world.isRemote()) {
            player.sendMessage(new TranslationTextComponent("event.maessentials.player.leave", player.getDisplayName(), true));
        }
    }


    @SubscribeEvent
    public static void worldTick(TickEvent.WorldTickEvent event) {
        if (event.phase == TickEvent.Phase.END){
            if (tickCounter == 20) {
                Teleport.checkTeleports();
//                PlayerData.checkTeleportRequests();
                tickCounter = 0;
            } else {
                tickCounter++;
            }
        }
    }




}
