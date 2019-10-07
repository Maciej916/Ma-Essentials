package com.maciej916.maessentials.utils;

import com.maciej916.maessentials.classes.Location;
import com.maciej916.maessentials.classes.Tpa;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;

import java.util.ArrayList;
import java.util.HashMap;

public class Teleport {
    private static HashMap<ServerPlayerEntity, Location> playerLastLoc = new HashMap<ServerPlayerEntity, Location>();
    private static ArrayList<Tpa> tpaArrayList = new ArrayList<Tpa>();

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

    public static ArrayList<Tpa> getTpaRequests(ServerPlayerEntity player) {
        ArrayList<Tpa> userTpaRequests = new ArrayList<>();
        for (Tpa thisTpa : tpaArrayList) {
            if (thisTpa.getRequestPlayer() == player || thisTpa.getTargetPlayer() == player) {
                userTpaRequests.add(thisTpa);
            }
        }
        return userTpaRequests;
    }
    
    public static Tpa findTpa(ServerPlayerEntity player, ServerPlayerEntity requestPlayer, ServerPlayerEntity targetPlayer) {
        for (Tpa thisTpa : tpaArrayList) {
            if (thisTpa.getPlayer() == player && thisTpa.getRequestPlayer() == requestPlayer && thisTpa.getTargetPlayer() == targetPlayer) {
                return thisTpa;
            }
        }
        return null;
    }

    public static boolean requestTpa(ServerPlayerEntity player, ServerPlayerEntity requestPlayer, ServerPlayerEntity targetPlayer) {
        if (findTpa(player, requestPlayer, targetPlayer) == null) {
            Tpa newTpa = new Tpa(player, requestPlayer, targetPlayer);
            tpaArrayList.add(newTpa);
            return true;
        } else {
            return false;
        }
    }

    public static void checkTpa() {
        ArrayList<Tpa> found = new ArrayList<>();
        for (Tpa thisTpa : tpaArrayList) {
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
        tpaArrayList.removeAll(found);
    }

    public static void removeTpa(Tpa thisTpa) {
        tpaArrayList.remove(thisTpa);
    }

}
