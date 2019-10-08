package com.maciej916.maessentials.setup;

import net.minecraft.world.World;
import net.minecraft.world.GameType;

public class ServerProxy implements IProxy {

    @Override
    public void init() {

    }

    @Override
    public World getClientWorld() {
        throw new IllegalStateException("Only run this on the client!");
    }

}