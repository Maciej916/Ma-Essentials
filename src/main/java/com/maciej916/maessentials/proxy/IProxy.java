package com.maciej916.maessentials.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public interface IProxy {

    void init();
    Minecraft getClient();
    World getPlayerWorld();
    PlayerEntity getPlayerEntity();

}