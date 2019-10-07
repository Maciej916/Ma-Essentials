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

public class CommandSetHome {
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder<CommandSource> builder = Commands.literal("sethome").requires(source -> source.hasPermissionLevel(0));
        builder
                .executes(context -> setHome(context))
                .then(Commands.argument("HomeName", StringArgumentType.word()).executes(context -> setHomeArgs(context)));
        dispatcher.register(builder);
    }

    private static int setHome(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        handleSetHome(player, "home");
        return Command.SINGLE_SUCCESS;
    }

    private static int setHomeArgs(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        String args = StringArgumentType.getString(context, "HomeName").toString().toLowerCase();
        handleSetHome(player, args);
        return Command.SINGLE_SUCCESS;
    }

    private static void handleSetHome(ServerPlayerEntity player, String newHomeName) {
        Homes playerHomes = HomeManager.getPlayerHomes(player);
        playerHomes.setHome(player, newHomeName);
        player.sendMessage(new TranslationTextComponent("command.maessentials.sethome.set", newHomeName, true));
    }
}
