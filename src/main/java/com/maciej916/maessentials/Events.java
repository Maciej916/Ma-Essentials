package com.maciej916.maessentials;

import com.maciej916.maessentials.config.ConfigValues;
import com.maciej916.maessentials.libs.Teleport;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class Events {
    private static int tickCounter = 0;

    @SubscribeEvent
    public static void onDeath(LivingDeathEvent event) {
        if (ConfigValues.enableBack) {
            if (event.getEntity() instanceof PlayerEntity) {
                ServerPlayerEntity player = (ServerPlayerEntity) event.getEntity();
                player.sendMessage(new TranslationTextComponent("command.maessentials.back.death"));
                Teleport.setPlayerLastLoc(player);
            }
        }
    }

    @SubscribeEvent
    public static void worldTick(TickEvent.WorldTickEvent event) {
        if (event.phase == TickEvent.Phase.END){
            if (tickCounter == 20) {
                if (ConfigValues.enableTpa) {
                    Teleport.checkTeleportRequests();
                }
                tickCounter = 0;
            } else {
                tickCounter++;
            }
        }
    }




}
