package com.maciej916.maessentials.common.config;

public final class ConfigHelper {

	private static net.minecraftforge.fml.config.ModConfig commonConfig;

	public static void bake(final net.minecraftforge.fml.config.ModConfig config) {
		commonConfig = config;

		// Spawn
		ModConfig.spawn_enable = ConfigHolder.COMMON.spawn_enable.get();
		ModConfig.spawn_delay = ConfigHolder.COMMON.spawn_delay.get();
		ModConfig.spawn_cooldown = ConfigHolder.COMMON.spawn_cooldown.get();
		ModConfig.spawn_force_on_death = ConfigHolder.COMMON.spawn_force_on_death.get();

		// Homes
		ModConfig.homes_enable = ConfigHolder.COMMON.homes_enable.get();
		ModConfig.homes_delay = ConfigHolder.COMMON.homes_delay.get();
		ModConfig.homes_cooldown = ConfigHolder.COMMON.homes_cooldown.get();
		ModConfig.homes_limit = ConfigHolder.COMMON.homes_limit.get();
		ModConfig.homes_limit_op = ConfigHolder.COMMON.homes_limit_op.get();

		// Warps
		ModConfig.warps_enable = ConfigHolder.COMMON.warps_enable.get();
		ModConfig.warps_delay = ConfigHolder.COMMON.warps_delay.get();
		ModConfig.warps_cooldown = ConfigHolder.COMMON.warps_cooldown.get();

		// Back
		ModConfig.back_enable = ConfigHolder.COMMON.back_enable.get();
		ModConfig.back_delay = ConfigHolder.COMMON.back_delay.get();
		ModConfig.back_cooldown = ConfigHolder.COMMON.back_cooldown.get();
		ModConfig.back_death_enable = ConfigHolder.COMMON.back_death_enable.get();
		ModConfig.back_death_custom_cooldown = ConfigHolder.COMMON.back_death_custom_cooldown.get();

		// TPA
		ModConfig.tpa_enable = ConfigHolder.COMMON.tpa_enable.get();
		ModConfig.tpa_delay = ConfigHolder.COMMON.tpa_delay.get();
		ModConfig.tpa_cooldown = ConfigHolder.COMMON.tpa_cooldown.get();
		ModConfig.tpa_timeout = ConfigHolder.COMMON.tpa_timeout.get();

		// RNDTP
		ModConfig.rndtp_enable = ConfigHolder.COMMON.rndtp_enable.get();
		ModConfig.rndtp_delay = ConfigHolder.COMMON.rndtp_delay.get();
		ModConfig.rndtp_cooldown = ConfigHolder.COMMON.rndtp_cooldown.get();
		ModConfig.rndtp_range_min = ConfigHolder.COMMON.rndtp_range_min.get();
		ModConfig.rndtp_range_max = ConfigHolder.COMMON.rndtp_range_max.get();

		// Suicide
		ModConfig.suicide_enable = ConfigHolder.COMMON.suicide_enable.get();
		ModConfig.suicide_enable_player = ConfigHolder.COMMON.suicide_enable_player.get();
		ModConfig.suicide_player_cooldown = ConfigHolder.COMMON.suicide_player_cooldown.get();

		// Time
		ModConfig.time_enable = ConfigHolder.COMMON.time_enable.get();

		// Weather
		ModConfig.weather_enable = ConfigHolder.COMMON.weather_enable.get();

		// Heal
		ModConfig.heal_enable = ConfigHolder.COMMON.heal_enable.get();

		// GM
		ModConfig.gm_enable = ConfigHolder.COMMON.gm_enable.get();

		// Fly
		ModConfig.fly_enable = ConfigHolder.COMMON.fly_enable.get();

		// God
		ModConfig.god_enable = ConfigHolder.COMMON.god_enable.get();

		// Top
		ModConfig.top_enable = ConfigHolder.COMMON.top_enable.get();

		// Mute
		ModConfig.mute_enable = ConfigHolder.COMMON.mute_enable.get();

		// Kits
		ModConfig.kits_enable = ConfigHolder.COMMON.kits_enable.get();
		ModConfig.kits_starting = ConfigHolder.COMMON.kits_starting.get();
		ModConfig.kits_starting_name = ConfigHolder.COMMON.kits_starting_name.get();

		// Speed
		ModConfig.speed_enable = ConfigHolder.COMMON.speed_enable.get();
		ModConfig.speed_max_walk = ConfigHolder.COMMON.speed_max_walk.get();
		ModConfig.speed_max_fly = ConfigHolder.COMMON.speed_max_fly.get();

		// Tpall
		ModConfig.tpall_enable = ConfigHolder.COMMON.tpall_enable.get();

		// Kickall
		ModConfig.kickall_enable = ConfigHolder.COMMON.kickall_enable.get();

		// Broadcast
		ModConfig.broadcast_enable = ConfigHolder.COMMON.broadcast_enable.get();

		// Up
		ModConfig.up_enable = ConfigHolder.COMMON.up_enable.get();

		// AFK
		ModConfig.afk_auto = ConfigHolder.COMMON.afk_auto.get();
		ModConfig.afk_auto_kick = ConfigHolder.COMMON.afk_auto_kick.get();
		ModConfig.afk_auto_time = ConfigHolder.COMMON.afk_auto_time.get();
		ModConfig.afk_command = ConfigHolder.COMMON.afk_command.get();
		ModConfig.afk_command_cooldown = ConfigHolder.COMMON.afk_command_cooldown.get();

		// Endc
		ModConfig.endc_enable = ConfigHolder.COMMON.endc_enable.get();

		// Trash
		ModConfig.trash_enable = ConfigHolder.COMMON.trash_enable.get();

		// Tempban
		ModConfig.tempban_enable = ConfigHolder.COMMON.tempban_enable.get();

		// Check
		ModConfig.check_enable = ConfigHolder.COMMON.check_enable.get();

		// Head
		ModConfig.head_enable = ConfigHolder.COMMON.head_enable.get();

		// Invsee
		ModConfig.invsee_enable = ConfigHolder.COMMON.invsee_enable.get();

		// Repair
		ModConfig.repair_enable = ConfigHolder.COMMON.repair_enable.get();

		// Hat
		ModConfig.hat_enable = ConfigHolder.COMMON.hat_enable.get();
	}

	public static void setValueAndSave(final net.minecraftforge.fml.config.ModConfig modConfig, final String path, final Object newValue) {
		modConfig.getConfigData().set(path, newValue);
		modConfig.save();
	}
}
