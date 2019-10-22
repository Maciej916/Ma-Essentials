package com.maciej916.maessentials.commands;

import com.maciej916.maessentials.data.DataManager;
import com.maciej916.maessentials.libs.Methods;
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

public class CommandDelWarp {
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder<CommandSource> builder = Commands.literal("delwarp").requires(source -> source.hasPermissionLevel(2));
        builder
            .executes(context -> warp(context))
            .then(Commands.argument("warpName", StringArgumentType.string())
                .suggests(Methods.WARP_SUGGEST)
                .executes(context -> warpArgs(context)));
        dispatcher.register(builder);
    }

    private static int warp(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        player.sendMessage(Methods.formatText("warp.maessentials.specify_name", TextFormatting.RED));
        return Command.SINGLE_SUCCESS;
    }

    private static int warpArgs(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        String warpName = StringArgumentType.getString(context, "warpName").toString().toLowerCase();
        if (DataManager.getWarpData().delWarp(warpName)) {
            player.sendMessage(Methods.formatText("delwarp.maessentials.success", TextFormatting.WHITE, warpName));
        } else {
            player.sendMessage(Methods.formatText("warp.maessentials.not_exist", TextFormatting.RED, warpName));
        }
        return Command.SINGLE_SUCCESS;
    }
}