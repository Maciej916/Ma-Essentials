package com.maciej916.maessentials.commands;

import com.maciej916.maessentials.libs.Methods;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.MessageArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class CommandKickall {
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder<CommandSource> builder = Commands.literal("kickall").requires(source -> source.hasPermissionLevel(2));
        builder
                .executes(context -> kickall(context))
                        .then(Commands.argument("reason", MessageArgument.message())
                        .executes(context -> kickallArgs(context)));
        dispatcher.register(builder);
    }

    private static int kickall(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        doKickall(player, "No reason provided");
        return Command.SINGLE_SUCCESS;
    }

    private static int kickallArgs(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        ITextComponent reason = MessageArgument.getMessage(context, "reason");
        doKickall(player, reason.toString());
        return Command.SINGLE_SUCCESS;
    }

    private static void doKickall(ServerPlayerEntity player, String reason) {
        for (ServerPlayerEntity tp : player.server.getPlayerList().getPlayers()) {
            if (player.getUniqueID() != tp.getUniqueID()) {
                tp.connection.disconnect(new StringTextComponent(reason));
            }
        }
        player.sendMessage(Methods.formatText("kickall.maessentials.success"));
    }
}
