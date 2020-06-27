package com.maciej916.maessentials.common.lib;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class Location {
    public double x,y,z;
    public float rotationYaw,rotationPitch;
    public ResourceLocation world = Registry.field_239699_ae_.func_240901_a_();
    public ResourceLocation dimension;

    public Location() {}

    public Location(int posX, int posY, int posZ, ResourceLocation dimension) {
        this.x = posX;
        this.y = posY;
        this.z = posZ;
        this.rotationYaw = 0;
        this.rotationPitch = 0;
        this.dimension = dimension;
    }

    public Location(double posX, double posY, double posZ, float rotationYaw, float rotationPitch, ResourceLocation dimension) {
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
        this.dimension = player.getServerWorld().getWorld().func_234923_W_().func_240901_a_();
    }

    public RegistryKey<World> getWorld() {
        return RegistryKey.func_240903_a_(RegistryKey.func_240904_a_(this.world), this.dimension);
    }
}
