package com.maciej916.maessentials.commands;

import com.maciej916.maessentials.classes.Location;
import com.maciej916.maessentials.config.ConfigValues;
import com.maciej916.maessentials.data.DataManager;
import com.maciej916.maessentials.data.PlayerData;
import com.maciej916.maessentials.libs.Methods;
import com.maciej916.maessentials.libs.Teleport;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TextFormatting;

public class CommandBack {

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder<CommandSource> builder = Commands.literal("back").requires(source -> source.hasPermissionLevel(0));
        builder.executes(context -> back(context));
        dispatcher.register(builder);
    }

    private static int back(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        PlayerData playerData = DataManager.getPlayerData(player);
        Location lastLocation = playerData.getLastLocation();
        if (lastLocation != null) {
            long currentTime = System.currentTimeMillis() / 1000;
            if (Methods.delayCommand(playerData.getBacktime(), ConfigValues.back_cooldown)) {
                playerData.setBacktime(currentTime);
                DataManager.savePlayerData(playerData);
                if ( ConfigValues.back_delay == 0) {
                    player.sendMessage(Methods.formatText("command.maessentials.back.success", TextFormatting.WHITE));
                } else {
                    player.sendMessage(Methods.formatText("command.maessentials.back.success.wait", TextFormatting.WHITE, ConfigValues.back_delay));
                }
                Teleport.teleportPlayer(player, lastLocation, true, ConfigValues.back_delay);
            } else {
                long timeleft = playerData.getBacktime() + ConfigValues.back_cooldown - currentTime;
                player.sendMessage(Methods.formatText("command.maessentials.player.cooldown", TextFormatting.DARK_RED, timeleft));
            }
        } else {
            player.sendMessage(Methods.formatText("command.maessentials.back.failed", TextFormatting.DARK_RED));
        }
        return Command.SINGLE_SUCCESS;
    }
}