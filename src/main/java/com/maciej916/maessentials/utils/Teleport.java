package com.maciej916.maessentials.utils;

import com.maciej916.maessentials.classes.Location;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;

import java.util.HashMap;

public class Teleport {
    private static HashMap<ServerPlayerEntity, Location> playerLastLoc = new HashMap<ServerPlayerEntity, Location>();

    public static void teleportPlayer(ServerPlayerEntity player, Location loc, boolean exact) {
        setPlayerLastLoc(player);

        if (player.dimension.getId() != loc.dim) {
            player.changeDimension(DimensionType.getById(loc.dim));
        }
        ServerWorld world = player.getServerWorld();
        if (exact) {
            player.teleport(world, loc.posX, loc.posY, loc.posZ, loc.rotationYaw, loc.rotationPitch);
        } else {
            player.teleport(world, loc.x + 0.5, loc.y + 0.5, loc.z, player.rotationYaw, player.rotationPitch);
        }
    }

    public static boolean backPlayer(ServerPlayerEntity player) {
        if (playerLastLoc.containsKey(player)){
            teleportPlayer(player, playerLastLoc.get(player), true);
            return true;
        }
        return false;
    }

    public static void setPlayerLastLoc(ServerPlayerEntity player) {
        if (playerLastLoc.containsKey(player)){
            playerLastLoc.remove(player);
        }
        playerLastLoc.put(player, new Location(player));
    }
}
