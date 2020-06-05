package com.maciej916.maessentials.client.gui.base;

import com.maciej916.maessentials.client.interfaces.IGuiWrapper;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.widget.Widget;

public abstract class GuiElement extends Widget {

    protected final IGuiWrapper guiObj;
    protected boolean playClickSound;

    public GuiElement(IGuiWrapper gui, int x, int y, int width, int height, String msg) {
        super(x, y, width, height, msg);
        this.guiObj = gui;
    }

    public void renderForeground(int mouseX, int mouseY, int xAxis, int yAxis) {
        if (isMouseOver(mouseX, mouseY)) {
            renderToolTip(xAxis, yAxis);
        }
    }

    @Override
    public void playDownSound(SoundHandler soundHandler) {
        if (playClickSound) {
            super.playDownSound(soundHandler);
        }
    }

}
