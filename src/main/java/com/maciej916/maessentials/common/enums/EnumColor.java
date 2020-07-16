package com.maciej916.maessentials.common.enums;

import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.Tags;

public enum EnumColor {
    BLACK("\u00a70", new int[]{64, 64, 64}, DyeColor.BLACK),
    DARK_BLUE("\u00a71", new int[]{54, 107, 208}, DyeColor.BLUE),
    DARK_GREEN("\u00a72", new int[]{89, 193, 95}, DyeColor.GREEN),
    DARK_AQUA("\u00a73", new int[]{0, 243, 208}, DyeColor.CYAN),
    DARK_RED("\u00a74", new int[]{201, 7, 31}, Tags.Items.DYES_RED),
    PURPLE("\u00a75", new int[]{164, 96, 217}, DyeColor.PURPLE),
    ORANGE("\u00a76", new int[]{255, 161, 96}, DyeColor.ORANGE),
    GRAY("\u00a77", new int[]{207, 207, 207}, DyeColor.LIGHT_GRAY),
    DARK_GRAY("\u00a78", new int[]{122, 122, 122}, DyeColor.GRAY),
    INDIGO("\u00a79", new int[]{85, 158, 255}, DyeColor.LIGHT_BLUE),
    BRIGHT_GREEN("\u00a7a", new int[]{117, 255, 137}, DyeColor.LIME),
    AQUA("\u00a7b", new int[]{48, 255, 249}, Tags.Items.DYES_LIGHT_BLUE),
    RED("\u00a7c", new int[]{255, 56, 60}, DyeColor.RED),
    PINK("\u00a7d", new int[]{255, 164, 249}, DyeColor.MAGENTA),
    YELLOW("\u00a7e", new int[]{255, 221, 79}, DyeColor.YELLOW),
    WHITE("\u00a7f", new int[]{255, 255, 255}, DyeColor.WHITE);

    public final String code;

    private int[] rgbCode;
    private Color color;
    private final ITag<Item> dyeTag;

    EnumColor(String code, int[] rgbCode, DyeColor dyeColor) {
        this(code, rgbCode, dyeColor.getTag());
    }

    EnumColor(String code, int[] rgbCode, ITag<Item> dyeTag) {
        this.code = code;
        this.rgbCode = rgbCode;
        setColorFromAtlas(rgbCode);
        this.dyeTag = dyeTag;
    }

    public String getCode() {
        return code;
    }

    public int[] getRgbCode() {
        return rgbCode;
    }

    public float getColor(int index) {
        return getRgbCode()[index] / 255F;
    }

    public Color getColor() {
        return color;
    }

    public ITag<Item> getDyeTag() {
        return dyeTag;
    }

    public IFormattableTextComponent getName() {
        return new TranslationTextComponent("test");
    }

    public void setColorFromAtlas(int[] color) {
        rgbCode = color;
        this.color = Color.func_240743_a_(rgbCode[0] << 16 | rgbCode[1] << 8 | rgbCode[2]);
    }
}
