package com.maciej916.maessentials.common.lib.kit;

import net.minecraft.command.arguments.ItemInput;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;

public class Kit {

    private long duration;
    private ArrayList<KitItem> items;

    public Kit(long duration, ArrayList<KitItem> items) {
        this.duration = duration;
        this.items = items;
    }

    public long getDuration() {
        return duration;
    }

    public ArrayList<ItemStack> getItems() throws Exception {
        ArrayList<ItemStack> items = new ArrayList<>();
        for (KitItem item : this.items) {
            ItemInput itemInput = item.getInput();
            ItemStack itemstack = itemInput.createStack(item.getQuantity(), true);
            items.add(itemstack);
        }
        return items;
    }

}
