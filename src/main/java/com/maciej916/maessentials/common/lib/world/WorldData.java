package com.maciej916.maessentials.common.lib.world;

import com.maciej916.maessentials.common.lib.Location;
import com.maciej916.maessentials.common.config.ModConfig;
import com.maciej916.maessentials.common.util.JsonUtils;

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
        JsonUtils.save(ModConfig.worldCatalog, "data", this);
    }
}
