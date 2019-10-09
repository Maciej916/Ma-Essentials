package com.maciej916.maessentials.classes;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.dimension.DimensionType;

import java.io.Serializable;

public final class Location implements Serializable {
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
        this.z = posX;
        this.y = posY;
        this.z = posZ;
        this.rotationYaw = rotationYaw;
        this.rotationPitch = rotationPitch;
        this.dimension = dimension;
    }

    public Location(ServerPlayerEntity player) {
        this.x = player.posX;
        this.y = player.posY;
        this.z = player.posZ;
        this.rotationYaw = player.rotationYaw;
        this.rotationPitch = player.rotationPitch;
        this.dimension = player.dimension.getId();
    }

    public DimensionType getDimension() {
        return DimensionType.getById(dimension);
    }
}
