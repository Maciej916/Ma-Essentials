package com.maciej916.maessentials.classes;

import net.minecraft.entity.player.ServerPlayerEntity;

import java.io.Serializable;

public final class Home implements Serializable {
    private String name;
    private Location location;

    public Home(ServerPlayerEntity player, String newName) {
        super();
        this.name = newName;
        this.location = new Location(player);
    }

    public String getHomeName() {
        return name;
    }

    public Location getHomeLocation() {
        return location;
    }
}
