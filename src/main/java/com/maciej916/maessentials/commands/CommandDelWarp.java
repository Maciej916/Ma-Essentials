package com.maciej916.maessentials.commands;

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

public class CommandDelWarp {
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder<CommandSource> builder = Commands.literal("delwarp").requires(source -> source.hasPermissionLevel(2));
        builder
                .executes(context -> warp(context))
                .then(Commands.argument("WarpName", StringArgumentType.word())
                        .suggests(WarpManager.WARP_SUGGEST)
                        .executes(context -> warpArgs(context)));
        dispatcher.register(builder);
    }

    private static int warp(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        player.sendMessage(new TranslationTextComponent("command.maessentials.delwarp.specifyname"));
        return Command.SINGLE_SUCCESS;
    }

    private static int warpArgs(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        String args = StringArgumentType.getString(context, "WarpName").toString().toLowerCase();
        if (WarpManager.delWarp(args)) {
            player.sendMessage(new TranslationTextComponent("command.maessentials.delwarp.done", args, true));
        } else {
            player.sendMessage(new TranslationTextComponent("command.maessentials.warp.notexist", args, true));
        }
        return Command.SINGLE_SUCCESS;
    }
}