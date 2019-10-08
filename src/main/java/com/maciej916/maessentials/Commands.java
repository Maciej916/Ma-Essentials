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

        // World commands
        CommandDay.register(dispatcher);
        CommandNight.register(dispatcher);

        CommandSun.register(dispatcher);
        CommandRain.register(dispatcher);
        CommandThunder.register(dispatcher);

        CommandSpawn.register(dispatcher);
        CommandSetSpawn.register(dispatcher);

        // Player commands
        CommandBack.register(dispatcher);
        CommandPkill.register(dispatcher);
        CommandHeal.register(dispatcher);
        CommandGm.register(dispatcher);
        CommandFly.register(dispatcher);

        // Home commands
        CommandSetHome.register(dispatcher);
        CommandDelHome.register(dispatcher);
        CommandHome.register(dispatcher);

        // Warp commands
        CommandSetWarp.register(dispatcher);
        CommandDelWarp.register(dispatcher);
        CommandWarp.register(dispatcher);

        if (event.getServer().isDedicatedServer()) {
            // Tpa commands
            CommandTpa.register(dispatcher);
            CommandTpahere.register(dispatcher);
            CommandTpdeny.register(dispatcher);
            CommandTpaccept.register(dispatcher);
        }

    }
}
