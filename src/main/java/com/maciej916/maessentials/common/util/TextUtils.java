package com.maciej916.maessentials.common.util;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public final class TextUtils {

    private static void sendMessage(ServerPlayerEntity player, IFormattableTextComponent textComponent, boolean actionBar) {
        player.sendStatusMessage(textComponent, actionBar);
    }

    private static void sendGlobalMessage(PlayerList players, IFormattableTextComponent textComponent, boolean actionBar) {
        for(int i = 0; i < players.getPlayers().size(); ++i) {
            ServerPlayerEntity player = players.getPlayers().get(i);
            sendMessage(player, textComponent, actionBar);
        }
    }

    // Chat msg

    public static void sendChatMessage(ServerPlayerEntity player, IFormattableTextComponent textComponent) {
        sendMessage(player, textComponent, false);
    }

    public static void sendChatMessage(ServerPlayerEntity player, String translationKey, Object... args) {
        sendMessage(player, new TranslationTextComponent(translationKey, args), false);
    }

    // Action Bar

    public static void sendActionMessage(ServerPlayerEntity player, IFormattableTextComponent textComponent) {
        sendMessage(player, textComponent, true);
    }

    public static void sendActionMessage(ServerPlayerEntity player, String translationKey, Object... args) {
        sendMessage(player, new TranslationTextComponent(translationKey, args), true);
    }

    // Global msg

    public static void sendGlobalActionMessage(PlayerList players, String translationKey, Object... args) {
        sendGlobalMessage(players, new TranslationTextComponent(translationKey, args), true);
    }

    public static void sendGlobalActionMessage(PlayerList players, IFormattableTextComponent textComponent) {
        sendGlobalMessage(players, textComponent, true);
    }

    // Global Action Bar

    public static void sendGlobalChatMessage(PlayerList players, String translationKey, Object... args) {
        sendGlobalMessage(players, new TranslationTextComponent(translationKey, args), false);
    }

    public static void sendGlobalChatMessage(PlayerList players, TextComponent textComponent) {
        sendGlobalMessage(players, textComponent, false);
    }
}
