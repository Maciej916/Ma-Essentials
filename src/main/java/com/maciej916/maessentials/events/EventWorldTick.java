package com.maciej916.maessentials.events;

import com.maciej916.maessentials.config.ConfigValues;
import com.maciej916.maessentials.libs.Afk;
import com.maciej916.maessentials.libs.Teleport;
import net.minecraftforge.event.TickEvent;

import java.util.Objects;

import static com.maciej916.maessentials.libs.Methods.isDev;

public class EventWorldTick {

    private static int tickCounter = 0;

    public static void event(TickEvent.WorldTickEvent event) {
        if (event.phase == TickEvent.Phase.END){
            if (tickCounter == 20) {
                Teleport.checkTeleports();
                if ((event.world.getServer().isDedicatedServer() && ConfigValues.afk_auto) || isDev()) {
                    Afk.checkAfk(Objects.requireNonNull(event.world.getServer()).getPlayerList());
                }
                tickCounter = 0;
            } else {
                tickCounter++;
            }
        }
    }
}
