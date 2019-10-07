package com.maciej916.maessentials.classes;

import net.minecraft.entity.player.ServerPlayerEntity;

import java.io.Serializable;

public final class Location implements Serializable {
    public int x,y,z;
    public double posX,posY,posZ;
    public float rotationYaw,rotationPitch;
    public int dim;

    private static int round(double pos) {
        return (int)Math.floor(pos);
    }

    public Location(int newX, int newY, int newZ, int newDim) {
        this.x = newX;
        this.y = newY;
        this.z = newZ;
        this.dim = newDim;
    }

    public Location(double newPosX, double newPosY, double mewPosZ, float newRotationYaw, float newRotationPitch, int newDim) {
        this.x = round(newPosX);
        this.y = round(newPosY);
        this.z = round(mewPosZ);
        this.posX = newPosX;
        this.posY = newPosY;
        this.posZ = mewPosZ;
        this.rotationYaw = newRotationYaw;
        this.rotationPitch = newRotationPitch;
        this.dim = newDim;
    }

    public Location(ServerPlayerEntity player) {
        this.x = round(player.posX);
        this.y = round(player.posY);
        this.z = round(player.posZ);
        this.posX = player.posX;
        this.posY = player.posY;
        this.posZ = player.posZ;
        this.rotationYaw = player.rotationYaw;
        this.rotationPitch = player.rotationPitch;
        this.dim = player.dimension.getId();
    }
}
