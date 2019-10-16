package com.maciej916.maessentials.commands;

import com.maciej916.maessentials.data.DataManager;
import com.maciej916.maessentials.data.PlayerData;
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

public class CommandDelHome {
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder<CommandSource> builder = Commands.literal("delhome").requires(source -> source.hasPermissionLevel(0));
        builder
            .executes(context -> delHome(context))
            .then(Commands.argument("homeName", StringArgumentType.string())
                .suggests(Methods.HOME_SUGGEST)
                .executes(context -> delHomeArgs(context)));
        dispatcher.register(builder);
    }

    private static int delHome(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        player.sendMessage(Methods.formatText("command.maessentials.delhome.specifyname", TextFormatting.DARK_RED));
        return Command.SINGLE_SUCCESS;
    }

    private static int delHomeArgs(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        String homeName = StringArgumentType.getString(context, "homeName").toString().toLowerCase();
        PlayerData playerData = DataManager.getPlayerData(player);
        if (playerData.delHome(homeName)) {
            player.sendMessage(Methods.formatText("command.maessentials.delhome.done", TextFormatting.WHITE, homeName));
        } else {
            player.sendMessage(Methods.formatText("command.maessentials.home.notexist", TextFormatting.DARK_RED, homeName));
        }
        return Command.SINGLE_SUCCESS;
    }
}