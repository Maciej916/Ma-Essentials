package com.maciej916.maessentials.commands;

import com.maciej916.maessentials.libs.Methods;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldInfo;

public class CommandSun {

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder<CommandSource> builder = Commands.literal("sun").requires(source -> source.hasPermissionLevel(2));
        builder.executes(context -> rain(context));
        dispatcher.register(builder);
    }

    private static int rain(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();

        for (ServerWorld serverworld : context.getSource().getServer().getWorlds()) {
            WorldInfo worldData = serverworld.getWorldInfo();
            worldData.setRaining(false);
            worldData.setThundering(false);
            worldData.setClearWeatherTime(10000);
            worldData.setRainTime(0);
        }

        player.sendMessage(Methods.formatText("sun.maessentials.success"));
        return Command.SINGLE_SUCCESS;
    }
}