package com.maciej916.maessentials.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class ServerProxy implements IProxy {

    @Override
    public void init() {

    }

    @Override
    public Minecraft getClient() {
        throw new IllegalStateException("Only run this on the client!");
    }

    @Override
    public World getPlayerWorld() {
        throw new IllegalStateException("Only run this on the client!");
    }

    @Override
    public PlayerEntity getPlayerEntity() {
        throw new IllegalStateException("Only run this on the client!");
    }

}