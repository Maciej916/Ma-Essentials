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

public class CommandTpa{
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder<CommandSource> builder = Commands.literal("tpa").requires(source -> source.hasPermissionLevel(0));
        builder
            .executes(context -> tpa(context))
            .then(Commands.argument("targetPlayer", EntityArgument.players())
                    .executes(context -> tpaArgs(context)));
        dispatcher.register(builder);
    }

    private static int tpa(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        player.sendMessage(Methods.formatText("maessentials.provide.player", TextFormatting.RED));
        return Command.SINGLE_SUCCESS;
    }

    private static int tpaArgs(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        ServerPlayerEntity requestedPlayer = EntityArgument.getPlayer(context, "targetPlayer");
        if (requestedPlayer != player) {
            Teleport tpr = Teleport.findTeleportRequest(player, player, requestedPlayer);
            if (tpr == null) {
                PlayerData playerData = DataManager.getPlayerData(player);
                long cooldown = Methods.delayCommand(playerData.getBacktime(), ConfigValues.tpa_cooldown);
                if (cooldown == 0) {
                    long currentTime = System.currentTimeMillis() / 1000;
                    playerData.setTeleportRequestTime(currentTime);
                    DataManager.savePlayerData(playerData);
                    player.sendMessage(Methods.formatText("tpa.maessentials.request", TextFormatting.WHITE, requestedPlayer.getDisplayName()));
                    requestedPlayer.sendMessage(Methods.formatText("tpa.maessentials.request.target", TextFormatting.WHITE, player.getDisplayName()));
                    requestedPlayer.sendMessage(Methods.formatText("tpa.maessentials.request.target.accept", TextFormatting.WHITE));
                    requestedPlayer.sendMessage(Methods.formatText("tpa.maessentials.request.target.deny", TextFormatting.WHITE));
                    Teleport.teleportRequest(player, player, requestedPlayer, true);
                } else {
                    player.sendMessage(Methods.formatText("maessentials.cooldown", TextFormatting.RED, cooldown));
                }
            } else {
                player.sendMessage(Methods.formatText("tpa.maessentials.exist", TextFormatting.RED, requestedPlayer.getDisplayName()));
            }
        } else {
            player.sendMessage(Methods.formatText("tpa.maessentials.self", TextFormatting.RED));
        }
        return Command.SINGLE_SUCCESS;
    }
}