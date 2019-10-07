package com.maciej916.maessentials.commands;

import com.maciej916.maessentials.classes.Warp;
import com.maciej916.maessentials.utils.Teleport;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.maciej916.maessentials.managers.WarpManager;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.HashMap;
import java.util.Map;

public class CommandWarp {
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder<CommandSource> builder = Commands.literal("warp").requires(source -> source.hasPermissionLevel(0));
        builder
                .executes(context -> warp(context))
                .then(Commands.argument("WarpName", StringArgumentType.word())
                        .suggests(WarpManager.WARP_SUGGEST)
                        .executes(context -> warpArgs(context)));
        dispatcher.register(builder);
    }

    private static int warp(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        HashMap<String, Warp> warps =  WarpManager.getAllWarps();
        StringBuilder warpString = new StringBuilder();
        if (warps.size() != 0) {
            int i = 1;
            for (Map.Entry me : warps.entrySet()) {
                warpString.append(me.getKey());
                if (warps.size() != i) {
                    warpString.append(", ");
                    i++;
                }
            }
        } else {
            warpString.append("-");
        }

        player.sendMessage(new TranslationTextComponent("command.maessentials.warp.list",warps.size(), warpString, true));
        return Command.SINGLE_SUCCESS;
    }

    private static int warpArgs(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        String args = StringArgumentType.getString(context, "WarpName").toString().toLowerCase();
        Warp thisWarp = WarpManager.getWarp(args);
        if (thisWarp != null) {
            Teleport.teleportPlayer(player, thisWarp.getWarpLocation(), true);
            player.sendMessage(new TranslationTextComponent("command.maessentials.warp.teleported", args, true));
        } else {
            player.sendMessage(new TranslationTextComponent("command.maessentials.warp.notexist", args, true));
        }
        return Command.SINGLE_SUCCESS;
    }
}
