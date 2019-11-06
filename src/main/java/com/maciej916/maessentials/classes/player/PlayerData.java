package com.maciej916.maessentials.classes.player;

import com.maciej916.maessentials.classes.Location;

public class PlayerData {

    private Location last_location = null;

    public void setLastLocation(Location location) {
        this.last_location = location;
    }

    public Location getLastLocation() {
        return last_location;
    }
}
