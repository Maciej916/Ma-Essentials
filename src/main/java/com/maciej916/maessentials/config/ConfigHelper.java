package com.maciej916.maessentials.config;

import net.minecraftforge.fml.config.ModConfig;

public final class ConfigHelper {

	private static ModConfig commonConfig;

	public static void bake(final ModConfig config) {
		commonConfig = config;

		// Spawn
		ConfigValues.spawn_enable = ConfigHolder.COMMON.spawn_enable.get();
		ConfigValues.spawn_delay = ConfigHolder.COMMON.spawn_delay.get();
		ConfigValues.spawn_cooldown = ConfigHolder.COMMON.spawn_cooldown.get();
		ConfigValues.spawn_force_on_death = ConfigHolder.COMMON.spawn_force_on_death.get();

		// Homes
		ConfigValues.homes_enable = ConfigHolder.COMMON.homes_enable.get();
		ConfigValues.homes_delay = ConfigHolder.COMMON.homes_delay.get();
		ConfigValues.homes_cooldown = ConfigHolder.COMMON.homes_cooldown.get();
		ConfigValues.homes_limit = ConfigHolder.COMMON.homes_limit.get();

		// Warps
		ConfigValues.warps_enable = ConfigHolder.COMMON.warps_enable.get();
		ConfigValues.warps_delay = ConfigHolder.COMMON.warps_delay.get();
		ConfigValues.warps_cooldown = ConfigHolder.COMMON.warps_cooldown.get();

		// Back
		ConfigValues.back_enable = ConfigHolder.COMMON.back_enable.get();
		ConfigValues.back_delay = ConfigHolder.COMMON.back_delay.get();
		ConfigValues.back_cooldown = ConfigHolder.COMMON.back_cooldown.get();
		ConfigValues.back_death_enable = ConfigHolder.COMMON.back_death_enable.get();

		// TPA
		ConfigValues.tpa_enable = ConfigHolder.COMMON.tpa_enable.get();
		ConfigValues.tpa_delay = ConfigHolder.COMMON.tpa_delay.get();
		ConfigValues.tpa_cooldown = ConfigHolder.COMMON.tpa_cooldown.get();
		ConfigValues.tpa_timeout = ConfigHolder.COMMON.tpa_timeout.get();

		// RNDTP
		ConfigValues.rndtp_enable = ConfigHolder.COMMON.rndtp_enable.get();
		ConfigValues.rndtp_delay = ConfigHolder.COMMON.rndtp_delay.get();
		ConfigValues.rndtp_cooldown = ConfigHolder.COMMON.rndtp_cooldown.get();
		ConfigValues.rndtp_range_min = ConfigHolder.COMMON.rndtp_range_min.get();
		ConfigValues.rndtp_range_max = ConfigHolder.COMMON.rndtp_range_max.get();

		// Suicide
		ConfigValues.suicide_enable = ConfigHolder.COMMON.suicide_enable.get();
		ConfigValues.suicide_enable_player = ConfigHolder.COMMON.suicide_enable_player.get();
		ConfigValues.suicide_player_cooldown = ConfigHolder.COMMON.suicide_player_cooldown.get();

		// Time
		ConfigValues.time_enable = ConfigHolder.COMMON.time_enable.get();

		// Weather
		ConfigValues.weather_enable = ConfigHolder.COMMON.weather_enable.get();

		// Heal
		ConfigValues.heal_enable = ConfigHolder.COMMON.heal_enable.get();

		// GM
		ConfigValues.gm_enable = ConfigHolder.COMMON.gm_enable.get();

		// Fly
		ConfigValues.fly_enable = ConfigHolder.COMMON.fly_enable.get();

		// God
		ConfigValues.god_enable = ConfigHolder.COMMON.god_enable.get();

		// Top
		ConfigValues.top_enable = ConfigHolder.COMMON.top_enable.get();

		// Mute
		ConfigValues.mute_enable = ConfigHolder.COMMON.mute_enable.get();

		// Kits
		ConfigValues.kits_enable = ConfigHolder.COMMON.kits_enable.get();
		ConfigValues.kits_starting = ConfigHolder.COMMON.kits_starting.get();
		ConfigValues.kits_starting_name = ConfigHolder.COMMON.kits_starting_name.get();

		// Speed
		ConfigValues.speed_enable = ConfigHolder.COMMON.speed_enable.get();
		ConfigValues.speed_max_walk = ConfigHolder.COMMON.speed_max_walk.get();
		ConfigValues.speed_max_fly = ConfigHolder.COMMON.speed_max_fly.get();

		// Tpall
		ConfigValues.tpall_enable = ConfigHolder.COMMON.tpall_enable.get();

		// Kickall
		ConfigValues.kickall_enable = ConfigHolder.COMMON.kickall_enable.get();

		// Broadcast
		ConfigValues.broadcast_enable = ConfigHolder.COMMON.broadcast_enable.get();

		// Up
		ConfigValues.up_enable = ConfigHolder.COMMON.up_enable.get();

		// AFK
		ConfigValues.afk_auto = ConfigHolder.COMMON.afk_auto.get();
		ConfigValues.afk_auto_kick = ConfigHolder.COMMON.afk_auto_kick.get();
		ConfigValues.afk_auto_time = ConfigHolder.COMMON.afk_auto_time.get();
		ConfigValues.afk_command = ConfigHolder.COMMON.afk_command.get();
		ConfigValues.afk_command_cooldown = ConfigHolder.COMMON.afk_command_cooldown.get();

		// Endc
		ConfigValues.endc_enable = ConfigHolder.COMMON.endc_enable.get();

		// Trash
		ConfigValues.trash_enable = ConfigHolder.COMMON.trash_enable.get();


		// Tempban
		ConfigValues.tempban_enable = ConfigHolder.COMMON.tempban_enable.get();
	}

	public static void setValueAndSave(final ModConfig modConfig, final String path, final Object newValue) {
		modConfig.getConfigData().set(path, newValue);
		modConfig.save();
	}
}
