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
import net.minecraft.world.GameType;

public class CommandHeal {

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder<CommandSource> builder = Commands.literal("heal").requires(source -> source.hasPermissionLevel(2));
        builder
                .executes(context -> heal(context))
                        .then(Commands.argument("targetPlayer", EntityArgument.players())
                                .executes(context -> healArgs(context)));
        dispatcher.register(builder);
    }

    private static int heal(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        doHeal(player, player);
        return Command.SINGLE_SUCCESS;
    }

    private static int healArgs(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        ServerPlayerEntity targetPlayer = EntityArgument.getPlayer(context, "targetPlayer");
        doHeal(player, targetPlayer);
        return Command.SINGLE_SUCCESS;
    }

    private static void doHeal(ServerPlayerEntity player, ServerPlayerEntity target) {
        if (target.interactionManager.getGameType() == GameType.CREATIVE || target.interactionManager.getGameType() == GameType.SPECTATOR) {
            if (player == target) {
                target.sendMessage(Methods.formatText("maessentials.invaild_gamemode"));
            } else {
                target.sendMessage(Methods.formatText("maessentials.invaild_gamemode.player", target.getDisplayName()));
            }
            return;
        }

        target.setHealth(player.getMaxHealth());
        target.getFoodStats().setFoodLevel(20);
        target.extinguish();
        target.clearActivePotions();

        if (player == target) {
            target.sendMessage(Methods.formatText("heal.maessentials.self"));
        } else {
            player.sendMessage(Methods.formatText("heal.maessentials.player", target.getDisplayName()));
            target.sendMessage(Methods.formatText("heal.maessentials.player.target", player.getDisplayName()));
        }
    }
}