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

public class CommandTpdeny {
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder<CommandSource> builder = Commands.literal("tpdeny").requires(source -> source.hasPermissionLevel(0));
        builder
                .executes(context -> tpdeny(context))
                        .then(Commands.argument("targetPlayer", EntityArgument.players())
                        .executes(context -> tpdenyArgs(context)));
        dispatcher.register(builder);
    }

    private static int tpdeny(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        ArrayList<Teleport> teleports = Teleport.getPlayerTeleports(player);
        if (teleports.size() == 1) {
            Teleport tpr = teleports.get(0);
            Teleport.declineTrade(tpr);
        } else if (teleports.size() > 1) {
            player.sendMessage(Methods.formatText("maessentials.specify.player"));
        } else {
            player.sendMessage(Methods.formatText("tpa.maessentials.no_request"));
        }
        return Command.SINGLE_SUCCESS;
    }

    private static int tpdenyArgs(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        ServerPlayerEntity targetPlayer = EntityArgument.getPlayer(context, "targetPlayer");
        Teleport tpr = Teleport.findTeleportRequest(player, targetPlayer);
        if (tpr != null) {
            Teleport.declineTrade(tpr);
        } else {
            player.sendMessage(Methods.formatText("tpa.maessentials.not_found"));
        }
        return Command.SINGLE_SUCCESS;
    }
}
