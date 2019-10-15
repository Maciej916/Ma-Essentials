package com.maciej916.maessentials.commands;

import com.maciej916.maessentials.classes.TeleportRequest;
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
//        ArrayList<TeleportRequest> tpaRequests = PlayerData.getTeleportRequests(player);
//        if (tpaRequests.size() == 1) {
//            TeleportRequest thisTpa = tpaRequests.get(0);
//            handleTpdeny(thisTpa, player);
//        } else if (tpaRequests.size() > 1) {
//            player.sendMessage(Methods.formatText("command.maessentials.tpdeny.specifyplayer", TextFormatting.DARK_RED));
//        } else {
//            player.sendMessage(Methods.formatText("command.maessentials.tpa.norequest", TextFormatting.DARK_RED));
//        }
        return Command.SINGLE_SUCCESS;
    }

    private static int tpdenyArgs(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        ServerPlayerEntity requestedPlayer = EntityArgument.getPlayer(context, "targetPlayer");
//        ArrayList<TeleportRequest> tpaRequests = PlayerData.getTeleportRequests(player);
//        if (tpaRequests.size() != 0) {
//            TeleportRequest thisTpa = PlayerData.findTeleportRequest(requestedPlayer, player);
//            if (thisTpa != null) {
//                handleTpdeny(thisTpa, player);
//            } else {
//                player.sendMessage(Methods.formatText("command.maessentials.tpa.notfound", TextFormatting.DARK_RED));
//            }
//        } else {
//            player.sendMessage(Methods.formatText("command.maessentials.tpa.norequest", TextFormatting.DARK_RED));
//        }
        return Command.SINGLE_SUCCESS;
    }

    private static void handleTpdeny(TeleportRequest tpa, ServerPlayerEntity player) {
        tpa.getTpPlayer().sendMessage(new TranslationTextComponent("command.maessentials.tpdeny.request", tpa.getTpPlayer().getDisplayName(), true));
        tpa.getTpTargetPlayer().sendMessage(new TranslationTextComponent("command.maessentials.tpdeny.target", tpa.getTpTargetPlayer().getDisplayName(), true));
//        PlayerData.denyTeleportRequest(tpa);
    }
}
