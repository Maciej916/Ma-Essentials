package com.maciej916.maessentials.commands;

import com.maciej916.maessentials.classes.teleport.TeleportRequest;
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
        ArrayList<TeleportRequest> requests = Teleport.findRequest(player);
        if (requests.size() == 1) {
            Teleport.acceptRequest(requests.get(0));
        } else if (requests.size() > 1) {
            player.sendMessage(Methods.formatText("maessentials.specify.player"));
        } else {
            player.sendMessage(Methods.formatText("tpa.maessentials.no_request"));
        }
        return Command.SINGLE_SUCCESS;
    }

    private static int tpacceptArgs(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        ServerPlayerEntity targetPlayer = EntityArgument.getPlayer(context, "targetPlayer");
        TeleportRequest tpR = Teleport.findRequest(player, targetPlayer);
        if (tpR != null) {
            Teleport.acceptRequest(tpR);
        } else {
            player.sendMessage(Methods.formatText("tpa.maessentials.not_found"));
        }
        return Command.SINGLE_SUCCESS;
    }
}