package com.maciej916.maessentials.commands;

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
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TextFormatting;

public class CommandTpahere {
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder<CommandSource> builder = Commands.literal("tpahere").requires(source -> source.hasPermissionLevel(0));
        builder
            .executes(context -> tpahere(context))
            .then(Commands.argument("targetPlayer", EntityArgument.players())
                    .executes(context -> tpahereArgs(context)));
        dispatcher.register(builder);
    }

    private static int tpahere(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        player.sendMessage(Methods.formatText("command.maessentials.player.provide", TextFormatting.DARK_RED));
        return Command.SINGLE_SUCCESS;
    }

    private static int tpahereArgs(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        ServerPlayerEntity requestedPlayer = EntityArgument.getPlayer(context, "targetPlayer");
        if (requestedPlayer != player) {
            Teleport tpr = Teleport.findTeleportRequest(player, requestedPlayer, player);
            if (tpr == null) {
                PlayerData playerData = DataManager.getPlayerData(player);
                long currentTime = System.currentTimeMillis() / 1000;
                if (Methods.delayCommand(playerData.getTeleportRequestTime(), ConfigValues.tpa_cooldown)) {
                    playerData.setTeleportRequestTime(currentTime);
                    DataManager.savePlayerData(playerData);
                    player.sendMessage(Methods.formatText("command.maessentials.tpr.request", TextFormatting.WHITE, requestedPlayer.getDisplayName()));
                    requestedPlayer.sendMessage(Methods.formatText("command.maessentials.tpr.tpa.target", TextFormatting.WHITE, player.getDisplayName()));
                    Teleport.teleportRequest(player, requestedPlayer, player, true);
                } else {
                    long timeleft = playerData.getTeleportRequestTime() + ConfigValues.tpa_cooldown - currentTime;
                    player.sendMessage(Methods.formatText("command.maessentials.player.cooldown", TextFormatting.DARK_RED, timeleft));
                }
            } else {
                player.sendMessage(Methods.formatText("command.maessentials.tpr.exist", TextFormatting.DARK_RED, requestedPlayer.getDisplayName()));
            }
        } else {
            player.sendMessage(Methods.formatText("command.maessentials.tpr.self", TextFormatting.DARK_RED));
        }
        return Command.SINGLE_SUCCESS;
    }
}