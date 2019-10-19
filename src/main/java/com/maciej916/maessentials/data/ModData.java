package com.maciej916.maessentials.data;

import com.google.gson.annotations.SerializedName;
import com.maciej916.maessentials.classes.Location;

import java.io.Serializable;

public class ModData implements Serializable {
    @SerializedName("spawn")
    private Location spawnPoint;

    public Location getSpawnPoint() {
        return spawnPoint;
    }

    public void setSpawnPoint(Location spawnPoint) {
        this.spawnPoint = spawnPoint;
        DataManager.saveModData();
    }
}
