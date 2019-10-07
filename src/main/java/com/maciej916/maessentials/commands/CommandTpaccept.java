package com.maciej916.maessentials.commands;

import com.maciej916.maessentials.classes.Location;
import com.maciej916.maessentials.classes.Tpa;
import com.maciej916.maessentials.utils.Teleport;
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
        ArrayList<Tpa> tpaRequests = Teleport.getTpaRequests(player);
        if (tpaRequests.size() == 1) {
            Tpa thisTpa = tpaRequests.get(0);
            handleTpa(thisTpa, player);
        } else if (tpaRequests.size() > 1) {
            player.sendMessage(new TranslationTextComponent("command.maessentials.tpaccept.specifyplayer"));
        } else {
            player.sendMessage(new TranslationTextComponent("command.maessentials.tpaccept.norequest"));
        }
        return Command.SINGLE_SUCCESS;
    }

    private static int tpacceptArgs(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        ServerPlayerEntity requestedPlayer = EntityArgument.getPlayer(context, "targetPlayer");
        ArrayList<Tpa> tpaRequests = Teleport.getTpaRequests(player);
        if (tpaRequests.size() != 0) {
            Tpa thisTpa = Teleport.findTpa(requestedPlayer, requestedPlayer, player);
            if (thisTpa != null) {
                handleTpa(thisTpa, player);
            } else {
                player.sendMessage(new TranslationTextComponent("command.maessentials.tpa.notfound"));
            }
        } else {
            player.sendMessage(new TranslationTextComponent("command.maessentials.tpa.norequest"));
        }
        return Command.SINGLE_SUCCESS;
    }

    private static void handleTpa(Tpa thisTpa, ServerPlayerEntity player) {
        thisTpa.getRequestPlayer().sendMessage(new TranslationTextComponent("command.maessentials.tpaccept.request", thisTpa.getTargetPlayer().getDisplayName(), true));
        thisTpa.getTargetPlayer().sendMessage(new TranslationTextComponent("command.maessentials.tpaccept.target", thisTpa.getRequestPlayer().getDisplayName(), true));
        if (thisTpa.getTargetPlayer() == null) {
             player.sendMessage(new TranslationTextComponent("command.maessentials.player.notfound"));
        } else {
            Teleport.teleportPlayer(thisTpa.getRequestPlayer(), new Location(thisTpa.getTargetPlayer()), true);
            Teleport.removeTpa(thisTpa);
        }
    }
}