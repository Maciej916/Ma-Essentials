package com.maciej916.maessentials.commands;

import com.maciej916.maessentials.classes.Location;
import com.maciej916.maessentials.data.DataManager;
import com.maciej916.maessentials.libs.Methods;
import com.maciej916.maessentials.libs.Teleport;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TextFormatting;

import java.util.Set;

public class CommandWarp {
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder<CommandSource> builder = Commands.literal("warp").requires(source -> source.hasPermissionLevel(0));
        builder
            .executes(context -> warp(context))
                .then(Commands.argument("warpName", StringArgumentType.string())
                    .suggests(Methods.WARP_SUGGEST)
                    .executes(context -> warpArgs(context)));
        dispatcher.register(builder);
    }

    private static int warp(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();

        Set<String> warps =  DataManager.getWarpData().getWarps().keySet();
        StringBuilder warpString = new StringBuilder();
        if (warps.size() != 0) {
            int i = 1;
            for (String name : warps) {
                warpString.append(name);
                if (warps.size() != i) {
                    warpString.append(", ");
                    i++;
                }
            }
        } else {
            warpString.append("-");
        }

        player.sendMessage(Methods.formatText("command.maessentials.warp.list", TextFormatting.WHITE, warps.size(), warpString));
        return Command.SINGLE_SUCCESS;
    }

    private static int warpArgs(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        String warpName = StringArgumentType.getString(context, "warpName").toString().toLowerCase();
        Location warpLocation = DataManager.getWarpData().getWarps().get(warpName);
        if (warpLocation != null) {
            player.sendMessage(Methods.formatText("command.maessentials.warp.teleported", TextFormatting.WHITE, warpName));
            Teleport.teleportPlayer(player, warpLocation, true);
        } else {
            player.sendMessage(Methods.formatText("command.maessentials.warp.notexist", TextFormatting.DARK_RED, warpName));
        }
        return Command.SINGLE_SUCCESS;
    }
}
