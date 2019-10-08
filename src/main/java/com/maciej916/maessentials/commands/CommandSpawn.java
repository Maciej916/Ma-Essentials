package com.maciej916.maessentials.commands;

import com.maciej916.maessentials.classes.Location;
import com.maciej916.maessentials.utils.Teleport;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldInfo;

public class CommandSpawn {

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher
                .register(Commands.literal("spawn")
                        .requires(source -> source.hasPermissionLevel(0))
                        .executes(context -> execute(context)));
    }

    private static int execute(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerWorld world = context.getSource().getWorld();
        ServerPlayerEntity player = context.getSource().asPlayer();

        WorldInfo worldData = world.getWorldInfo();

        Location spawnLocation = new Location(worldData.getSpawnX(), worldData.getSpawnY(), worldData.getSpawnZ(), 0);
        Teleport.teleportPlayer(player, spawnLocation, false);

        player.sendMessage(new TranslationTextComponent("command.maessentials.spawn"));
        return Command.SINGLE_SUCCESS;
    }
}