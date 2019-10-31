package com.maciej916.maessentials.commands;

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
import net.minecraft.world.GameType;

public class CommandGod {

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder<CommandSource> builder = Commands.literal("god").requires(source -> source.hasPermissionLevel(2));
        builder
                .executes(context -> god(context))
                .then(Commands.argument("targetPlayer", EntityArgument.players())
                        .executes(context -> godArgs(context)));

        dispatcher.register(builder);
    }

    private static int god(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        godManage(player, player);
        return Command.SINGLE_SUCCESS;
    }

    private static int godArgs(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        ServerPlayerEntity requestedPlayer = EntityArgument.getPlayer(context, "targetPlayer");
        godManage(player, requestedPlayer);
        return Command.SINGLE_SUCCESS;
    }

    private static void godManage(ServerPlayerEntity player, ServerPlayerEntity targetPlayer) {
        if (targetPlayer.interactionManager.getGameType() == GameType.SURVIVAL || targetPlayer.interactionManager.getGameType() == GameType.ADVENTURE) {
            if (targetPlayer.abilities.disableDamage) {
                targetPlayer.abilities.disableDamage = false;
                if (player == targetPlayer) {
                    player.sendMessage(Methods.formatText("god.maessentials.self.disabled"));
                } else {
                    player.sendMessage(Methods.formatText("god.maessentials.player.disabled", targetPlayer.getDisplayName()));
                    targetPlayer.sendMessage(Methods.formatText("god.maessentials.self.disabled"));
                }
            } else {
                targetPlayer.abilities.disableDamage = true;

                if (player == targetPlayer) {
                    player.sendMessage(Methods.formatText("god.maessentials.self.enabled"));
                } else {
                    player.sendMessage(Methods.formatText("god.maessentials.player.enabled", targetPlayer.getDisplayName()));
                    targetPlayer.sendMessage(Methods.formatText("god.maessentials.self.enabled"));
                }
            }
            targetPlayer.sendPlayerAbilities();
        } else {
            player.sendMessage(Methods.formatText("maessentials.invaild_gamemode.player", targetPlayer.getDisplayName()));
        }
    }
}
