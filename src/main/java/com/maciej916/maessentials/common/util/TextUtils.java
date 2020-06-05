package com.maciej916.maessentials.common.util;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public final class TextUtils {

    public static void sendMessage(ServerPlayerEntity player, String translationKey, Object... args) {
        player.sendMessage(new TranslationTextComponent(translationKey, args));
    }

    public static void sendMessage(ServerPlayerEntity player, TextComponent textComponent) {
        player.sendMessage(textComponent);
    }

    public static void sendGlobalMessage(PlayerList players, String translationKey, Object... args) {
        players.sendMessage(new TranslationTextComponent(translationKey, args));
    }

}
