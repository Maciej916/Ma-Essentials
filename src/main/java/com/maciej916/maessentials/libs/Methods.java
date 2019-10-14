package com.maciej916.maessentials.libs;

import com.maciej916.maessentials.data.DataManager;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.command.CommandSource;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

public class Methods {

    public static TextComponent formatText(String translationKey, TextFormatting color, Object... args) {
        TextComponent msg = new TranslationTextComponent(translationKey);
        msg.getStyle().setColor(color);
        return msg;
    }

    public static final SuggestionProvider<CommandSource> HOME_SUGGEST = (context, builder) -> {
        ServerPlayerEntity player = context.getSource().asPlayer();
        return ISuggestionProvider.suggest(DataManager.getPlayerData(player).getHomes().keySet().stream().toArray(String[]::new), builder);
    };


}
