package com.maciej916.maessentials.common.lib.player;


import com.maciej916.maessentials.common.lib.Location;

public class PlayerData {

    private Location last_location = null;
    private long last_death;
    private int death_count;

    private boolean fly_enabled = false;
    private boolean god_enabled = false;

    public void setLastLocation(Location location) {
        this.last_location = location;
    }

    public Location getLastLocation() {
        return last_location;
    }

    public void setLastDeath(long last_death) {
        this.last_death = last_death;
    }

    public long getLastDeath() {
        return last_death;
    }

    public void addDeathCount() {
        this.death_count++;
    }

    public int getDeathCount() {
        return death_count;
    }

    public void setFlyEnabled(boolean fly_enabled) {
        this.fly_enabled = fly_enabled;
    }

    public boolean getFlyEnabled() {
        return fly_enabled;
    }

    public void setGodEnabled(boolean god_enabled) {
        this.god_enabled = god_enabled;
    }

    public boolean getGodEnabled() {
        return god_enabled;
    }
}
