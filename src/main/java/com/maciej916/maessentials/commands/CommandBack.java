package com.maciej916.maessentials.commands;

import com.maciej916.maessentials.classes.Location;
import com.maciej916.maessentials.data.DataManager;
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
import net.minecraft.util.text.TranslationTextComponent;

public class CommandBack {

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder<CommandSource> builder = Commands.literal("back").requires(source -> source.hasPermissionLevel(0));
        builder.executes(context -> back(context));
        dispatcher.register(builder);
    }

    private static int back(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        Location playerData = DataManager.getPlayerData(player).getLastLocation();
        if (playerData != null) {
            Teleport.teleportPlayer(player, playerData, true);
            player.sendMessage(Methods.formatText("command.maessentials.back.success", TextFormatting.WHITE));
        } else {
            player.sendMessage(Methods.formatText("command.maessentials.back.failed", TextFormatting.DARK_RED));
        }
        return Command.SINGLE_SUCCESS;
    }
}