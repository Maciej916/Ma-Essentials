package com.maciej916.maessentials.commands;

import com.maciej916.maessentials.classes.Location;
import com.maciej916.maessentials.classes.player.EssentialPlayer;
import com.maciej916.maessentials.config.ConfigValues;
import com.maciej916.maessentials.data.DataManager;
import com.maciej916.maessentials.libs.Methods;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

import static com.maciej916.maessentials.libs.Methods.simpleTeleport;

public class CommandRndtp {

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("rndtp").requires((source) -> source.hasPermissionLevel(0))
                .executes((context) -> rndtp(context.getSource()))
        );
    }

    private static int rndtp(CommandSource source) throws CommandSyntaxException {
        ServerWorld world = source.getWorld();
        ServerPlayerEntity player = source.asPlayer();
        EssentialPlayer eslPlayer = DataManager.getPlayer(player);

        long cooldown = eslPlayer.getUsage().getTeleportCooldown("rndtp", ConfigValues.rndtp_cooldown);
        if (cooldown != 0) {
            player.sendMessage(Methods.formatText("maessentials.cooldown", cooldown));
            return Command.SINGLE_SUCCESS;
        }

        Location spawnLocation = DataManager.getWorld().getSpawn();
        if (spawnLocation.getDimensionID() != player.dimension.getId()) {
            player.sendMessage(Methods.formatText("rndtp.maessentials.dimension"));
            return Command.SINGLE_SUCCESS;
        }

        Location location = findRandomTp(world, spawnLocation, player, 0);
        if (location == null) {
            player.sendMessage(Methods.formatText("rndtp.maessentials.not_found"));
            return Command.SINGLE_SUCCESS;
        }

        eslPlayer.getUsage().setCommandUsage("rndtp");
        eslPlayer.saveData();
        if (simpleTeleport(player, location, "rndtp", ConfigValues.rndtp_delay)) {
            if (ConfigValues.rndtp_delay == 0) {
                player.sendMessage(Methods.formatText("rndtp.maessentials.teleport"));
            } else {
                player.sendMessage(Methods.formatText("rndtp.maessentials.teleport.wait", ConfigValues.rndtp_delay));
            }
        }

        return Command.SINGLE_SUCCESS;
    }

    private static Location findRandomTp(World world, Location spawnLocation, ServerPlayerEntity player, int count) {
        if (count == 10) {
            return null;
        }
        count++;

        Random rand = new Random();

        int min = ConfigValues.rndtp_range_min;
        int max = ConfigValues.rndtp_range_max;

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