package com.maciej916.maessentials.common.util;

import com.maciej916.maessentials.common.enums.EnumColor;
import com.maciej916.maessentials.common.enums.EnumLang;
import com.maciej916.maessentials.common.interfaces.text.IHasTranslationKey;
import net.minecraft.util.text.*;

import java.util.ArrayList;
import java.util.List;

public class TextComponentUtil {

    public static IFormattableTextComponent getFormattableComponent(ITextComponent component) {
        return component instanceof IFormattableTextComponent ? (IFormattableTextComponent) component : component.deepCopy();
    }

    public static StringTextComponent getString(String component) {
        return new StringTextComponent(component);
    }

    public static TranslationTextComponent translationComponent(String key, Object... args) {
        return new TranslationTextComponent(key, args);
    }

    public static TranslationTextComponent translate(String key, Object... components) {
        if (components.length == 0) return translationComponent(key);

        List<Object> args = new ArrayList<>();
        Style cachedStyle = Style.EMPTY;

        for (Object component : components) {
            if (component == null) {
                args.add(null);
                cachedStyle = Style.EMPTY;
                continue;
            }

            IFormattableTextComponent current = null;

            if (component instanceof IHasTranslationKey) {
                current = translate(((IHasTranslationKey) component).getTranslationKey());
            } else if (component instanceof EnumColor) {
                cachedStyle = cachedStyle.setColor(((EnumColor) component).getColor());
                continue;
            } else if (component instanceof TextFormatting) {
                cachedStyle = cachedStyle.setFormatting((TextFormatting) component);
                continue;
            }

            if (!cachedStyle.equals(Style.EMPTY)) {
                if (current == null) {
                    args.add(component);
                } else {
                    args.add(current.func_230530_a_(cachedStyle));
                }
                cachedStyle = Style.EMPTY;
            } else if (current == null) {
                args.add(component);
            } else {
                args.add(current);
            }
        }

        if (!cachedStyle.equals(Style.EMPTY)) {
            Object lastComponent = components[components.length - 1];
            if (lastComponent instanceof EnumColor) {
                args.add(((EnumColor) lastComponent).getName());
            } else {
                args.add(lastComponent);
            }
        }

        return translationComponent(key, args.toArray());
    }

    public static IFormattableTextComponent translateColored(Object... components) {

        IFormattableTextComponent result = null;
        Style cachedStyle = Style.EMPTY;

        for (Object component : components) {
            if (component == null) continue;

            IFormattableTextComponent current = null;

            if (component instanceof EnumColor) {
                cachedStyle = cachedStyle.setColor(((EnumColor) component).getColor());
            } else if (component instanceof ITextComponent) {
                current = getFormattableComponent((ITextComponent) component);
            } else if (component instanceof TextFormatting) {
                cachedStyle = cachedStyle.setFormatting((TextFormatting) component);
            } else if (component instanceof String || component instanceof Boolean || component instanceof Number) {
                current = getString(component.toString());
            } else {
                current = EnumLang.GENERIC.translate(component);
            }

            if (current == null) {
                continue;
            }

            if (!cachedStyle.equals(Style.EMPTY)) {
                current.func_230530_a_(cachedStyle);
                cachedStyle = Style.EMPTY;
            }

            if (result == null) {
                result = current;
            } else {
                result.func_230529_a_(current);
            }

        }

        return result;
    }
    
}
