package com.maciej916.maessentials.client.gui.impl;

import com.maciej916.maessentials.MaEssentials;
import com.maciej916.maessentials.client.container.impl.InvseeContainer;
import com.maciej916.maessentials.client.gui.ModGui;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class InvseeGui extends ModGui<InvseeContainer> {

    public InvseeGui(InvseeContainer container, PlayerInventory inv, ITextComponent title) {
        super(container, inv, title);
        this.ySize = 202;
    }

    @Override
    protected void func_230451_b_(MatrixStack p_230451_1_, int p_230451_2_, int p_230451_3_) {
        this.field_230712_o_.func_238422_b_(p_230451_1_, this.field_230704_d_, 8, 6, 4210752);
        this.field_230712_o_.func_238422_b_(p_230451_1_, this.playerInventory.getDisplayName(), 8, 109, 4210752);
    }

    @Override
    public ResourceLocation getGuiLocation() {
        return new ResourceLocation(MaEssentials.MODID, "textures/gui/container/invsee.png");
    }

}
