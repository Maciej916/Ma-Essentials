package com.maciej916.maessentials.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class ClientProxy implements IProxy {

    @Override
    public void init() {

    }

    @Override
    public Minecraft getClient() {
        return Minecraft.getInstance();
    }

    @Override
    public World getPlayerWorld() {
        return Minecraft.getInstance().world;
    }

    @Override
    public PlayerEntity getPlayerEntity() {
        return Minecraft.getInstance().player;
    }

}