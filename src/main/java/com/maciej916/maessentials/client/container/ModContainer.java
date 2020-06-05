package com.maciej916.maessentials.client.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.EnderChestInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class ModContainer extends Container {

    protected ModContainer(ContainerType<?> type, int id) {
        super(type, id);
    }

    protected void addEnderchestSlots(int yOffset, @Nonnull EnderChestInventory inv) {
        final int playerInventoryStartX = 8;
        final int slotSizePlus2 = 18;

        for (int row = -1; row < 2; ++row) {
            for (int column = 0; column < 9; ++column) {
                this.addSlot(new Slot(inv, 9 + (row * 9) + column, playerInventoryStartX + (column * slotSizePlus2), yOffset + ((row + 1) * slotSizePlus2)));
            }
        }
    }

    protected void addInventorySlots(int yOffset, @Nonnull PlayerInventory inv) {
        final int playerInventoryStartX = 8;
        final int slotSizePlus2 = 18;

        for (int row = 0; row < 3; ++row) {
            for (int column = 0; column < 9; ++column) {
                this.addSlot(new Slot(inv, 9 + (row * 9) + column, playerInventoryStartX + (column * slotSizePlus2), yOffset + (row * slotSizePlus2)));
            }
        }

        final int playerHotbarY = yOffset + slotSizePlus2 * 3 + 4;
        for (int column = 0; column < 9; ++column) {
            this.addSlot(new Slot(inv, column, playerInventoryStartX + (column * slotSizePlus2), playerHotbarY));
        }
    }

    @Nonnull
    @Override
    public ItemStack transferStackInSlot(final PlayerEntity player, final int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            final int numRows = this.inventorySlots.size() - player.inventory.mainInventory.size();
            if (index < numRows * 9) {
                if (!this.mergeItemStack(itemstack1, numRows * 9, this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 0, numRows * 9, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }

}
