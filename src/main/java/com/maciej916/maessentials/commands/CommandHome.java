package com.maciej916.maessentials.commands;

import com.maciej916.maessentials.classes.Home;
import com.maciej916.maessentials.utils.Homes;
import com.maciej916.maessentials.utils.Teleport;
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

public class CommandHome {
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder<CommandSource> builder = Commands.literal("home").requires(source -> source.hasPermissionLevel(0));
        builder
                .executes(context -> home(context))
                .then(Commands.argument("HomeName", StringArgumentType.word())
                        .suggests(HomeManager.HOME_SUGGEST)
                        .executes(context -> homeArgs(context)));
        dispatcher.register(builder);
    }

    private static int home(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        handleHome(player, "home");
        return Command.SINGLE_SUCCESS;
    }

    private static int homeArgs(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        String args = StringArgumentType.getString(context, "HomeName").toString().toLowerCase();
        handleHome(player, args);
        return Command.SINGLE_SUCCESS;
    }

    private static void handleHome( ServerPlayerEntity player, String homeName) {
        Homes playerHomes = HomeManager.getPlayerHomes(player);
        Home thisHome = playerHomes.getHome(homeName);
        if (thisHome != null) {
            Teleport.teleportPlayer(player, thisHome.getHomeLocation(), true);
            player.sendMessage(new TranslationTextComponent("command.maessentials.home.teleported", homeName, true));
        } else {
            player.sendMessage(new TranslationTextComponent("command.maessentials.home.notexist", homeName, true));
        }
    }
}