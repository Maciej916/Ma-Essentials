package com.maciej916.maessentials.classes.player;

import com.maciej916.maessentials.classes.Location;

public class PlayerTemp {

    private Location location;
    private long last_move_time;

    private boolean teleportActive = false;
    private Location teleportLocation;

    public boolean isTeleportActive() {
        return teleportActive;
    }

    public Location getTeleportLocation() {
        return teleportLocation;
    }

    public void setTeleportActive(Location location) {
        this.teleportActive = true;
        this.teleportLocation = location;
    }

    public void setTeleportNotActive() {
        this.teleportActive = false;
        this.location = null;
    }
}
