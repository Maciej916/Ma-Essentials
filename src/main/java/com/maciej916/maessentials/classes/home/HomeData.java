package com.maciej916.maessentials.classes.home;

import com.maciej916.maessentials.classes.Location;

import java.util.HashMap;
import java.util.Map;

public class HomeData {

    private Map<String, Location> homes = new HashMap<>();

    public HomeData() {
    }

    public void setHome(String name, Location location) {
        homes.put(name, location);
    }

    public void delHome(String name) {
        homes.remove(name);
    }

    public Location getHome(String name) {
        return homes.get(name);
    }

    public Map<String, Location> getHomes() {
        return homes;
    }
}
