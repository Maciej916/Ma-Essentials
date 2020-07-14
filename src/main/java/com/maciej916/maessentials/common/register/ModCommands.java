package com.maciej916.maessentials.common.register;

import com.maciej916.maessentials.common.command.BaseCommand;
import com.maciej916.maessentials.common.command.impl.admin.*;
import com.maciej916.maessentials.common.command.impl.home.DelHomeCommand;
import com.maciej916.maessentials.common.command.impl.home.HomeCommand;
import com.maciej916.maessentials.common.command.impl.home.SetHomeCommand;
import com.maciej916.maessentials.common.command.impl.kit.KitCommand;
import com.maciej916.maessentials.common.command.impl.kit.KitsCommand;
import com.maciej916.maessentials.common.command.impl.spawn.SetSpawnCommand;
import com.maciej916.maessentials.common.command.impl.spawn.SpawnCommand;
import com.maciej916.maessentials.common.command.impl.teleport_request.TpaCommand;
import com.maciej916.maessentials.common.command.impl.teleport_request.TpacceptCommand;
import com.maciej916.maessentials.common.command.impl.teleport_request.TpahereCommand;
import com.maciej916.maessentials.common.command.impl.teleport_request.TpdenyCommand;
import com.maciej916.maessentials.common.command.impl.time.DayCommand;
import com.maciej916.maessentials.common.command.impl.time.NightCommand;
import com.maciej916.maessentials.common.command.impl.warp.DelWarpCommand;
import com.maciej916.maessentials.common.command.impl.warp.SetWarpCommand;
import com.maciej916.maessentials.common.command.impl.warp.WarpCommand;
import com.maciej916.maessentials.common.command.impl.warp.WarpsCommand;
import com.maciej916.maessentials.common.command.impl.weather.RainCommand;
import com.maciej916.maessentials.common.command.impl.weather.SunCommand;
import com.maciej916.maessentials.common.command.impl.weather.ThunderCommand;
import com.maciej916.maessentials.common.command.impl.player.*;
import com.maciej916.maessentials.common.config.ModConfig;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandSource;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

import java.util.ArrayList;

public final class ModCommands {

    private static final ArrayList<BaseCommand> commands = new ArrayList<>();

    @SubscribeEvent
    public static void onServerStarting(FMLServerStartingEvent event) {
        CommandDispatcher<CommandSource> dispatcher = event.getCommandDispatcher();

        // Time

        commands.add(new DayCommand("day", 2, ModConfig.time_enable));
        commands.add(new NightCommand("night", 2, ModConfig.time_enable));

        // Weather

        commands.add(new RainCommand("rain", 2, ModConfig.weather_enable));
        commands.add(new SunCommand("sun", 2, ModConfig.weather_enable));
        commands.add(new ThunderCommand("thunder", 2, ModConfig.weather_enable));

        // Spawn

        commands.add(new SpawnCommand("spawn", 0, ModConfig.spawn_enable));
        commands.add(new SetSpawnCommand("setspawn", 2, ModConfig.spawn_enable));

        // Player

        commands.add(new AfkCommand("afk", 0, ModConfig.afk_command));
        commands.add(new BackCommand("back", 0, ModConfig.back_enable));
        commands.add(new RndtpCommand("rndtp", 0, ModConfig.rndtp_enable));
        commands.add(new PlayerSuicideCommand("suicide", 0, ModConfig.suicide_enable));
        commands.add(new TrashCommand("trash", 0, ModConfig.trash_enable));

        // Home

        commands.add(new DelHomeCommand("delhome", 0, ModConfig.homes_enable));
        commands.add(new SetHomeCommand("sethome", 0, ModConfig.homes_enable));
        commands.add(new HomeCommand("home", 0, ModConfig.homes_enable));

        // Warp

        commands.add(new DelWarpCommand("delwarp", 2, ModConfig.warps_enable));
        commands.add(new SetWarpCommand("setwarp", 2, ModConfig.warps_enable));
        commands.add(new WarpCommand("warp", 0, ModConfig.warps_enable));
        commands.add(new WarpsCommand("warps", 0, ModConfig.warps_enable));

        // Teleport request

        commands.add(new TpaCommand("tpa", 0, ModConfig.tpa_enable));
        commands.add(new TpahereCommand("tpahere", 0, ModConfig.tpa_enable));
        commands.add(new TpacceptCommand("tpaccept", 0, ModConfig.tpa_enable));
        commands.add(new TpdenyCommand("tpdeny", 0, ModConfig.tpa_enable));

        // Kits

        commands.add(new KitCommand("kit", 0, ModConfig.kits_enable));
        commands.add(new KitsCommand("kits", 0, ModConfig.kits_enable));

        // Admin

        commands.add(new MuteCommand("mute", 2, ModConfig.mute_enable));
        commands.add(new UnmuteCommand("unmute", 2, ModConfig.mute_enable));

        commands.add(new AdminSuicideCommand("suicide", 2, ModConfig.suicide_enable));
        commands.add(new TpallCommand("tpall", 2, ModConfig.tpall_enable));
        commands.add(new KickallCommand("kickall", 2, ModConfig.kickall_enable));
        commands.add(new BroadcastCommand("broadcast", 2, ModConfig.broadcast_enable));

        commands.add(new EndcCommand("endc", 2, ModConfig.endc_enable));
        commands.add(new InvseeCommand("invsee", 2, ModConfig.invsee_enable));

        commands.add(new HealCommand("heal", 2, ModConfig.heal_enable));
        commands.add(new GmCommand("gm", 2, ModConfig.gm_enable));
        commands.add(new FlyCommand("fly", 2, ModConfig.fly_enable));
        commands.add(new GodCommand("god", 2, ModConfig.god_enable));
        commands.add(new TopCommand("top", 2, ModConfig.top_enable));

        commands.add(new SpeedCommand("speed", 2, ModConfig.speed_enable));
        commands.add(new UpCommand("up", 2, ModConfig.up_enable));
        commands.add(new TempbanCommand("tempban", 2, ModConfig.tempban_enable));
        commands.add(new UnbanCommand("unban", 2, ModConfig.tempban_enable));
        commands.add(new CheckCommand("check", 2, ModConfig.check_enable));
        commands.add(new HeadCommand("head", 2, ModConfig.head_enable));
        commands.add(new RepairCommand("repair", 2, ModConfig.repair_enable));
        commands.add(new HatCommand("hat", 2, ModConfig.hat_enable));

        commands.add(new AdminKitCommand("kit", 2, ModConfig.kits_enable));

        commands.add(new ReloadCommand("maereload", 4, true));

        // Register enabled commands

        commands.forEach((cmd) -> {
            if (cmd.isEnabled() && cmd.setExecution() != null) {
                dispatcher.register(cmd.getBuilder());
            }
        });
    }

}
