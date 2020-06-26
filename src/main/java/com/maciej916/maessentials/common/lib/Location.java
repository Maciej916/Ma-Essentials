package com.maciej916.maessentials.common.lib;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.World;

public class Location {
    public double x,y,z;
    public float rotationYaw,rotationPitch;
    public RegistryKey<World> world;

    public Location() {}

    public Location(int posX, int posY, int posZ, RegistryKey<World> worldRegistryKey) {
        this.x = posX;
        this.y = posY;
        this.z = posZ;
        this.rotationYaw = 0;
        this.rotationPitch = 0;
        this.world = worldRegistryKey;
    }

    public Location(double posX, double posY, double posZ, float rotationYaw, float rotationPitch, RegistryKey<World> worldRegistryKey) {
        this.x = posX;
        this.y = posY;
        this.z = posZ;
        this.rotationYaw = rotationYaw;
        this.rotationPitch = rotationPitch;
        this.world = worldRegistryKey;
    }

    public Location(ServerPlayerEntity player) {
        this.x = player.getPosX();
        this.y = player.getPosY();
        this.z = player.getPosZ();
        this.rotationYaw = player.rotationYaw;
        this.rotationPitch = player.rotationPitch;
        this.world = player.getServerWorld().getWorld().func_234923_W_();
    }

    public RegistryKey<World> getWorld() {
        return this.world;
    }
}
