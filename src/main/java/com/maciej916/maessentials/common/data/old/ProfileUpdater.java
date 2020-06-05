package com.maciej916.maessentials.common.data.old;

import com.maciej916.maessentials.common.lib.player.EssentialPlayer;

import java.util.Map;
import java.util.UUID;

public class ProfileUpdater {

    public static EssentialPlayer updateProfie(UUID playerUUID, OldProfile old) {
        EssentialPlayer eslPlayer = new EssentialPlayer(playerUUID);

        if (old.last_location != null) {
            eslPlayer.getData().setLastLocation(old.last_location);
        }

        if (old.spawn_time != 0) {
            eslPlayer.getUsage().setCommandUsage("spawn", old.spawn_time);
        }

        if (old.home_time != 0) {
            eslPlayer.getUsage().setCommandUsage("home", old.home_time);
        }

        if (old.warp_time != 0) {
            eslPlayer.getUsage().setCommandUsage("warp", old.warp_time);
        }

        if (old.back_time != 0) {
            eslPlayer.getUsage().setCommandUsage("back", old.back_time);
        }

        if (old.teleport_request_time != 0) {
            eslPlayer.getUsage().setCommandUsage("tpa", old.teleport_request_time);
        }

        if (old.rndtp_time != 0) {
            eslPlayer.getUsage().setCommandUsage("rndtp", old.rndtp_time);
        }

        if (old.suicide_time != 0) {
            eslPlayer.getUsage().setCommandUsage("suicide",old.suicide_time);
        }

        if (old.mute_time != 0) {
            eslPlayer.getRestrictions().setMute(old.mute_time, old.mute_reason);
        }

        for (Map.Entry me : old.kit_usage_time.entrySet()) {
            eslPlayer.getUsage().setKitUssage((String) me.getKey(), (long) me.getValue());
        }

        return eslPlayer;
    }

}
