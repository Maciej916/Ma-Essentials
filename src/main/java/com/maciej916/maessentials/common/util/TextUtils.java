package com.maciej916.maessentials.common.util;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public final class TextUtils {

    public static void sendMessage(ServerPlayerEntity player, String translationKey, Object... args) {
//        player.sendMessage(new TranslationTextComponent(translationKey, args));
        player.sendMessage(new TranslationTextComponent(translationKey, args), player.getUniqueID());
    }

    public static void sendMessage(ServerPlayerEntity player, TextComponent textComponent) {
//        player.sendMessage(textComponent);
        player.sendMessage(textComponent, player.getUniqueID());
    }

    public static void sendGlobalMessage(PlayerList players, String translationKey, Object... args) {
        for(int i = 0; i < players.getPlayers().size(); ++i) {
            ServerPlayerEntity serverplayerentity = players.getPlayers().get(i);
            serverplayerentity.sendMessage(new TranslationTextComponent(translationKey, args), serverplayerentity.getUniqueID());
        }

//        players.sendMessage(new TranslationTextComponent(translationKey, args));
    }

}
