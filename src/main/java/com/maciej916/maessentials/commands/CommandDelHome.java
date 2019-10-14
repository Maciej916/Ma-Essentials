package com.maciej916.maessentials.commands;

import com.maciej916.maessentials.classes.EssentialPlayer;
import com.maciej916.maessentials.classes.Location;
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
import net.minecraft.util.text.TranslationTextComponent;

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
        player.sendMessage(new TranslationTextComponent("command.maessentials.delhome.specifyname"));
        return Command.SINGLE_SUCCESS;
    }

    private static int delHomeArgs(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        String homeName = StringArgumentType.getString(context, "homeName").toString().toLowerCase();
        EssentialPlayer playerData = DataManager.getPlayerData(player);
        if (playerData.delHome(homeName)) {
            player.sendMessage(new TranslationTextComponent("command.maessentials.delhome.done", homeName, true));
        } else {
            player.sendMessage(new TranslationTextComponent("command.maessentials.home.notexist", homeName, true));
        }
        return Command.SINGLE_SUCCESS;
    }
}