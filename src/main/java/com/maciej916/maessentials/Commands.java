package com.maciej916.maessentials;

import com.maciej916.maessentials.commands.*;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandSource;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

public class Commands {
    public Commands() {}

    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {

        MaEssentials.logger.info("Registering commands.");
        CommandDispatcher<CommandSource> dispatcher = event.getCommandDispatcher();

        CommandDay.register(dispatcher);
        CommandNight.register(dispatcher);
        CommandSun.register(dispatcher);
        CommandRain.register(dispatcher);
        CommandSpawn.register(dispatcher);
        CommandSetSpawn.register(dispatcher);
        CommandBack.register(dispatcher);
        CommandKill.register(dispatcher);
        CommandHeal.register(dispatcher);
        CommandHeal.register(dispatcher);

        CommandSetHome.register(dispatcher);
        CommandDelHome.register(dispatcher);
        CommandHome.register(dispatcher);

        CommandSetWarp.register(dispatcher);
        CommandDelWarp.register(dispatcher);
        CommandWarp.register(dispatcher);

    }
}
