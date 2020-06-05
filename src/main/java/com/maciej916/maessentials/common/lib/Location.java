package com.maciej916.maessentials.common.lib;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.dimension.DimensionType;

public class Location {
    public double x,y,z;
    public float rotationYaw,rotationPitch;
    public int dimension;

    public Location() {}

    public Location(int posX, int posY, int posZ, int dimension) {
        this.x = posX;
        this.y = posY;
        this.z = posZ;
        this.rotationYaw = 0;
        this.rotationPitch = 0;
        this.dimension = dimension;
    }

    public Location(double posX, double posY, double posZ, float rotationYaw, float rotationPitch, int dimension) {
        this.x = posX;
        this.y = posY;
        this.z = posZ;
        this.rotationYaw = rotationYaw;
        this.rotationPitch = rotationPitch;
        this.dimension = dimension;
    }

    public Location(ServerPlayerEntity player) {
        this.x = player.getPosX();
        this.y = player.getPosY();
        this.z = player.getPosZ();
        this.rotationYaw = player.rotationYaw;
        this.rotationPitch = player.rotationPitch;
        this.dimension = player.dimension.getId();
    }

    public DimensionType getDimension() {
        return DimensionType.getById(dimension);
    }

    public int getDimensionID() {
        return dimension;
    }
}
