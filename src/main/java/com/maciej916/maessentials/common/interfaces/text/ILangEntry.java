package com.maciej916.maessentials.common.interfaces.text;

import com.maciej916.maessentials.common.enums.EnumColor;
import com.maciej916.maessentials.common.util.TextComponentUtil;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public interface ILangEntry extends IHasTranslationKey {

    default TranslationTextComponent translate(Object... args) {
        return TextComponentUtil.translate(getTranslationKey(), args);
    }

    default IFormattableTextComponent translateColored(EnumColor color, Object... args) {
        return TextComponentUtil.translateColored(color, translate(args));
    }

}
