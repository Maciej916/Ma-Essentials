package com.maciej916.maessentials.common.lib.player;

import java.util.UUID;

public class PlayerRestriction {
    private String username;
    private UUID playerUUID;
    private long time;
    private String reason;

    public PlayerRestriction(long time, String reason) {
        this.time = time;
        this.reason = reason;
    }

   public PlayerRestriction(String username, UUID playerUUID, PlayerRestriction restriction) {
        this.username = username;
        this.playerUUID = playerUUID;
        this.time = restriction.getTime();
        this.reason = restriction.getReason();
    }

    public String getUsername() {
        return username;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public long getTime() {
        return time;
    }

    public String getReason() {
        return reason;
    }

}
