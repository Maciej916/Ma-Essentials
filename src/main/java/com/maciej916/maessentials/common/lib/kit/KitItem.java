package com.maciej916.maessentials.common.lib.kit;

import com.google.gson.annotations.JsonAdapter;
import net.minecraft.command.arguments.ItemInput;

@JsonAdapter(KitItemSD.class)
public class KitItem {
    private String name;
    private String nbt;
    private int quantity;
    private transient ItemInput input;

    public KitItem(String name, int count, String nbt, ItemInput input) {
        this.name = name;
        this.quantity = count;
        this.nbt = nbt;
        this.input = input;
    }

    public String getName() {
        return name;
    }

    public String getNbt() {
        return nbt;
    }

    public int getQuantity() {
        return quantity;
    }

    public ItemInput getInput() {
        return input;
    }
}
