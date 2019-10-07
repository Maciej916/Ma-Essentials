package com.maciej916.maessentials.classes;

import net.minecraft.entity.player.ServerPlayerEntity;

public final class Tpa {
    private ServerPlayerEntity player;
    private ServerPlayerEntity requestPlayer;
    private ServerPlayerEntity targetPlayer;
    private int timer;

    public Tpa(ServerPlayerEntity newPlayer, ServerPlayerEntity newRequestPlayer, ServerPlayerEntity newTargetPlayer) {
        this.player = newPlayer;
        this.requestPlayer = newRequestPlayer;
        this.targetPlayer = newTargetPlayer;
        this.timer = 20;
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
