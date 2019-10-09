package com.maciej916.maessentials.libs;

import com.maciej916.maessentials.classes.Location;
import com.maciej916.maessentials.classes.TeleportRequest;
import com.maciej916.maessentials.config.ConfigValues;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;

import java.util.ArrayList;
import java.util.HashMap;

public class Teleport {
    private static HashMap<ServerPlayerEntity, Location> playerLastLoc = new HashMap<>();
    private static ArrayList<TeleportRequest> teleportRequests = new ArrayList<TeleportRequest>();

    public static void teleportPlayer(ServerPlayerEntity player, Location loc, boolean exact) {
        setPlayerLastLoc(player);

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

    public static Location getPlayerLastLoc(ServerPlayerEntity player) {
        return playerLastLoc.get(player);
    }

    public static void setPlayerLastLoc(ServerPlayerEntity player) {
        if (playerLastLoc.containsKey(player)){
            playerLastLoc.remove(player);
        }
        playerLastLoc.put(player, new Location(player));
    }

    public static ArrayList<TeleportRequest> getTeleportRequests(ServerPlayerEntity player) {
        ArrayList<TeleportRequest> userTpaRequests = new ArrayList<>();
        for (TeleportRequest thisTpa : teleportRequests) {
            if (thisTpa.getRequestPlayer() == player || thisTpa.getTargetPlayer() == player) {
                userTpaRequests.add(thisTpa);
            }
        }
        return userTpaRequests;
    }
    
    public static TeleportRequest findTeleportRequest(ServerPlayerEntity player, ServerPlayerEntity requestPlayer, ServerPlayerEntity targetPlayer) {
        for (TeleportRequest thisTpa : teleportRequests) {
            if (thisTpa.getPlayer() == player && thisTpa.getRequestPlayer() == requestPlayer && thisTpa.getTargetPlayer() == targetPlayer) {
                return thisTpa;
            }
        }
        return null;
    }

    public static boolean requestTeleportRequest(ServerPlayerEntity player, ServerPlayerEntity requestPlayer, ServerPlayerEntity targetPlayer) {
        if (findTeleportRequest(player, requestPlayer, targetPlayer) == null) {
            TeleportRequest newTpa = new TeleportRequest(player, requestPlayer, targetPlayer, ConfigValues.tpaRequestTimeout);
            teleportRequests.add(newTpa);
            return true;
        } else {
            return false;
        }
    }

    public static void checkTeleportRequests() {
        ArrayList<TeleportRequest> found = new ArrayList<>();
        for (TeleportRequest thisTpa : teleportRequests) {
            int tpaTime = thisTpa.countDown();
            if (tpaTime == 0) {
                ServerPlayerEntity request = thisTpa.getRequestPlayer();
                ServerPlayerEntity target = thisTpa.getTargetPlayer();
                if (request != null && target != null) {
                    request.sendMessage(new TranslationTextComponent("command.maessentials.tpa.expired.target", target.getDisplayName(), true));
                    target.sendMessage(new TranslationTextComponent("command.maessentials.tpa.expired.request", request.getDisplayName(), true));
                }
                found.add(thisTpa);
            }
        }
        teleportRequests.removeAll(found);
    }

    public static void removeTeleportRequest(TeleportRequest thisTpa) {
        teleportRequests.remove(thisTpa);
    }

}
