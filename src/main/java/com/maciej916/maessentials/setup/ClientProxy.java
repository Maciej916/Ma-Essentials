package com.maciej916.maessentials.setup;

import net.minecraft.client.Minecraft;
import net.minecraft.world.GameType;
import net.minecraft.world.World;

public class ClientProxy implements IProxy {

    @Override
    public void init() {

    }

    @Override
    public World getClientWorld() {
        return Minecraft.getInstance().world;
    }

}