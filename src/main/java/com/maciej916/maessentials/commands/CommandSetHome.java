package com.maciej916.maessentials.commands;

import com.maciej916.maessentials.classes.EssentialPlayer;
import com.maciej916.maessentials.config.ConfigValues;
import com.maciej916.maessentials.data.DataManager;
import com.maciej916.maessentials.data.PlayerData;
import com.maciej916.maessentials.libs.Methods;
import com.maciej916.maessentials.libs.PlayerHomes;
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

public class CommandSetHome {
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder<CommandSource> builder = Commands.literal("sethome").requires(source -> source.hasPermissionLevel(0));
        builder
            .executes(context -> setHome(context))
            .then(Commands.argument("homeName", StringArgumentType.string()).executes(context -> setHomeArgs(context)));
        dispatcher.register(builder);
    }

    private static int setHome(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        handleSetHome(player, "home");
        return Command.SINGLE_SUCCESS;
    }

    private static int setHomeArgs(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        String args = StringArgumentType.getString(context, "homeName").toString().toLowerCase();
        handleSetHome(player, args);
        return Command.SINGLE_SUCCESS;
    }

    private static void handleSetHome(ServerPlayerEntity player, String homeName) {
        EssentialPlayer playerData = DataManager.getPlayerData(player);
        if (playerData.getHomes().size() < ConfigValues.maxHomes) {
           if (playerData.setHome(player, homeName)) {
               player.sendMessage(new TranslationTextComponent("command.maessentials.sethome.set", homeName, true));
           } else {
               player.sendMessage(Methods.formatText("command.maessentials.sethome.exist", TextFormatting.DARK_RED, homeName));
           }
       } else {
            player.sendMessage(Methods.formatText("command.maessentials.sethome.max", TextFormatting.DARK_RED));
       }
    }
}
