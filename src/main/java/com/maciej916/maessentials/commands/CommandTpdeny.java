package com.maciej916.maessentials.commands;

import com.maciej916.maessentials.classes.TeleportRequest;
import com.maciej916.maessentials.data.PlayerData;
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
import net.minecraft.util.text.TranslationTextComponent;

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
        ArrayList<TeleportRequest> tpaRequests = PlayerData.getTeleportRequests(player);
        if (tpaRequests.size() == 1) {
            TeleportRequest thisTpa = tpaRequests.get(0);
            handleTpdeny(thisTpa, player);
        } else if (tpaRequests.size() > 1) {
            player.sendMessage(new TranslationTextComponent("command.maessentials.tpdeny.specifyplayer"));
        } else {
            player.sendMessage(new TranslationTextComponent("command.maessentials.tpa.norequest"));
        }
        return Command.SINGLE_SUCCESS;
    }

    private static int tpdenyArgs(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        ServerPlayerEntity requestedPlayer = EntityArgument.getPlayer(context, "targetPlayer");
        ArrayList<TeleportRequest> tpaRequests = PlayerData.getTeleportRequests(player);
        if (tpaRequests.size() != 0) {
            TeleportRequest thisTpa = PlayerData.findTeleportRequest(requestedPlayer, player);
            if (thisTpa != null) {
                handleTpdeny(thisTpa, player);
            } else {
                player.sendMessage(new TranslationTextComponent("command.maessentials.tpa.notfound"));
            }
        } else {
            player.sendMessage(new TranslationTextComponent("command.maessentials.tpa.norequest"));
        }
        return Command.SINGLE_SUCCESS;
    }

    private static void handleTpdeny(TeleportRequest tpa, ServerPlayerEntity player) {
        tpa.getTpPlayer().sendMessage(new TranslationTextComponent("command.maessentials.tpdeny.request", tpa.getTpPlayer().getDisplayName(), true));
        tpa.getTpTargetPlayer().sendMessage(new TranslationTextComponent("command.maessentials.tpdeny.target", tpa.getTpTargetPlayer().getDisplayName(), true));
        PlayerData.denyTeleportRequest(tpa);
    }
}
