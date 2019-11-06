package com.maciej916.maessentials.classes.warp;

import com.maciej916.maessentials.classes.Location;
import com.maciej916.maessentials.config.ConfigValues;
import com.maciej916.maessentials.libs.Json;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class WarpData {

    private Map<String, Location> warps = new HashMap<>();

    public WarpData() {
    }

    public WarpData(Map<String, Location> warps) {
        this.warps = warps;
    }

    public boolean setWarp(String name, Location location) {
        if (!warps.containsKey(name)) {
            warps.put(name, location);
            saveWarp(name, location);
            return true;
        }
        return false;
    }

    public boolean delWarp(String name) {
        if (warps.containsKey(name)) {
            warps.remove(name);
            removeWarp(name);
            return true;
        }
        return false;
    }

    public Location getWarp(String name) {
        return warps.get(name);
    }

    public Map<String, Location> getWarps() {
        return warps;
    }

    private void saveWarp(String name, Location location) {
        Json.save(ConfigValues.worldCatalog + "warps/", name, location);
    }

    private void removeWarp(String name) {
        File file = new File(ConfigValues.worldCatalog + "warps/" + name + ".json");
        file.delete();
    }
}
