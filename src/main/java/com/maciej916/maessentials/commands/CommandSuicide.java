package com.maciej916.maessentials.commands;

import com.maciej916.maessentials.classes.Location;
import com.maciej916.maessentials.config.ConfigValues;
import com.maciej916.maessentials.data.DataManager;
import com.maciej916.maessentials.data.PlayerData;
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

public class CommandSuicide {
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        if (ConfigValues.suicide_enable_player) {
            LiteralArgumentBuilder<CommandSource> builder = Commands.literal("suicide").requires(source -> source.hasPermissionLevel(0));
            builder
                    .executes(context -> suicide(context));
            dispatcher.register(builder);
        }

        LiteralArgumentBuilder<CommandSource> builder2 = Commands.literal("suicide").requires(source -> source.hasPermissionLevel(2));
        builder2
                .then(Commands.argument("targetPlayer", EntityArgument.players())
                        .executes(context -> suicideArgs(context)));
        dispatcher.register(builder2);
    }

    private static int suicide(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        PlayerData playerData = DataManager.getPlayerData(player);
        long cooldown = Methods.delayCommand(playerData.getSuicideTime(), ConfigValues.suicide_player_cooldown);
        if (cooldown == 0) {
            long currentTime = System.currentTimeMillis() / 1000;
            playerData.setSuicideTime(currentTime);
            DataManager.savePlayerData(playerData);
            doSuicide(player, player);
        } else {
            player.sendMessage(Methods.formatText("maessentials.cooldown", TextFormatting.RED, cooldown));
        }
        return Command.SINGLE_SUCCESS;
    }

    private static int suicideArgs(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        ServerPlayerEntity requestedPlayer = EntityArgument.getPlayer(context, "targetPlayer");
        doSuicide(player, requestedPlayer);
        return Command.SINGLE_SUCCESS;
    }

    private static void doSuicide(ServerPlayerEntity player, ServerPlayerEntity targerPlayer) {
        if (player == targerPlayer) {
            player.setHealth(0);
            player.sendMessage(Methods.formatText("suicide.maessentials.self", TextFormatting.WHITE));
            DataManager.getPlayerData(player).setLastLocation(new Location(player));
        } else {
            targerPlayer.setHealth(0);
            DataManager.getPlayerData(player).setLastLocation(new Location(player));
            player.sendMessage(Methods.formatText("suicide.maessentials.player", TextFormatting.WHITE, targerPlayer.getDisplayName()));
            targerPlayer.sendMessage(Methods.formatText("suicide.maessentials.player.target", TextFormatting.WHITE, player.getDisplayName()));
        }
    }
}