package com.maciej916.maessentials.common.command;

import com.maciej916.maessentials.common.util.TextUtils;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.awt.*;

public abstract class BaseCommand {

    protected LiteralArgumentBuilder<CommandSource> builder;
    boolean enabled;

    public BaseCommand(String name, int permissionLevel, boolean enabled) {
        this.builder = Commands.literal(name).requires(source -> source.hasPermissionLevel(permissionLevel));
        this.enabled = enabled;
    }

    public LiteralArgumentBuilder<CommandSource> getBuilder() {
        return builder;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public LiteralArgumentBuilder<CommandSource> setExecution() {
        return null;
    }

    public void sendMessage(ServerPlayerEntity player, String translationKey, Object... args) {
        TextUtils.sendMessage(player, translationKey, args);
    }

    public void sendMessage(ServerPlayerEntity player, TextComponent textComponent) {
        TextUtils.sendMessage(player, textComponent);
    }

    public void sendGlobalMessage(PlayerList players, String translationKey, Object... args) {
        TextUtils.sendGlobalMessage(players, translationKey, args);
    }

}
