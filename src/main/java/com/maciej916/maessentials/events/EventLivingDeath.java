package com.maciej916.maessentials.events;

import com.maciej916.maessentials.classes.Location;
import com.maciej916.maessentials.classes.player.EssentialPlayer;
import com.maciej916.maessentials.config.ConfigValues;
import com.maciej916.maessentials.data.DataManager;
import com.maciej916.maessentials.libs.Methods;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

public class EventLivingDeath {

    public static void event(LivingDeathEvent event) {
        if (ConfigValues.back_death_enable) {
            if (event.getEntity() instanceof PlayerEntity) {
                ServerPlayerEntity player = (ServerPlayerEntity) event.getEntity();
                player.sendMessage(Methods.formatText("back.maessentials.death"));

                Location location = new Location(player);
                location.y++;

                EssentialPlayer eslPlayer = DataManager.getPlayer(player);
                eslPlayer.getData().setLastLocation(location);
                eslPlayer.saveData();
            }
        }
    }
}
