package com.maciej916.maessentials.client.gui;

import com.maciej916.maessentials.client.gui.base.GuiElement;
import com.maciej916.maessentials.client.interfaces.IGuiWrapper;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public abstract class ModGui<CONTAINER extends Container> extends ContainerScreen<CONTAINER> implements IGuiWrapper {

    public ModGui(CONTAINER screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
    }

    @Override
    protected void func_230450_a_(MatrixStack p_230450_1_, float p_230450_2_, int p_230450_3_, int p_230450_4_) {

    }

    @Override
    public int getStringWidth(ITextComponent component) {
//        return getStringWidth(component.getFormattedText())
        return 0;
    }

    @Override
    public int getStringWidth(String text) {
        return getFont().getStringWidth(text);
    }

    @Override
    public int drawString(ITextComponent component, int x, int y, int color) {
        return 0;
    }

    @Override
    public int drawString(String text, int x, int y, int color) {
        return 0;
    }

//    @Override
//    public int drawString(ITextComponent component, int x, int y, int color) {
//        return drawString(component.getFormattedText(), x, y, color);
//    }

//    @Override
//    public int drawString(String text, int x, int y, int color) {
//        return getFont().drawString(text, x, y, color);
//    }

    @Override
    public void drawCenteredText(ITextComponent component, int leftMargin, int y, int color) {
        drawCenteredText(component, leftMargin, 0, y, color);
    }

    @Override
    public void drawCenteredText(ITextComponent component, int leftMargin, int areaWidth, int y, int color) {
        int textWidth = getStringWidth(component);
        int centerX = leftMargin + (areaWidth / 2) - (textWidth / 2);
        drawString(component, centerX, y, color);
    }

    @Override
    public void renderScaledText(ITextComponent component, int x, int y, int color, int maxX) {

    }
//
//    @Override
//    public void renderScaledText(ITextComponent component, int x, int y, int color, int maxX) {
//        renderScaledText(component.getFormattedText(), x, y, color, maxX);
//    }

    @Override
    public void renderScaledText(String text, int x, int y, int color, int maxX) {
        int length = getStringWidth(text);
        if (length <= maxX) {
            drawString(text, x, y, color);
        } else {
            float scale = (float) maxX / length;
            float reverse = 1 / scale;
            float yAdd = 4 - (scale * 8) / 2F;
            RenderSystem.pushMatrix();
            RenderSystem.scalef(scale, scale, scale);
            drawString(text, (int) (x * reverse), (int) ((y * reverse) + yAdd), color);
            RenderSystem.popMatrix();
        }
    }

    public void bindTexture(ResourceLocation texture) {
        Minecraft.getInstance().textureManager.bindTexture(texture);
    }

//    protected void drawGuiTitleText() {
//        drawCenteredText(this.title, 0, getXSize(), 6 , 0x404040);
//        drawString(this.playerInventory.getDisplayName(), 8, getYSize() - 96 + 2, 0x404040);
//    }

//    @Override
//    protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY) {
//        drawGuiTitleText();
//
//        int xAxis = mouseX - getGuiLeft();
//        int yAxis = mouseY - getGuiTop();
//
//        for (Widget widget : this.buttons) {
//            if (widget instanceof GuiElement) {
//                ((GuiElement) widget).renderForeground(mouseX, mouseY, xAxis, yAxis);
//            }
//        }
//    }
//
//    @Override
//    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
//        bindTexture(getGuiLocation());
//        drawRect(getGuiLeft(), getGuiTop(), 0, 0, getXSize(), getYSize());
//    }

    @Override
    public void drawRect(int x, int y, int textureX, int textureY, int width, int height) {

    }

    @Override
    public void drawRectFromIcon(int x, int y, TextureAtlasSprite icon, int width, int height) {

    }

    @Override
    public FontRenderer getFont() {
        return Minecraft.getInstance().fontRenderer;
    }

//    @Override
//    public void render(int mouseX, int mouseY, float partialTicks) {
//        this.renderBackground();
//        super.render(mouseX, mouseY, partialTicks);
//        this.renderHoveredToolTip(mouseX, mouseY);
//    }
//
//    @Override
//    public void drawRect(int x, int y, int textureX, int textureY, int width, int height) {
//        blit(x, y, textureX, textureY, width, height);
//    }
//
//    @Override
//    public void drawRectFromIcon(int x, int y, TextureAtlasSprite icon, int width, int height) {
//        blit(x, y, getBlitOffset(), width, height, icon);
//    }

    protected abstract ResourceLocation getGuiLocation();

}