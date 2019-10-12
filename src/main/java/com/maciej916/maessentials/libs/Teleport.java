package com.maciej916.maessentials.libs;

import com.maciej916.maessentials.classes.Location;
import com.maciej916.maessentials.data.PlayerData;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;

public class Teleport {

    public static void teleportPlayer(ServerPlayerEntity player, Location loc, boolean exact) {
        PlayerData.setPlayerLastLocation(player);

        if (player.dimension == DimensionType.THE_END && loc.getDimension() == DimensionType.OVERWORLD) {
            player.queuedEndExit = true;
        }

        ServerWorld worldDest = player.server.getWorld(loc.getDimension());
        if (exact) {
            player.teleport(worldDest, loc.x, loc.y, loc.z, loc.rotationYaw, loc.rotationPitch);
        } else {
            player.teleport(worldDest, loc.x + 0.5, loc.y + 0.5, loc.z, player.rotationYaw, player.rotationPitch);
        }
    }
}
