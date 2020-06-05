package com.maciej916.maessentials.common.command.impl.admin;

import com.maciej916.maessentials.common.command.BaseCommand;
import com.maciej916.maessentials.common.lib.Location;
import com.maciej916.maessentials.common.util.TeleportUtils;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.block.material.Material;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.server.ServerWorld;

public class TopCommand extends BaseCommand {

    public TopCommand(String command, int permissionLevel, boolean enabled) {
        super(command, permissionLevel, enabled);
    }

    @Override
    public LiteralArgumentBuilder<CommandSource> setExecution() {
        return builder.executes((context) -> execute(context.getSource()));
    }

    private int execute(CommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.asPlayer();
        ServerWorld world = source.getWorld();

        int x = (int) player.getPosX();
        int y = world.getMaxHeight();
        int z = (int) player.getPosZ();

        Chunk chunk = world.getChunk((int) player.getPosX() >> 4, (int)player.getPosZ()>> 4);

        while (y > 0) {
            y--;

            BlockPos groundPos = new BlockPos(x, y-2, z);
            if (!chunk.getBlockState(groundPos).getMaterial().equals(Material.AIR)) {
                BlockPos legPos = new BlockPos(x, y-1, z);
                if (chunk.getBlockState(legPos).getMaterial().equals(Material.AIR)) {
                    BlockPos headPos = new BlockPos(x, y, z);
                    if (chunk.getBlockState(headPos).getMaterial().equals(Material.AIR)) {
                        Location topLocation = new Location(player.getPosX(), y-1, player.getPosZ(), player.rotationYaw, player.rotationPitch, player.dimension.getId());
                        TeleportUtils.doTeleport(player, topLocation, true, true);
                        sendMessage(player, "top.maessentials.teleported");
                        break;
                    }
                }
            }
        }

        return Command.SINGLE_SUCCESS;
    }

}
