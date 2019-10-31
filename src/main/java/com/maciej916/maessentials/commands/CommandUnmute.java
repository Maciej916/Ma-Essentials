package com.maciej916.maessentials.commands;

import com.maciej916.maessentials.data.DataManager;
import com.maciej916.maessentials.data.PlayerData;
import com.maciej916.maessentials.libs.Log;
import com.maciej916.maessentials.libs.Methods;
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

public class CommandUnmute {

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder<CommandSource> builder = Commands.literal("unmute").requires(source -> source.hasPermissionLevel(2));
        builder
            .executes(context -> unmute(context))
            .then(Commands.argument("targetPlayer", EntityArgument.players())
                .executes(context -> unmuteArgs(context)));

        dispatcher.register(builder);
    }

    private static int unmute(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        player.sendMessage(Methods.formatText("maessentials.provide.player"));
        return Command.SINGLE_SUCCESS;
    }

    private static int unmuteArgs(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        ServerPlayerEntity requestedPlayer = EntityArgument.getPlayer(context, "targetPlayer");
        PlayerData playerData = DataManager.getPlayerData(requestedPlayer);

        long currentTime = System.currentTimeMillis() / 1000;
        if (playerData.getMuteTime() == -1 || playerData.getMuteTime() > currentTime) {
            player.sendMessage(Methods.formatText("unmmute.maessentials.success", requestedPlayer.getDisplayName()));
            requestedPlayer.sendMessage(Methods.formatText("unmmute.maessentials.success.target"));
            playerData.unmute();
            DataManager.savePlayerData(playerData);
        } else {
            player.sendMessage(Methods.formatText("unmmute.maessentials.not_muted", requestedPlayer.getDisplayName()));
        }
        return Command.SINGLE_SUCCESS;
    }
}
