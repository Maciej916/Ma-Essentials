package com.maciej916.maessentials.network;

import com.maciej916.maessentials.MaEssentials;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class Networking {

    public static SimpleChannel INSTANCE;
    private static int ID = 0;

    private static int nextID() {
        return ID++;
    }

    public static void registerMessages() {
        INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(MaEssentials.MODID, "ma-essentials"), () -> "1.0", s -> true, s -> true);
        INSTANCE.registerMessage(nextID(), PacketSpeed.class, PacketSpeed::toBytes, PacketSpeed::new, PacketSpeed::handle);
    }

}