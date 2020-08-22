package com.maciej916.maessentials.client.interfaces;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.client.gui.GuiUtils;

import java.util.Collections;
import java.util.List;

public interface IGuiWrapper {

    default void displayTooltip(MatrixStack matrixStack, ITextComponent component, int x, int y) {
        this.displayTooltips(matrixStack, Collections.singletonList(component), x, y);
    }

    default void displayTooltips(MatrixStack matrixStack, List<ITextComponent> components, int xAxis, int yAxis) {
        displayTooltips(matrixStack, components, xAxis, yAxis, -1);
    }

    default void displayTooltips(MatrixStack matrixStack, List<ITextComponent> components, int xAxis, int yAxis, int maxWidth) {
//        GuiUtils.drawHoveringText(matrixStack, components, xAxis, yAxis, getXSize(), getYSize(), maxWidth, getFont());
    }

    int getGuiLeft();
    int getGuiTop();
    int getXSize();
    int getYSize();

    void bindTexture(ResourceLocation texture);
    void draw(MatrixStack matrixStack, int x, int y, int textureX, int textureY, int width, int height);
    void draw(MatrixStack matrixStack, int x, int y, TextureAtlasSprite icon, int width, int height, int ss);

    FontRenderer getFont();
    ResourceLocation getGuiLocation();

}
