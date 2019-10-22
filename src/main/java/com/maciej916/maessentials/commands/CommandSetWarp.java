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
import net.minecraft.util.text.TranslationTextComponent;

public class CommandSetWarp {
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder<CommandSource> builder = Commands.literal("setwarp").requires(source -> source.hasPermissionLevel(2));
        builder
            .executes(context -> warp(context))
            .then(Commands.argument("warpName", StringArgumentType.string())
                    .executes(context -> warpArgs(context)));
        dispatcher.register(builder);
    }

    private static int warp(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        player.sendMessage(new TranslationTextComponent("warp.maessentials.specify_name"));
        return Command.SINGLE_SUCCESS;
    }

    private static int warpArgs(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        String warpName = StringArgumentType.getString(context, "warpName").toString().toLowerCase();
        if (DataManager.getWarpData().setWarp(player, warpName)) {
            player.sendMessage(new TranslationTextComponent("setwarp.maessentials.success", warpName, true));
        } else {
            player.sendMessage(Methods.formatText("setwarp.maessentials.exist", TextFormatting.RED, warpName));
        }
        return Command.SINGLE_SUCCESS;
    }
}
