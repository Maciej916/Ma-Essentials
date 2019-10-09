package com.maciej916.maessentials.commands;

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
import net.minecraft.util.text.TranslationTextComponent;

public class CommandSuicide {
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder<CommandSource> builder = Commands.literal("suicide").requires(source -> source.hasPermissionLevel(0));
        builder
                .executes(context -> kill(context));
        dispatcher.register(builder);


        LiteralArgumentBuilder<CommandSource> builder2 = Commands.literal("suicide").requires(source -> source.hasPermissionLevel(2));
        builder2
                .then(Commands.argument("targetPlayer", EntityArgument.players())
                        .executes(context -> killArgs(context)));
        dispatcher.register(builder2);
    }

    private static int kill(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        player.setHealth(0);
        player.sendMessage(new TranslationTextComponent("command.maessentials.suicide.self"));
        Teleport.setPlayerLastLoc(player);
        return Command.SINGLE_SUCCESS;
    }

    private static int killArgs(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        ServerPlayerEntity requestedPlayer = EntityArgument.getPlayer(context, "targetPlayer");
        if (requestedPlayer == player) {
            kill(context);
        } else {
            if (requestedPlayer != null) {
                requestedPlayer.setHealth(0);
                Teleport.setPlayerLastLoc(requestedPlayer);
                player.sendMessage(new TranslationTextComponent("command.maessentials.suicide.player", requestedPlayer.getDisplayName(), true));
                requestedPlayer.sendMessage(new TranslationTextComponent("command.maessentials.suicide.killed", player.getDisplayName(), true));
            } else {
                player.sendMessage(new TranslationTextComponent("command.maessentials.player.notfound"));
            }
        }
        return Command.SINGLE_SUCCESS;
    }
}