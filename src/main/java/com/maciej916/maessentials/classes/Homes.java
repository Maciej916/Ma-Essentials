package com.maciej916.maessentials.classes;

import java.io.Serializable;
import java.util.HashMap;

public final class Homes implements Serializable {
    private HashMap<String, Location> homes = new HashMap<>();

    public void setHome(Location location, String name) {
        homes.put(name, location);
    }

    public boolean removeHome(String name) {
        if (homes.containsKey(name)) {
            homes.remove(name);
            return true;
        }
        return false;
    }

    public HashMap<String, Location> getHomes() {
        return homes;
    }
}
