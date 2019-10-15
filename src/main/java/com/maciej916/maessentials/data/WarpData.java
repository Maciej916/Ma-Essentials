package com.maciej916.maessentials.data;

import com.maciej916.maessentials.classes.Location;
import net.minecraft.entity.player.ServerPlayerEntity;

import java.util.HashMap;
import java.util.Map;

public class WarpData {
    private Map<String, Location> warps = new HashMap<>();

    public Map<String, Location> getWarps() {
        return warps;
    }

    public void addWarp(String warpName, Location location) {
        warps.put(warpName, location);
    }

    public void cleanWarps() { warps.clear(); }

    public boolean setWarp(ServerPlayerEntity player, String name) {
        if (!warps.containsKey(name)) {
            Location location = new Location(player);
            warps.put(name, location);
            DataManager.saveWarp(name, location);
            return true;
        } else {
            return false;
        }
    }

    public boolean delWarp(String name) {
        if (warps.containsKey(name)) {
            warps.remove(name);
            DataManager.removeWarp(name);
            return true;
        } else {
            return false;
        }
    }
}
