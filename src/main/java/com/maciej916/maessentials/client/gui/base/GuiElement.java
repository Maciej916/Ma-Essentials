package com.maciej916.maessentials.client.gui.base;

import com.maciej916.maessentials.client.interfaces.IGuiWrapper;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.text.ITextComponent;

public abstract class GuiElement extends Widget {

    protected final IGuiWrapper guiObj;
    protected boolean playClickSound;

    public GuiElement(IGuiWrapper gui, int x, int y, int width, int height, ITextComponent msg) {
        super(x, y, width, height, msg);
        this.guiObj = gui;
    }

    public void renderForeground(int mouseX, int mouseY, int xAxis, int yAxis) {
//        if (isMouseOver(mouseX, mouseY)) {
        if (func_231047_b_(mouseX, mouseY)) {
//            renderToolTip(xAxis, yAxis);
        }
    }

    @Override
//    public void playDownSound(SoundHandler soundHandler) {
    public void func_230988_a_(SoundHandler soundHandler) {
        if (playClickSound) {
            super.func_230988_a_(soundHandler);
        }
    }

}
