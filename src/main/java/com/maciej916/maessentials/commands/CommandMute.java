package com.maciej916.maessentials.commands;

import com.maciej916.maessentials.data.DataManager;
import com.maciej916.maessentials.data.PlayerData;
import com.maciej916.maessentials.libs.Log;
import com.maciej916.maessentials.libs.Methods;
import com.maciej916.maessentials.libs.Time;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.command.arguments.MessageArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TextFormatting;

public class CommandMute {

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder<CommandSource> builder = Commands.literal("mute").requires(source -> source.hasPermissionLevel(2));
        builder
            .executes(context -> mute(context))
                .then(Commands.argument("targetPlayer", EntityArgument.players())
                .executes(context -> mutePlayer(context))
                        .then(Commands.argument("time", StringArgumentType.word())
                        .executes(context -> mutePlayerTime(context))
                                .then(Commands.argument("reason", MessageArgument.message())
                                .executes(context -> mutePlayerReason(context)))));

        dispatcher.register(builder);
    }

    private static int mute(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        player.sendMessage(Methods.formatText("command.maessentials.player.provide", TextFormatting.DARK_RED));
        return Command.SINGLE_SUCCESS;
    }

    private static int mutePlayerTime(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        player.sendMessage(Methods.formatText("command.maessentials.mute.time", TextFormatting.DARK_RED));
        return Command.SINGLE_SUCCESS;
    }

    private static int mutePlayer(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        ServerPlayerEntity requestedPlayer = EntityArgument.getPlayer(context, "targetPlayer");
        String time = StringArgumentType.getString(context, "time");
        doMute(player, requestedPlayer, time, "No reason provided");
        return Command.SINGLE_SUCCESS;
    }

    private static int mutePlayerReason(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        ServerPlayerEntity requestedPlayer = EntityArgument.getPlayer(context, "targetPlayer");
        String time = StringArgumentType.getString(context, "time").toLowerCase();
        ITextComponent reason = MessageArgument.getMessage(context, "reason");
        doMute(player, requestedPlayer, time, reason.getString());
        return Command.SINGLE_SUCCESS;
    }

    private static void doMute(ServerPlayerEntity player, ServerPlayerEntity target, String time, String reason) {
        PlayerData playerData = DataManager.getPlayerData(target);

        long mutetime = Time.parseDate(time, true);
        if (time.equals("perm")) {
            mutetime = -1;
        }

        if (mutetime != 0) {
            long currentTime = System.currentTimeMillis() / 1000;
            if (playerData.getMuteTime() < currentTime) {
                playerData.mute(mutetime, reason);
                DataManager.savePlayerData(playerData);
                if (mutetime == -1) {
                    player.sendMessage(Methods.formatText("command.maessentials.mute.perm", TextFormatting.WHITE, target.getDisplayName(), reason));
                    target.sendMessage(Methods.formatText("event.maessentials.mute.perm", TextFormatting.RED, reason));
                } else {
                    String displayTime = Time.formatDate(mutetime - currentTime);
                    player.sendMessage(Methods.formatText("command.maessentials.mute", TextFormatting.WHITE, target.getDisplayName(), displayTime, reason));
                    target.sendMessage(Methods.formatText("event.maessentials.mute", TextFormatting.RED, displayTime, reason));
                }
            } else {
                player.sendMessage(Methods.formatText("command.maessentials.mute.already_muted", TextFormatting.WHITE, TextFormatting.WHITE, target.getDisplayName()));
            }
        } else {
            player.sendMessage(Methods.formatText("command.maessentials.illegal_date", TextFormatting.DARK_RED));
        }
    }
}
