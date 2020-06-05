package com.maciej916.maessentials.common.command.impl.admin;

import com.maciej916.maessentials.common.command.BaseCommand;
import com.maciej916.maessentials.common.lib.Location;
import com.maciej916.maessentials.common.util.TeleportUtils;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.server.ServerWorld;

public class UpCommand extends BaseCommand {

    public UpCommand(String command, int permissionLevel, boolean enabled) {
        super(command, permissionLevel, enabled);
    }

    @Override
    public LiteralArgumentBuilder<CommandSource> setExecution() {
        return builder.then(Commands.argument("number", IntegerArgumentType.integer()).executes((context) -> execute(context.getSource(), IntegerArgumentType.getInteger(context, "number"))));
    }

    private int execute(CommandSource source, int number) throws CommandSyntaxException {
        ServerPlayerEntity player = source.asPlayer();
        ServerWorld world = source.getWorld();

        int x = MathHelper.floor(player.getPosX());
        int y = MathHelper.floor(player.getPosY()) + number - 1;
        int z = MathHelper.floor(player.getPosZ());

        Chunk chunk = world.getChunk(x >> 4, z >> 4);
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
                TeleportUtils.doTeleport(player, location, true, true);
                tp = true;
            }
        }

        if (tp) {
            if (number > 0) {
                sendMessage(player, "up.maessentials.success.up", number);
            } else {
                number = number * -1;
                sendMessage(player, "up.maessentials.success.down", number);
            }
        } else {
            sendMessage(player, "maessentials.invalid.location");
        }

        return Command.SINGLE_SUCCESS;
    }

}
