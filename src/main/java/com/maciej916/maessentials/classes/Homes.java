package com.maciej916.maessentials.classes;

import java.io.Serializable;
import java.util.HashMap;

public final class Homes implements Serializable {
    private HashMap<String, Location> homes = new HashMap<>();

    protected boolean addHome(Location location, String name) {
        if (!homes.containsKey(name)) {
            homes.put(name, location);
            return true;
        }
        return false;
    }

    protected boolean removeHome(String name) {
        if (homes.containsKey(name)) {
            homes.remove(name);
            return true;
        }
        return false;
    }

    protected HashMap<String, Location> getHomes() {
        return homes;
    }
}
