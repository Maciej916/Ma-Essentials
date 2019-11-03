package com.maciej916.maessentials.events;

import com.maciej916.maessentials.libs.Teleport;
import net.minecraftforge.event.TickEvent;

public class EventWorldTick {

    private static int tickCounter = 0;

    public static void event(TickEvent.WorldTickEvent event) {
        if (event.phase == TickEvent.Phase.END){
            if (tickCounter == 20) {
                Teleport.checkTeleports();
                tickCounter = 0;
            } else {
                tickCounter++;
            }
        }
    }
}
