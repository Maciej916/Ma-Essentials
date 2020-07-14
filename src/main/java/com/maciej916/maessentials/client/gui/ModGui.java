package com.maciej916.maessentials.client.gui;

import com.maciej916.maessentials.client.interfaces.IGuiWrapper;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
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
    public void bindTexture(ResourceLocation texture) {
        this.getMinecraft().getTextureManager().bindTexture(texture);
    }

    @Override
    public void draw(MatrixStack matrixStack, int x, int y, int textureX, int textureY, int width, int height) {
        this.blit(matrixStack, x, y, textureX, textureY, width, height);
    }

    @Override
    public void draw(MatrixStack matrixStack, int x, int y, TextureAtlasSprite icon, int width, int height, int ss) {
        this.blit(matrixStack, x, y, width, height, ss, icon);
    }

    @Override
    public FontRenderer getFont() {
        return getMinecraft().fontRenderer;
    }

    protected void updateData() {

    }

    @Override
    public void tick() {
        super.tick();
        updateData();
    }

    @Override
    public void render(MatrixStack matrixStack, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, p_230430_2_, p_230430_3_, p_230430_4_);
        this.func_230459_a_(matrixStack, p_230430_2_, p_230430_3_);
    }

    @Override
    protected void func_230450_a_(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        bindTexture(getGuiLocation());
        draw(matrixStack, getGuiLeft(), getGuiTop(), 0, 0, getXSize(), getYSize());
    }
}