package com.maciej916.maessentials.data;

import com.maciej916.maessentials.classes.Location;

import java.io.Serializable;

public class ModData implements Serializable {
    public Location spawnPoint;

    public Location getSpawnPoint() {
        return spawnPoint;
    }

    public void setSpawnPoint(Location spawnPoint) {
        this.spawnPoint = spawnPoint;
        LoadData.saveModData();
    }
}
