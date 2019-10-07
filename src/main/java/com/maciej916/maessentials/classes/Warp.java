package com.maciej916.maessentials.classes;

import net.minecraft.entity.player.ServerPlayerEntity;

import java.io.Serializable;

public final class Warp implements Serializable {
    private String name;
    private Location location;

    public Warp(ServerPlayerEntity player, String newName) {
        super();
        this.name = newName;
        this.location = new Location(player);
    }

    public String getWarpName() {
        return name;
    }

    public Location getWarpLocation() {
        return location;
    }
}
