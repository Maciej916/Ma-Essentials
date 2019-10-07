package com.maciej916.maessentials;

import com.maciej916.maessentials.utils.Teleport;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class Events {
    public Events() {}

    @SubscribeEvent
    public void onDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof PlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) event.getEntity();
            player.sendMessage(new TranslationTextComponent("command.maessentials.back.grave"));
            Teleport.setPlayerLastLoc(player);
        }
    }

}
