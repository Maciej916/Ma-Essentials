package com.maciej916.maessentials.libs;

import com.maciej916.maessentials.classes.Location;
import com.maciej916.maessentials.data.PlayerData;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.server.ServerWorld;

public class Teleport {

    public static void teleportPlayer(ServerPlayerEntity player, Location loc, boolean exact) {
        PlayerData.setPlayerLastLocation(player);

        if (player.dimension.getId() != loc.dimension) {
            player.changeDimension(loc.getDimension());
        }

        ServerWorld world = player.getServerWorld();
        if (exact) {
            player.teleport(world, loc.x, loc.y, loc.z, loc.rotationYaw, loc.rotationPitch);
        } else {
            player.teleport(world, loc.x + 0.5, loc.y + 0.5, loc.z, player.rotationYaw, player.rotationPitch);
        }
    }


}
