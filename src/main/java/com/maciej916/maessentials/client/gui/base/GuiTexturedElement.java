package com.maciej916.maessentials.client.gui.base;


import com.maciej916.maessentials.client.interfaces.IGuiWrapper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;

public abstract class GuiTexturedElement extends GuiElement {

    protected final ResourceLocation resource;
    protected final int relativeX;
    protected final int relativeY;

    public GuiTexturedElement(IGuiWrapper gui, ResourceLocation guiResource, int x, int y, int width, int height) {
        super(gui, x, y, width, height, new StringTextComponent(""));
        this.resource = guiResource;
        this.relativeX = x;
        this.relativeY = y;
    }

    protected ResourceLocation getResource() {
        return resource;
    }
}
