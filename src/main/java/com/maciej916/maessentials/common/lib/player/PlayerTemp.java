package com.maciej916.maessentials.common.lib.player;

import com.maciej916.maessentials.common.lib.Location;

import static com.maciej916.maessentials.common.util.TimeUtils.currentTimestamp;

public class PlayerTemp {

    private Location location;
    private long lastMoveTime;
    private boolean afk;

    private boolean teleportActive = false;
    private Location teleportLocation;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
        this.lastMoveTime = currentTimestamp();
    }

    public long getLastMoveTime() {
        return lastMoveTime;
    }

    public boolean isAfk() {
        return afk;
    }

    public void setAfk(boolean afk) {
        this.afk = afk;
    }

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
