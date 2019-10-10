package com.maciej916.maessentials.classes;

import net.minecraft.entity.player.ServerPlayerEntity;

public final class TeleportRequest {
    private ServerPlayerEntity creatorPlayer;
    private ServerPlayerEntity tpPlayer;
    private ServerPlayerEntity tpTargetPlayer;

    private int teleportTime;
    private int timeoutTime;

    private Location acceptLocation;

    public TeleportRequest(ServerPlayerEntity creatorPlayer, ServerPlayerEntity tpPlayer, ServerPlayerEntity tpTargetPlayer, int teleportTime, int timeoutTime) {
        this.creatorPlayer = creatorPlayer;
        this.tpPlayer = tpPlayer;
        this.tpTargetPlayer = tpTargetPlayer;
        this.teleportTime = teleportTime;
        this.teleportTime = teleportTime;
        this.timeoutTime = timeoutTime;
    }

    public ServerPlayerEntity getCreatorPlayer() {
        return creatorPlayer;
    }

    public ServerPlayerEntity getTpPlayer() {
        return tpPlayer;
    }

    public ServerPlayerEntity getTpTargetPlayer() {
        return tpTargetPlayer;
    }

    public void setAcceptLocation(Location location) {
        this.acceptLocation  = location;
    }

    public Location getAcceptLocation() {
        return acceptLocation;
    }

    public int countDownTeleport() {
        teleportTime--;
        return  teleportTime;
    }

    public int countdwnTimeout() {
        timeoutTime--;
        return  timeoutTime;
    }
}
