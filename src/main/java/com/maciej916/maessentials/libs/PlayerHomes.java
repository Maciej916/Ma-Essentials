package com.maciej916.maessentials.libs;

import com.maciej916.maessentials.classes.Location;
import com.maciej916.maessentials.data.LoadData;
import net.minecraft.entity.player.ServerPlayerEntity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PlayerHomes implements Serializable {
    public Map<String, Location> homes = new HashMap<String, Location>();

    public boolean setHome(ServerPlayerEntity player, String homeName) {
        if (!homes.containsKey(homeName)) {
            homes.put(homeName, new Location(player));
            LoadData.savePlayerHome(player, this);
            return true;
        } else {
            return false;
        }
    }

    public boolean delHome(ServerPlayerEntity player, String homeName) {
        if (homes.containsKey(homeName)) {
            homes.remove(homeName);
            LoadData.savePlayerHome(player, this);
            return true;
        } else {
            return false;
        }
    }

    public Map<String, Location> getHomes() {
        return homes;
    }

    public Set<String> getHomeNames(){
        return homes.keySet();
    }

    public Location getHomeLocation(String homeName) {
        if (homes.containsKey(homeName)) {
            return homes.get(homeName);
        } else {
            return null;
        }
    }
}