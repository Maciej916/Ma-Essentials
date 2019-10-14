package com.maciej916.maessentials.commands;

import com.maciej916.maessentials.classes.TeleportRequest;
import com.maciej916.maessentials.data.PlayerData;
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
        ArrayList<TeleportRequest> tpaRequests = PlayerData.getTeleportRequests(player);
        if (tpaRequests.size() == 1) {
            TeleportRequest tpa = tpaRequests.get(0);
            PlayerData.acceptTeleportRequest(tpa);
        } else if (tpaRequests.size() > 1) {
            player.sendMessage(Methods.formatText("command.maessentials.tpaccept.specifyplayer", TextFormatting.DARK_RED));
        } else {
            player.sendMessage(Methods.formatText("command.maessentials.tpaccept.norequest", TextFormatting.DARK_RED));
        }
        return Command.SINGLE_SUCCESS;
    }

    private static int tpacceptArgs(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        ServerPlayerEntity requestedPlayer = EntityArgument.getPlayer(context, "targetPlayer");
        ArrayList<TeleportRequest> tpaRequests = PlayerData.getTeleportRequests(player);
        if (tpaRequests.size() != 0) {
            TeleportRequest tpa = PlayerData.findTeleportRequest(player, requestedPlayer);
            if (tpa != null) {
                PlayerData.acceptTeleportRequest(tpa);
            } else {
                player.sendMessage(Methods.formatText("command.maessentials.tpa.notfound", TextFormatting.DARK_RED));
            }
        } else {
            player.sendMessage(Methods.formatText("command.maessentials.tpa.norequest", TextFormatting.DARK_RED));
        }
        return Command.SINGLE_SUCCESS;
    }
}