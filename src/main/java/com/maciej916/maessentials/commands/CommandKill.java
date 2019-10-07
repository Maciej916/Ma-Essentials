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

public class CommandKill {

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher
                .register(Commands.literal("kill")
                        .requires(source -> source.hasPermissionLevel(2))
                        .executes(context -> execute(context)));
    }

    private static int execute(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        player.setHealth(0);
        player.sendMessage(new TranslationTextComponent("command.maessentials.kill.grave"));
        Teleport.setPlayerLastLoc(player);
        return Command.SINGLE_SUCCESS;
    }
}
