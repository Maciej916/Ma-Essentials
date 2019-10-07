package com.maciej916.maessentials.commands;

import com.maciej916.maessentials.utils.Homes;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.maciej916.maessentials.managers.HomeManager;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;

public class CommandDelHome {
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder<CommandSource> builder = Commands.literal("delhome").requires(source -> source.hasPermissionLevel(0));
        builder
                .executes(context -> delHome(context))
                .then(Commands.argument("HomeName", StringArgumentType.word())
                        .suggests(HomeManager.HOME_SUGGEST)
                        .executes(context -> delHomeArgs(context)));
        dispatcher.register(builder);
    }

    private static int delHome(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        player.sendMessage(new TranslationTextComponent("command.maessentials.delhome.specifyname"));
        return Command.SINGLE_SUCCESS;
    }

    private static int delHomeArgs(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        String args = StringArgumentType.getString(context, "HomeName").toString().toLowerCase();
        Homes playerHomes = HomeManager.getPlayerHomes(player);
        if (playerHomes.getHome(args) != null) {
            playerHomes.delHome(player, args);
            player.sendMessage(new TranslationTextComponent("command.maessentials.delhome.done", args, true));
        } else {
            player.sendMessage(new TranslationTextComponent("command.maessentials.home.notexist"));
        }
        return Command.SINGLE_SUCCESS;
    }
}