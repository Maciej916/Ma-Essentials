package com.maciej916.maessentials;

import com.maciej916.maessentials.commands.*;
import com.maciej916.maessentials.config.ConfigValues;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandSource;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

public class Commands {

    @SubscribeEvent
    public static void onServerStarting(FMLServerStartingEvent event) {
        CommandDispatcher<CommandSource> dispatcher = event.getCommandDispatcher();

        if (ConfigValues.enableSpawn) {
            CommandSetSpawn.register(dispatcher);
            CommandSpawn.register(dispatcher);
        }

        if (ConfigValues.enableTime) {
            CommandDay.register(dispatcher);
            CommandNight.register(dispatcher);
        }

        if (ConfigValues.enableWeather) {
            CommandSun.register(dispatcher);
            CommandRain.register(dispatcher);
            CommandThunder.register(dispatcher);
        }

        if (ConfigValues.enableHomes) {
            CommandSetHome.register(dispatcher);
            CommandDelHome.register(dispatcher);
            CommandHome.register(dispatcher);
        }

        if (ConfigValues.enableBack) {
            CommandBack.register(dispatcher);
        }

        if (ConfigValues.enableSuicide) {
            CommandSuicide.register(dispatcher);
        }

        if (ConfigValues.enableHeal) {
            CommandHeal.register(dispatcher);
        }

        if (ConfigValues.enableGm) {
            CommandGm.register(dispatcher);
        }

        if (ConfigValues.enableFly) {
            CommandFly.register(dispatcher);
        }

        if (ConfigValues.enableGod) {
            CommandGod.register(dispatcher);
        }

        if (ConfigValues.enableWarps) {
            CommandSetWarp.register(dispatcher);
            CommandDelWarp.register(dispatcher);
            CommandWarp.register(dispatcher);
        }

        // Commands on server
        if (event.getServer().isDedicatedServer()) {
            if (ConfigValues.enableTpa) {
                CommandTpa.register(dispatcher);
                CommandTpahere.register(dispatcher);
                CommandTpdeny.register(dispatcher);
                CommandTpaccept.register(dispatcher);
            }
        }

        if (ConfigValues.enableRndtp) {
            CommandRndtp.register(dispatcher);
        }


    }
}
