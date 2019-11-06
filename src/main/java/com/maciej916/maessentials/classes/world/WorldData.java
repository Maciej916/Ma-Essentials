package com.maciej916.maessentials.classes.world;

import com.maciej916.maessentials.classes.Location;
import com.maciej916.maessentials.config.ConfigValues;
import com.maciej916.maessentials.libs.Json;

public class WorldData {

    Location spawn;

    public WorldData() {

    }

    public void setSpawn(Location spawn) {
        this.spawn = spawn;
    }

    public Location getSpawn() {
        return spawn;
    }

    public void saveData() {
        Json.save(ConfigValues.worldCatalog, "data", this);
    }
}
