package com.maciej916.maessentials.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;

public class CommandHeal {

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher
                .register(Commands.literal("heal")
                        .requires(source -> source.hasPermissionLevel(2))
                        .executes(context -> execute(context)));
    }

    private static int execute(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        player.setHealth(player.getMaxHealth());
        player.getFoodStats().setFoodLevel(20);
        player.extinguish();
        player.clearActivePotions();
        player.sendMessage(new TranslationTextComponent("command.maessentials.heal.done"));
        return Command.SINGLE_SUCCESS;
    }
}
