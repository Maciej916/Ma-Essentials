package com.maciej916.maessentials.commands;

import com.maciej916.maessentials.classes.Location;
import com.maciej916.maessentials.libs.Methods;
import com.maciej916.maessentials.libs.Teleport;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
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
        LiteralArgumentBuilder<CommandSource> builder = Commands.literal("up").requires(source -> source.hasPermissionLevel(2));
        builder
                .executes(context -> up(context))
                .then(Commands.argument("number", IntegerArgumentType.integer())
                        .executes(context -> upArgs(context)));
        dispatcher.register(builder);
    }

    private static int up(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        player.sendMessage(Methods.formatText("maessentials.provide.location"));
        return Command.SINGLE_SUCCESS;
    }

    private static int upArgs(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerWorld world = context.getSource().getWorld();
        ServerPlayerEntity player = context.getSource().asPlayer();
        int number = IntegerArgumentType.getInteger(context, "number");

        int x = (int) player.func_226277_ct_() - 1;
        int y = (int) player.func_226278_cu_() + number - 1;
        int z = (int) player.func_226281_cx_() - 1;

        Chunk chunk = world.getChunk((int) player.func_226277_ct_() >> 4, (int)player.func_226281_cx_() >> 4);
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
            player.sendMessage(Methods.formatText("up.maessentials.success", number));
        } else {
            player.sendMessage(Methods.formatText("maessentials.invalid.location"));
        }

        return Command.SINGLE_SUCCESS;
    }
}
