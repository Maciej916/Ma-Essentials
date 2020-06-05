package com.maciej916.maessentials.common.lib.player;

import static com.maciej916.maessentials.common.util.TimeUtils.currentTimestamp;

public class PlayerRestrictions {

    private PlayerRestriction mute;
    private PlayerRestriction ban;

    public PlayerRestriction getBan() {
        return ban;
    }

    public void setBan(long time, String reason) {
        this.ban = new PlayerRestriction(time, reason);
    }

    public void unBan() {
        this.ban = null;
    }

    public PlayerRestriction getMute() {
        return mute;
    }

    public void setMute(long time, String reason) {
        this.mute = new PlayerRestriction(time, reason);
    }

    public void unMute() {
        this.mute = null;
    }

    public boolean isRestricted(PlayerRestriction restriction) {
        return restriction != null && (currentTimestamp() < restriction.getTime() || restriction.getTime() == -1);
    }

}
