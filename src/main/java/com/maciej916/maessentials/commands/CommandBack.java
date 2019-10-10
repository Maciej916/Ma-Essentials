package com.maciej916.maessentials.commands;

import com.maciej916.maessentials.classes.Location;
import com.maciej916.maessentials.data.PlayerData;
import com.maciej916.maessentials.libs.Teleport;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;

public class CommandBack {

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder<CommandSource> builder = Commands.literal("back").requires(source -> source.hasPermissionLevel(0));
        builder.executes(context -> back(context));
        dispatcher.register(builder);
    }

    private static int back(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        Location lastLoc = PlayerData.getPlayerLastLoc(player);
        if (lastLoc != null) {
            Teleport.teleportPlayer(player, lastLoc, true);
            player.sendMessage(new TranslationTextComponent("command.maessentials.back.success"));
        } else {
            player.sendMessage(new TranslationTextComponent("command.maessentials.back.failed"));
        }
        return Command.SINGLE_SUCCESS;
    }
}