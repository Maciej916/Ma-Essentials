package com.maciej916.maessentials.common.network;

import com.maciej916.maessentials.common.network.packets.PacketSpeed;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import static com.maciej916.maessentials.MaEssentials.MODID;

public class Networking {

    public static SimpleChannel INSTANCE;
    private static int ID = 0;

    private static int nextID() {
        return ID++;
    }

    public static void setup() {
        INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(MODID, "maessentials"), () -> "1.0", s -> true, s -> true);
        INSTANCE.registerMessage(nextID(), PacketSpeed.class, PacketSpeed::toBytes, PacketSpeed::new, PacketSpeed::handle);
    }

}