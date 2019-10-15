package com.maciej916.maessentials.libs;

import com.maciej916.maessentials.classes.Location;
import com.maciej916.maessentials.config.ConfigValues;
import com.maciej916.maessentials.data.DataManager;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;

import java.util.*;

public class Teleport {
    private static Map<ServerPlayerEntity, Teleport> activeTeleports = new HashMap<>();

    private Location loc;
    private Location dest;
    private long time;
    private boolean exact;

    private Teleport(Location loc, Location dest, long time, boolean exact){
        this.loc = loc;
        this.dest = dest;
        this.time = time;
        this.exact = exact;
    }

    public static void teleportPlayer(ServerPlayerEntity player, Location loc, boolean exact) {
        if (ConfigValues.teleportTime == 0) {
            doTeleport(player, loc, exact);
        } else {
            if (activeTeleports.containsKey(player)) {
                player.sendMessage(new TranslationTextComponent("teleport.maessentials.active"));
            } else {
                Location currentLocation = new Location(player);
                long currentTime = System.currentTimeMillis() / 1000;
                Teleport teleport = new Teleport(currentLocation, loc, currentTime + ConfigValues.teleportTime, exact);
                activeTeleports.put(player, teleport);
            }
        }
    }

    public static void checkTeleports() {
        Set<ServerPlayerEntity> delTp = new HashSet<>();
        activeTeleports.forEach((player, teleport) -> {
            Location playerLocation = new Location(player);
            if (Methods.isLocationSame(playerLocation, teleport.loc)) {
                long currentTime = System.currentTimeMillis() / 1000;
                if (teleport.time <= currentTime) {
                    doTeleport(player, teleport.dest, teleport.exact);
                    delTp.add(player);
                }
            } else {
                delTp.add(player);
                player.sendMessage(new TranslationTextComponent("teleport.maessentials.moved"));
            }
        });
        activeTeleports.keySet().removeAll(delTp);
    }

    private static void doTeleport(ServerPlayerEntity player, Location loc, boolean exact) {
        UUID playerUUID = player.getUniqueID();
        Location currentLocation = new Location(player);
        DataManager.getPlayerData(player).setLastLocation(currentLocation);

        long currentTime = System.currentTimeMillis() / 1000;
        DataManager.getPlayerData(player).setLastTeleportTime(currentTime);

        DataManager.savePlayerData(playerUUID, DataManager.getPlayerData(playerUUID));

        if (player.dimension == DimensionType.THE_END && loc.getDimension() == DimensionType.OVERWORLD) {
            player.queuedEndExit = true;
        }

        ServerWorld worldDest = player.server.getWorld(loc.getDimension());
        if (exact) {
            player.teleport(worldDest, loc.x, loc.y, loc.z, loc.rotationYaw, loc.rotationPitch);
        } else {
            player.teleport(worldDest, loc.x + 0.5, loc.y + 0.5, loc.z, player.rotationYaw, player.rotationPitch);
        }

        player.sendMessage(new TranslationTextComponent("teleport.maessentials.teleported"));
    }
}
