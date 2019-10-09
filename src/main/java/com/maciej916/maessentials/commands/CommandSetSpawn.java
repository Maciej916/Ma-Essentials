package com.maciej916.maessentials.commands;

import com.maciej916.maessentials.MaEssentials;
import com.maciej916.maessentials.classes.Location;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;

public class CommandSetSpawn {

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder<CommandSource> builder = Commands.literal("setspawn").requires(source -> source.hasPermissionLevel(2));
        builder.executes(context -> setSpawn(context));
        dispatcher.register(builder);
    }

    private static int setSpawn(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        MaEssentials.modData.setSpawnPoint(new Location(player));
        player.sendMessage(new TranslationTextComponent("command.maessentials.setspawn"));
        return Command.SINGLE_SUCCESS;
    }
}