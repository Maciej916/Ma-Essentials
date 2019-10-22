package com.maciej916.maessentials.commands;

import com.maciej916.maessentials.libs.Methods;
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
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;

public class CommandTpaccept {
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder<CommandSource> builder = Commands.literal("tpaccept").requires(source -> source.hasPermissionLevel(0));
        builder
            .executes(context -> tpaccept(context))
            .then(Commands.argument("targetPlayer", EntityArgument.players())
                .executes(context -> tpacceptArgs(context)));
        dispatcher.register(builder);
    }

    private static int tpaccept(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        ArrayList<Teleport> teleports = Teleport.getPlayerTeleports(player);
        if (teleports.size() == 1) {
            Teleport tpr = teleports.get(0);
            Teleport.acceptTeleport(tpr);
        } else if (teleports.size() > 1) {
            player.sendMessage(Methods.formatText("maessentials.specify.player", TextFormatting.RED));
        } else {
            player.sendMessage(Methods.formatText("tpa.maessentials.no_request", TextFormatting.RED));
        }
        return Command.SINGLE_SUCCESS;
    }

    private static int tpacceptArgs(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        ServerPlayerEntity requestedPlayer = EntityArgument.getPlayer(context, "targetPlayer");
        Teleport tpr = Teleport.findTeleportRequest(player, requestedPlayer);
        if (tpr != null) {
            Teleport.acceptTeleport(tpr);
        } else {
            player.sendMessage(Methods.formatText("tpa.maessentials.not_found", TextFormatting.RED));
        }
        return Command.SINGLE_SUCCESS;
    }
}