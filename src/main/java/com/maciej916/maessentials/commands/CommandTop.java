package com.maciej916.maessentials.commands;

import com.maciej916.maessentials.classes.Location;
import com.maciej916.maessentials.data.DataManager;
import com.maciej916.maessentials.data.PlayerData;
import com.maciej916.maessentials.libs.Methods;
import com.maciej916.maessentials.libs.Teleport;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.block.material.Material;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.server.ServerWorld;

public class CommandTop {

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder<CommandSource> builder = Commands.literal("top").requires(source -> source.hasPermissionLevel(2));
        builder.executes(context -> top(context));
        dispatcher.register(builder);
    }

    private static int top(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerWorld world = context.getSource().getWorld();
        ServerPlayerEntity player = context.getSource().asPlayer();

        int x = (int) player.posX;
        int y = world.getMaxHeight();
        int z = (int) player.posZ;

        Chunk chunk = world.getChunk((int) player.posX >> 4, (int)player.posZ >> 4);

        while (y > 0) {
            y--;

            BlockPos groundPos = new BlockPos(x, y-2, z);
            if (!chunk.getBlockState(groundPos).getMaterial().equals(Material.AIR)) {
                BlockPos legPos = new BlockPos(x, y-1, z);
                if (chunk.getBlockState(legPos).getMaterial().equals(Material.AIR)) {
                    BlockPos headPos = new BlockPos(x, y, z);
                    if (chunk.getBlockState(headPos).getMaterial().equals(Material.AIR)) {
                        Location topLocation = new Location((int) player.posX + 0.5, y, (int) player.posZ + 0.5, player.rotationYaw, player.rotationPitch, player.dimension.getId());
                        Teleport.doTeleport(player, topLocation, true);
                        player.sendMessage(Methods.formatText("top.maessentials.teleported", TextFormatting.WHITE));
                        break;
                    }
                }
            }
        }

        return Command.SINGLE_SUCCESS;
    }
}