package com.maciej916.maessentials.data;

import com.maciej916.maessentials.classes.Location;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.command.CommandSource;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.entity.player.ServerPlayerEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class WarpData {
    private static Map<String, Location> warps = new HashMap<>();

    public static final SuggestionProvider<CommandSource> WARP_SUGGEST = (context, builder) -> ISuggestionProvider.suggest(warps.keySet().stream().toArray(String[]::new), builder);

    public static void addWarp(String warpName, Location location) {
        warps.put(warpName, location);
    }

    public static void cleanData() { warps.clear(); }

    public static Location getWarpLocation(String warpName) {
        return warps.get(warpName);
    }

    public static boolean setWarp(ServerPlayerEntity player, String warpName) {
        if (!warps.containsKey(warpName)) {
            Location warpLocation = new Location(player);
            warps.put(warpName, warpLocation);
            LoadData.saveWarp(warpName, warpLocation);
            return true;
        } else {
            return false;
        }
    }

    public static boolean delWarp(String warrpname) {
        if (warps.containsKey(warrpname)) {
            warps.remove(warrpname);
            LoadData.removeWarp(warrpname);
            return true;
        } else {
            return false;
        }
    }

    public static Set<String> getWarpNames(){
        return warps.keySet();
    }
}
