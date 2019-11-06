package com.maciej916.maessentials.classes.home;

import com.maciej916.maessentials.classes.Location;

import java.util.HashMap;
import java.util.Map;

public class HomeData {

    private Map<String, Location> homes = new HashMap<>();

    public HomeData() {
    }

    public boolean setHome(String name, Location location) {
        if (!homes.containsKey(name)) {
            homes.put(name, location);
            return true;
        }
        return false;
    }

    public boolean delHome(String name) {
        if (homes.containsKey(name)) {
            homes.remove(name);
            return true;
        }
        return false;
    }

    public Location getHome(String name) {
        return homes.get(name);
    }

    public Map<String, Location> getHomes() {
        return homes;
    }
}
