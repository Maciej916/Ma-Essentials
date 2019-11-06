package com.maciej916.maessentials.classes.player;

public class PlayerRestriction {
    private long time;
    private String reason;

    public PlayerRestriction(long time, String reason) {
        this.time = time;
        this.reason = reason;
    }

    public long getTime() {
        return time;
    }

    public String getReason() {
        return reason;
    }

}
