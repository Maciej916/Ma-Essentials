package com.maciej916.maessentials;

import com.maciej916.maessentials.commands.*;
import com.maciej916.maessentials.config.ConfigValues;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandSource;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

import static com.maciej916.maessentials.libs.Methods.isDev;

public class Commands {

    @SubscribeEvent
    public static void onServerStarting(FMLServerStartingEvent event) {
        CommandDispatcher<CommandSource> dispatcher = event.getCommandDispatcher();

        // Spawn
        if (ConfigValues.spawn_enable) {
            CommandSetSpawn.register(dispatcher);
            CommandSpawn.register(dispatcher);
        }

        // Homes
        if (ConfigValues.homes_enable) {
            CommandSetHome.register(dispatcher);
            CommandDelHome.register(dispatcher);
            CommandHome.register(dispatcher);
        }

        // Warps
        if (ConfigValues.warps_enable) {
            CommandSetWarp.register(dispatcher);
            CommandDelWarp.register(dispatcher);
            CommandWarp.register(dispatcher);
        }

        // Back
        if (ConfigValues.back_enable) {
            CommandBack.register(dispatcher);
        }

        // RNDTP
        if (ConfigValues.rndtp_enable) {
            CommandRndtp.register(dispatcher);
        }

        // Suicide
        if (ConfigValues.suicide_enable) {
            CommandSuicide.register(dispatcher);
        }

        // Time
        if (ConfigValues.time_enable) {
            CommandDay.register(dispatcher);
            CommandNight.register(dispatcher);
        }

        // Weather
        if (ConfigValues.weather_enable) {
            CommandSun.register(dispatcher);
            CommandRain.register(dispatcher);
            CommandThunder.register(dispatcher);
        }

        // Heal
        if (ConfigValues.heal_enable) {
            CommandHeal.register(dispatcher);
        }

        // GM
        if (ConfigValues.gm_enable) {
            CommandGm.register(dispatcher);
        }

        // Fly
        if (ConfigValues.fly_enable) {
            CommandFly.register(dispatcher);
        }

        // God
        if (ConfigValues.god_enable) {
            CommandGod.register(dispatcher);
        }

        // Top
        if (ConfigValues.top_enable) {
            CommandTop.register(dispatcher);
        }

        // Kits
        if (ConfigValues.kits_enable) {
            CommandKit.register(dispatcher);
        }

        // Speed
        if (ConfigValues.speed_enable) {
            CommandSpeed.register(dispatcher);
        }

        // Broadcast
        if (ConfigValues.broadcast_enable) {
            CommandBroadcast.register(dispatcher);
        }

        // Up
        if (ConfigValues.up_enable) {
            CommandUp.register(dispatcher);
        }

        // Server
        if (event.getServer().isDedicatedServer() || isDev()) {
            // TPA
            if (ConfigValues.tpa_enable) {
                CommandTpa.register(dispatcher);
                CommandTpahere.register(dispatcher);
                CommandTpdeny.register(dispatcher);
                CommandTpaccept.register(dispatcher);
            }

            // Mute
            if (ConfigValues.mute_enable) {
                CommandMute.register(dispatcher);
                CommandUnmute.register(dispatcher);
            }

            // Tpall
            if (ConfigValues.tpall_enable) {
                CommandTpall.register(dispatcher);
            }

            // Kickall
            if (ConfigValues.kickall_enable) {
                CommandKickall.register(dispatcher);
            }

            // AFK
            if (ConfigValues.afk_command) {
                CommandAfk.register(dispatcher);
            }
        }

        // Reload
        CommandMaeReload.register(dispatcher);
    }
}
