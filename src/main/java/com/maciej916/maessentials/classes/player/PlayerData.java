package com.maciej916.maessentials.classes.player;

import com.maciej916.maessentials.classes.Location;

public class PlayerData {

    private Location last_location = null;
    private long last_death;
    private int death_count;

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
}
