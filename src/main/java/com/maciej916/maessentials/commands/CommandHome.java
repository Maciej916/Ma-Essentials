package com.maciej916.maessentials.commands;

import com.maciej916.maessentials.classes.EssentialPlayer;
import com.maciej916.maessentials.classes.Location;
import com.maciej916.maessentials.data.DataManager;
import com.maciej916.maessentials.data.PlayerData;
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
import net.minecraft.util.text.TranslationTextComponent;

public class CommandHome {
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder<CommandSource> builder = Commands.literal("home").requires(source -> source.hasPermissionLevel(0));
        builder
                .executes(context -> home(context))
                .then(Commands.argument("homeName", StringArgumentType.string())
                        .suggests(Methods.HOME_SUGGEST)
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
        String args = StringArgumentType.getString(context, "homeName").toString().toLowerCase();
        handleHome(player, args);
        return Command.SINGLE_SUCCESS;
    }

    private static void handleHome(ServerPlayerEntity player, String homeName) {
        Location homeLocation = DataManager.getPlayerData(player).getHomes().get(homeName);
        if (homeLocation != null) {
            Teleport.teleportPlayer(player, homeLocation, true);
            player.sendMessage(new TranslationTextComponent("command.maessentials.home.teleported", homeName, true));
        } else {
            player.sendMessage(Methods.formatText("command.maessentials.home.notexist", TextFormatting.DARK_RED, homeName));
        }
    }
}