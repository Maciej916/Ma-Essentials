package com.maciej916.maessentials.classes;

import com.mojang.brigadier.StringReader;
import net.minecraft.command.arguments.ItemInput;
import net.minecraft.command.arguments.ItemParser;

import java.io.Serializable;

public class KitItem implements Serializable {
    private String name;
    private int quantity;
    private String nbt;

    public KitItem(String name, int count, String nbt) {
        this.name = name;
        this.quantity = count;
        this.nbt = nbt;
    }

    public String getRawName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getRawNbt() {
        return nbt;
    }

    public ItemInput getItemInput() throws Exception {
        StringReader stringReader = new StringReader(name + nbt);
        ItemParser itemParser = (new ItemParser(stringReader, false)).parse();
        return new ItemInput(itemParser.getItem(), itemParser.getNbt());
    }
}
