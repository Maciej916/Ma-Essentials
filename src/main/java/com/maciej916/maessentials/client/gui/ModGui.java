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
        this.field_230706_i_.getTextureManager().bindTexture(texture);
    }

    @Override
    public void draw(MatrixStack matrixStack, int x, int y, int textureX, int textureY, int width, int height) {
        this.func_238474_b_(matrixStack, x, y, textureX, textureY, width, height);
    }

    @Override
    public void draw(MatrixStack matrixStack, int x, int y, TextureAtlasSprite icon, int width, int height, int ss) {
        this.func_238470_a_(matrixStack, x, y, width, height, ss, icon);
    }

    @Override
    public FontRenderer getFont() {
        return field_230706_i_.fontRenderer;
    }

    protected void updateData() {

    }

    @Override
    public void func_231023_e_() {
        super.func_231023_e_();
        updateData();
    }

    @Override
    public void func_230430_a_(MatrixStack matrixStack, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
        this.func_230446_a_(matrixStack);
        super.func_230430_a_(matrixStack, p_230430_2_, p_230430_3_, p_230430_4_);
        this.func_230459_a_(matrixStack, p_230430_2_, p_230430_3_);
    }

    @Override
    protected void func_230450_a_(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        bindTexture(getGuiLocation());
        draw(matrixStack, getGuiLeft(), getGuiTop(), 0, 0, getXSize(), getYSize());
    }
}