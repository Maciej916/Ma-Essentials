package com.maciej916.maessentials.commands;

import com.maciej916.maessentials.utils.Teleport;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;

public class CommandBack {

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher
                .register(Commands.literal("back")
                        .requires(source -> source.hasPermissionLevel(0))
                        .executes(context -> execute(context)));
    }

    private static int execute(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        if (Teleport.backPlayer(player)) {
            player.sendMessage(new TranslationTextComponent("command.maessentials.back.success"));
        } else {
            player.sendMessage(new TranslationTextComponent("command.maessentials.back.failed"));
        }
        return Command.SINGLE_SUCCESS;
    }
}