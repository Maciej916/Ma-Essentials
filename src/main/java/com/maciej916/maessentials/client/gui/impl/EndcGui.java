package com.maciej916.maessentials.client.gui.impl;

import com.maciej916.maessentials.MaEssentials;
import com.maciej916.maessentials.client.container.impl.EndcContainer;
import com.maciej916.maessentials.client.container.impl.InvseeContainer;
import com.maciej916.maessentials.client.gui.ModGui;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class EndcGui extends ModGui<EndcContainer> {

    public EndcGui(EndcContainer container, PlayerInventory inv, ITextComponent title) {
        super(container, inv, title);
        this.ySize = 181;
    }

//    @Override
//    protected void drawGuiTitleText() {
//        drawString(this.title, 8, 6, 0x404040);
//        drawString(this.playerInventory.getDisplayName(), 8, 87, 0x404040);
//    }

    @Override
    protected ResourceLocation getGuiLocation() {
        return new ResourceLocation(MaEssentials.MODID, "textures/gui/container/endc.png");
    }

}
