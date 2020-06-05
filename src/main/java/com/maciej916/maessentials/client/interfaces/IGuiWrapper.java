package com.maciej916.maessentials.client.interfaces;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.client.gui.GuiUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public interface IGuiWrapper {

    default void displayTooltip(ITextComponent component, int x, int y, int maxWidth) {
        this.displayTooltips(Collections.singletonList(component), x, y, maxWidth);
    }

    default void displayTooltip(ITextComponent component, int x, int y) {
        this.displayTooltips(Collections.singletonList(component), x, y);
    }

    default void displayTooltips(List<ITextComponent> components, int xAxis, int yAxis) {
        displayTooltips(components, xAxis, yAxis, -1);
    }

    default void displayTooltips(List<ITextComponent> components, int xAxis, int yAxis, int maxWidth) {
        List<String> toolTips = components.stream().map(ITextComponent::getFormattedText).collect(Collectors.toList());
        GuiUtils.drawHoveringText(toolTips, xAxis, yAxis, getWidth(), getHeight(), maxWidth, getFont());
    }

    default int getLeft() {
        if (this instanceof ContainerScreen) {
            return ((ContainerScreen<?>) this).getGuiLeft();
        }
        return 0;
    }

    default int getTop() {
        if (this instanceof ContainerScreen) {
            return ((ContainerScreen<?>) this).getGuiTop();
        }
        return 0;
    }

    default int getWidth() {
        if (this instanceof ContainerScreen) {
            return ((ContainerScreen<?>) this).width;
        }
        return 0;
    }

    default int getHeight() {
        if (this instanceof ContainerScreen) {
            return ((ContainerScreen<?>) this).height;
        }
        return 0;
    }











    void drawRect(int x, int y, int textureX, int textureY, int width, int height);

    void drawRectFromIcon(int x, int y, TextureAtlasSprite icon, int width, int height);



    FontRenderer getFont();




    int getStringWidth(ITextComponent text);

    int getStringWidth(String text);

    int drawString(ITextComponent component, int x, int y, int color);

    int drawString(String text, int x, int y, int color);

    void drawCenteredText(ITextComponent component, int leftMargin, int y, int color);

    void drawCenteredText(ITextComponent component, int leftMargin, int areaWidth, int y, int color);

    void renderScaledText(ITextComponent component, int x, int y, int color, int maxX);

    void renderScaledText(String text, int x, int y, int color, int maxX);

    void bindTexture(ResourceLocation texture);

}
