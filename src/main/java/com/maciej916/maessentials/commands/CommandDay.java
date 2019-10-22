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
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldInfo;

public class CommandDay {

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder<CommandSource> builder = Commands.literal("day").requires(source -> source.hasPermissionLevel(2));
        builder.executes(context -> day(context));
        dispatcher.register(builder);
    }

    private static int day(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerWorld world = context.getSource().getWorld();
        ServerPlayerEntity player = context.getSource().asPlayer();
        WorldInfo worldData = world.getWorldInfo();
        world.setDayTime(2000);
        player.sendMessage(Methods.formatText("day.maessentials.success", TextFormatting.WHITE, worldData.getWorldName()));
        return Command.SINGLE_SUCCESS;
    }
}