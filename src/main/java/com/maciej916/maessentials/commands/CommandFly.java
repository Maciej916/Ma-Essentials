package com.maciej916.maessentials.commands;

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
import net.minecraft.world.GameType;

public class CommandFly {

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder<CommandSource> builder = Commands.literal("fly").requires(source -> source.hasPermissionLevel(2));
        builder
                .executes(context -> fly(context))
                    .then(Commands.argument("targetPlayer", EntityArgument.players())
                        .executes(context -> flyArgs(context)));

        dispatcher.register(builder);
    }

    private static int fly(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        flyManage(player, player);
        return Command.SINGLE_SUCCESS;
    }

    private static int flyArgs(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        ServerPlayerEntity requestedPlayer = EntityArgument.getPlayer(context, "targetPlayer");
        flyManage(player, requestedPlayer);
        return Command.SINGLE_SUCCESS;
    }

    private static void flyManage(ServerPlayerEntity player, ServerPlayerEntity targetPlayer) {
        if (targetPlayer.interactionManager.getGameType() == GameType.SURVIVAL || targetPlayer.interactionManager.getGameType() == GameType.ADVENTURE) {
            if (targetPlayer.abilities.allowFlying) {
                targetPlayer.abilities.allowFlying = false;
                targetPlayer.abilities.isFlying = false;

                if (player == targetPlayer) {
                    player.sendMessage(new TranslationTextComponent("command.maessentials.fly.disabled.self"));
                } else {
                    player.sendMessage(new TranslationTextComponent("command.maessentials.fly.disabled.other", targetPlayer.getDisplayName(), true));
                    targetPlayer.sendMessage(new TranslationTextComponent("command.maessentials.fly.disabled.self"));
                }
            } else {
                targetPlayer.abilities.allowFlying = true;

                if (player == targetPlayer) {
                    player.sendMessage(new TranslationTextComponent("command.maessentials.fly.enabled.self"));
                } else {
                    player.sendMessage(new TranslationTextComponent("command.maessentials.fly.enabled.other", targetPlayer.getDisplayName(), true));
                    targetPlayer.sendMessage(new TranslationTextComponent("command.maessentials.fly.enabled.self"));
                }
            }
            targetPlayer.sendPlayerAbilities();
        } else {
            targetPlayer.sendMessage(new TranslationTextComponent("command.maessentials.fly.invaildgamemode"));
        }
    }
}

