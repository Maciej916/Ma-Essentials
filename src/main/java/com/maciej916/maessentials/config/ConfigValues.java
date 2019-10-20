package com.maciej916.maessentials.config;

public class ConfigValues {
    // Spawn
    public static Boolean spawn_enable;
    public static Integer spawn_delay;
    public static Integer spawn_cooldown;

    // Homes
    public static Boolean homes_enable;
    public static Integer homes_delay;
    public static Integer homes_cooldown;
    public static Integer homes_limit;

    // Warps
    public static Boolean warps_enable;
    public static Integer warps_delay;
    public static Integer warps_cooldown;

    // Back
    public static Boolean back_enable;
    public static Integer back_delay;
    public static Integer back_cooldown;
    public static Boolean back_death_enable;

    // TPA
    public static Boolean tpa_enable;
    public static Integer tpa_delay;
    public static Integer tpa_cooldown;
    public static Integer tpa_timeout;

    // RNDTP
    public static Boolean rndtp_enable;
    public static Integer rndtp_delay;
    public static Integer rndtp_cooldown;
    public static Integer rndtp_range_min;
    public static Integer rndtp_range_max;

    // Suicide
    public static Boolean suicide_enable;
    public static Boolean suicide_enable_player;
    public static Integer suicide_player_cooldown;

    // Time
    public static Boolean time_enable;

    // Weather
    public static Boolean weather_enable;

    // Heal
    public static Boolean heal_enable;

    // GM
    public static Boolean gm_enable;

    // Fly
    public static Boolean fly_enable;

    // God
    public static Boolean god_enable;

    // Top
    public static Boolean top_enable;

    // Mute
    public static Boolean mute_enable;

    public static void init() {
        // Spawn
        spawn_enable = Config.spawn_enable.get();
        spawn_delay = Config.spawn_delay.get();
        spawn_cooldown = Config.spawn_cooldown.get();

        // Homes
        homes_enable = Config.homes_enable.get();
        homes_delay = Config.homes_delay.get();
        homes_cooldown = Config.homes_cooldown.get();
        homes_limit = Config.homes_limit.get();

        // Warps
        warps_enable = Config.warps_enable.get();
        warps_delay = Config.warps_delay.get();
        warps_cooldown = Config.warps_cooldown.get();

        // Back
        back_enable = Config.back_enable.get();
        back_delay = Config.back_delay.get();
        back_cooldown = Config.back_cooldown.get();
        back_death_enable = Config.back_death_enable.get();

        // TPA
        tpa_enable = Config.tpa_enable.get();
        tpa_delay = Config.tpa_delay.get();
        tpa_cooldown = Config.tpa_cooldown.get();
        tpa_timeout = Config.tpa_timeout.get();

        // RNDTP
        rndtp_enable = Config.rndtp_enable.get();
        rndtp_delay = Config.rndtp_delay.get();
        rndtp_cooldown = Config.rndtp_cooldown.get();
        rndtp_range_min = Config.rndtp_range_min.get();
        rndtp_range_max = Config.rndtp_range_max.get();

        // Suicide
        suicide_enable = Config.suicide_enable.get();
        suicide_enable_player = Config.suicide_enable_player.get();
        suicide_player_cooldown = Config.suicide_player_cooldown.get();

        // Time
        time_enable = Config.time_enable.get();

        // Weather
        weather_enable = Config.weather_enable.get();

        // Heal
        heal_enable = Config.heal_enable.get();

        // GM
        gm_enable = Config.gm_enable.get();

        // Fly
        fly_enable = Config.fly_enable.get();

        // God
        god_enable = Config.god_enable.get();

        // Top
        top_enable = Config.top_enable.get();

        // Mute
        mute_enable = Config.mute_enable.get();
    }
}