package com.maciej916.maessentials.commands;

import com.maciej916.maessentials.classes.Location;
import com.maciej916.maessentials.libs.Methods;
import com.maciej916.maessentials.libs.Teleport;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.server.ServerWorld;

public class CommandUp {

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("up").requires((source) -> source.hasPermissionLevel(2))
                .executes((context) -> up(context.getSource()))
                .then(Commands.argument("number", IntegerArgumentType.integer()).executes((context) -> up(context.getSource(), IntegerArgumentType.getInteger(context, "number"))))
        );
    }

    private static int up(CommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.asPlayer();
        player.sendMessage(Methods.formatText("maessentials.provide.location"));
        return Command.SINGLE_SUCCESS;
    }

    private static int up(CommandSource source, int number) throws CommandSyntaxException {
        ServerPlayerEntity player = source.asPlayer();
        ServerWorld world = source.getWorld();

        int x = (int) player.posX;
        int y = (int) player.posY + number - 1;
        int z = (int) player.posZ;

        Chunk chunk = world.getChunk((int) player.posX >> 4, (int)player.posZ >> 4);
        boolean tp = false;

        BlockPos headPos = new BlockPos(x, y+2, z);
        if (chunk.getBlockState(headPos).getMaterial().equals(Material.AIR)) {
            BlockPos legPos = new BlockPos(x, y+1, z);
            if (chunk.getBlockState(legPos).getMaterial().equals(Material.AIR)) {
                BlockPos groundPos = new BlockPos(x, y, z);
                if (chunk.getBlockState(groundPos).getMaterial().equals(Material.AIR)) {
                    world.setBlockState(groundPos, Blocks.GLASS.getDefaultState(), 2);
                }
                Location location = new Location(x + 0.5, y+1, z + 0.5, player.rotationYaw, player.rotationPitch, player.dimension.getId());
                Teleport.doTeleport(player, location, true, true);
                tp = true;
            }
        }

        if (tp) {
            if (number > 0) {
                player.sendMessage(Methods.formatText("up.maessentials.success.up", number));
            } else {
                number = number * -1;
                player.sendMessage(Methods.formatText("up.maessentials.success.down", number));
            }
        } else {
            player.sendMessage(Methods.formatText("maessentials.invalid.location"));
        }

        return Command.SINGLE_SUCCESS;
    }

}
