package com.maciej916.maessentials.commands;

import com.maciej916.maessentials.classes.Location;
import com.maciej916.maessentials.config.ConfigValues;
import com.maciej916.maessentials.data.DataManager;
import com.maciej916.maessentials.data.PlayerData;
import com.maciej916.maessentials.libs.Methods;
import com.maciej916.maessentials.libs.Teleport;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TextFormatting;

public class CommandHome {
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder<CommandSource> builder = Commands.literal("home").requires(source -> source.hasPermissionLevel(0));
        builder
                .executes(context -> home(context))
                .then(Commands.argument("homeName", StringArgumentType.string())
                        .suggests(Methods.HOME_SUGGEST)
                        .executes(context -> homeArgs(context)));
        dispatcher.register(builder);
    }

    private static int home(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        handleHome(player, "home");
        return Command.SINGLE_SUCCESS;
    }

    private static int homeArgs(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        String args = StringArgumentType.getString(context, "homeName").toString().toLowerCase();
        handleHome(player, args);
        return Command.SINGLE_SUCCESS;
    }

    private static void handleHome(ServerPlayerEntity player, String homeName) {
        PlayerData playerData = DataManager.getPlayerData(player);
        Location homeLocation = playerData.getHomes().get(homeName);
        if (homeLocation != null) {
            long currentTime = System.currentTimeMillis() / 1000;
            if (Methods.delayCommand(playerData.getHomeTime(), ConfigValues.homes_cooldown)) {
                playerData.setHomeTime(currentTime);
                DataManager.savePlayerData(playerData);
                if (ConfigValues.homes_delay == 0) {
                    player.sendMessage(Methods.formatText("command.maessentials.home.teleport", TextFormatting.WHITE, homeName));
                } else {
                    player.sendMessage(Methods.formatText("command.maessentials.home.teleport.wait", TextFormatting.WHITE, homeName, ConfigValues.homes_delay));
                }
                Teleport.teleportPlayer(player, homeLocation, true, ConfigValues.homes_delay);
            } else {
                long timeleft = playerData.getHomeTime() + ConfigValues.homes_cooldown - currentTime;
                player.sendMessage(Methods.formatText("command.maessentials.player.cooldown", TextFormatting.DARK_RED, timeleft));
            }
        } else {
            player.sendMessage(Methods.formatText("command.maessentials.home.notexist", TextFormatting.DARK_RED, homeName));
        }
    }
}