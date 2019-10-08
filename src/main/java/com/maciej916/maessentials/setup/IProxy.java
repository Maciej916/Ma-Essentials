package com.maciej916.maessentials.setup;

import net.minecraft.world.GameType;
import net.minecraft.world.World;

public interface IProxy {
    void init();


    World getClientWorld();

}
