package com.maciej916.maessentials.classes;

import net.minecraft.entity.player.ServerPlayerEntity;

public final class TeleportRequest {
    private ServerPlayerEntity player;
    private ServerPlayerEntity requestPlayer;
    private ServerPlayerEntity targetPlayer;
    private int timer;

    public TeleportRequest(ServerPlayerEntity player, ServerPlayerEntity requestPlayer, ServerPlayerEntity targetPlayer, int timer) {
        this.player = player;
        this.requestPlayer = requestPlayer;
        this.targetPlayer = targetPlayer;
        this.timer = timer;
    }

    public ServerPlayerEntity getPlayer() {
        return player;
    }

    public ServerPlayerEntity getRequestPlayer() {
        return requestPlayer;
    }

    public ServerPlayerEntity getTargetPlayer() {
        return targetPlayer;
    }

    public int getTimer() {
        return timer;
    }

    public int countDown() {
        timer--;
        return  timer;
    }
}
