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
import net.minecraft.util.text.TranslationTextComponent;
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
        if (player.interactionManager.getGameType() == GameType.SURVIVAL || player.interactionManager.getGameType() == GameType.ADVENTURE) {
            player.setHealth(player.getMaxHealth());
            player.getFoodStats().setFoodLevel(20);
            player.extinguish();
            player.clearActivePotions();
            player.sendMessage(Methods.formatText("heal.maessentials.self"));
        } else {
            player.sendMessage(Methods.formatText("maessentials.invaild_gamemode"));
        }
        return Command.SINGLE_SUCCESS;
    }

    private static int healArgs(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        ServerPlayerEntity targetPlayer = EntityArgument.getPlayer(context, "targetPlayer");
        if (targetPlayer.interactionManager.getGameType() == GameType.SURVIVAL || targetPlayer.interactionManager.getGameType() == GameType.ADVENTURE) {
            if (targetPlayer == player) {
                heal(context);
            } else {
                targetPlayer.setHealth(player.getMaxHealth());
                targetPlayer.getFoodStats().setFoodLevel(20);
                targetPlayer.extinguish();
                targetPlayer.clearActivePotions();
                player.sendMessage(Methods.formatText("heal.maessentials.player", targetPlayer.getDisplayName()));
                targetPlayer.sendMessage(Methods.formatText("heal.maessentials.player.target", player.getDisplayName()));
            }
        } else {
            player.sendMessage(Methods.formatText("maessentials.invaild_gamemode.player", targetPlayer.getDisplayName()));
        }
        return Command.SINGLE_SUCCESS;
    }
}