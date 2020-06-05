package com.maciej916.maessentials.client.container.impl;

import com.maciej916.maessentials.client.container.ModContainer;
import com.maciej916.maessentials.common.register.ModContainers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;

public class InvseeContainer extends ModContainer {

    private static PlayerEntity readPlayerFromBuffer(World world, PacketBuffer buf) {
        String targetPlayer = buf.readUniqueId().toString();
        return world.getPlayers().stream().filter(player -> player.getUniqueID().toString().equals(targetPlayer)).findFirst().orElse(null);
    }

    public InvseeContainer(int id, PlayerInventory inv, PacketBuffer buf) {
        this(id, inv, readPlayerFromBuffer(inv.player.world, buf));
    }

    public InvseeContainer(int id, PlayerInventory inv, PlayerEntity playerEntity) {
        super(ModContainers.INVSEE, id);
        if (playerEntity != null) {
            this.addInventorySlots(18, playerEntity.inventory);
            this.addInventorySlots(121, inv);
        }
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }
}
