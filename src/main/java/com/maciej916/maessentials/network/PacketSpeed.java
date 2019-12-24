package com.maciej916.maessentials.network;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

import static com.maciej916.maessentials.MaEssentials.proxy;

public class PacketSpeed {

    private final boolean walk;
    private final float speed;

    public PacketSpeed(boolean walk, float speed) {
        this.walk = walk;
        this.speed = speed;
    }

    public PacketSpeed(PacketBuffer buf) {
        walk = buf.readBoolean();
        speed = buf.readFloat();
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeBoolean(walk);
        buf.writeFloat(speed);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            PlayerEntity player = proxy.getPlayerEntity();
            if (walk) {
                player.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(speed);
            } else {
                player.abilities.setFlySpeed(speed);
                player.sendPlayerAbilities();
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
