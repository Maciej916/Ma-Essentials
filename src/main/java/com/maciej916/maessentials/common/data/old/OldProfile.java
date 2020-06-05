package com.maciej916.maessentials.common.data.old;

import com.maciej916.maessentials.common.lib.Location;

import java.util.HashMap;

public class OldProfile {

    public Location last_location;
    public long spawn_time;
    public long home_time;
    public long warp_time;
    public long back_time;
    public long teleport_request_time;
    public long rndtp_time;
    public long suicide_time;
    public long mute_time;
    public String mute_reason;
    public HashMap<String, Long> kit_usage_time = new HashMap<>();

}

