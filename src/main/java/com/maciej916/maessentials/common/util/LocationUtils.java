package com.maciej916.maessentials.common.util;

import com.maciej916.maessentials.common.config.ModConfig;
import com.maciej916.maessentials.common.lib.Location;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;

import java.util.Random;

public final class LocationUtils {

    public static boolean checkLocation(Location first, Location second) {
        return (int)first.x == (int)second.x && (int)first.y == (int)second.y && (int)first.z == (int)second.z && first.dimension == second.dimension;
    }

    public static boolean checkDetailedLocation(Location first, Location second) {
        return first.x == second.x && first.y == second.y && first.z == second.z && first.dimension == second.dimension && first.rotationPitch == second.rotationPitch && first.rotationYaw == second.rotationYaw;
    }

    public static Location findRandomTp(World world, Location spawnLocation, ServerPlayerEntity player, int count) {
        if (count == 10) {
            return null;
        }
        count++;

        Random rand = new Random();

        int min = ModConfig.rndtp_range_min;
        int max = ModConfig.rndtp_range_max;

        int x = (int) Math.round(spawnLocation.x) + rand.nextInt(max + min) - min;
        int y = world.getMaxHeight();
        int z = (int) Math.round(spawnLocation.z) + rand.nextInt(max + min) - min;

        Chunk chunk = world.getChunk(x >> 4, z >> 4);
        Biome biome = world.getBiome(new BlockPos(x, y, z));

        if (biome.getCategory().getName().equals("ocean")) {
            return findRandomTp(world, spawnLocation, player, count);
        }

        while (y > 0) {
            y--;
            BlockPos groundPos = new BlockPos(x, y-2, z);
            if (!chunk.getBlockState(groundPos).getMaterial().equals(Material.AIR) && (!chunk.getBlockState(groundPos).getBlock().equals(Blocks.BEDROCK) && y-2 != 1)) {
                BlockPos legPos = new BlockPos(x, y-1, z);
                if (chunk.getBlockState(legPos).getMaterial().equals(Material.AIR)) {
                    BlockPos headPos = new BlockPos(x, y, z);
                    if (chunk.getBlockState(headPos).getMaterial().equals(Material.AIR)) {
                        return new Location(x + 0.5, y, z+ 0.5, 0, 0, spawnLocation.getDimensionID());
                    }
                }
            }
        }
        return findRandomTp(world, spawnLocation, player, count);
    }

}
