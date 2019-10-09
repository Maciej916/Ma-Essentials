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
        player.setHealth(player.getMaxHealth());
        player.getFoodStats().setFoodLevel(20);
        player.extinguish();
        player.clearActivePotions();
        player.sendMessage(new TranslationTextComponent("command.maessentials.heal.self"));
        return Command.SINGLE_SUCCESS;
    }

    private static int healArgs(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        ServerPlayerEntity requestedPlayer = EntityArgument.getPlayer(context, "targetPlayer");
        if (requestedPlayer == player) {
            heal(context);
        } else {
            requestedPlayer.setHealth(player.getMaxHealth());
            requestedPlayer.getFoodStats().setFoodLevel(20);
            requestedPlayer.extinguish();
            requestedPlayer.clearActivePotions();

            player.sendMessage(new TranslationTextComponent("command.maessentials.heal.player", requestedPlayer.getDisplayName(), true));
            requestedPlayer.sendMessage(new TranslationTextComponent("command.maessentials.heal.healed", player.getDisplayName(), true));
        }
        return Command.SINGLE_SUCCESS;
    }
}