package com.maciej916.maessentials.common.config;

import net.minecraftforge.common.ForgeConfigSpec;

final class CommonConfig {
	private final static int MAX = Integer.MAX_VALUE;

	// Spawn
	final ForgeConfigSpec.BooleanValue spawn_enable;
	final ForgeConfigSpec.IntValue spawn_delay;
	final ForgeConfigSpec.IntValue spawn_cooldown;
	final ForgeConfigSpec.BooleanValue spawn_force_on_death;

	// Homes
	final ForgeConfigSpec.BooleanValue homes_enable;
	final ForgeConfigSpec.IntValue homes_delay;
	final ForgeConfigSpec.IntValue homes_cooldown;
	final ForgeConfigSpec.IntValue homes_limit;
	final ForgeConfigSpec.IntValue homes_limit_op;

	// Warps
	final ForgeConfigSpec.BooleanValue warps_enable;
	final ForgeConfigSpec.IntValue warps_delay;
	final ForgeConfigSpec.IntValue warps_cooldown;

	// Back
	final ForgeConfigSpec.BooleanValue back_enable;
	final ForgeConfigSpec.IntValue back_delay;
	final ForgeConfigSpec.IntValue back_cooldown;
	final ForgeConfigSpec.BooleanValue back_death_enable;
	final ForgeConfigSpec.IntValue back_death_custom_cooldown;

	// TPA
	final ForgeConfigSpec.BooleanValue tpa_enable;
	final ForgeConfigSpec.IntValue tpa_delay;
	final ForgeConfigSpec.IntValue tpa_cooldown;
	final ForgeConfigSpec.IntValue tpa_timeout;

	// RNDTP
	final ForgeConfigSpec.BooleanValue rndtp_enable;
	final ForgeConfigSpec.IntValue rndtp_delay;
	final ForgeConfigSpec.IntValue rndtp_cooldown;
	final ForgeConfigSpec.IntValue rndtp_range_min;
	final ForgeConfigSpec.IntValue rndtp_range_max;

	// Suicide
	final ForgeConfigSpec.BooleanValue suicide_enable;
	final ForgeConfigSpec.BooleanValue suicide_enable_player;
	final ForgeConfigSpec.IntValue suicide_player_cooldown;

	// Time
	final ForgeConfigSpec.BooleanValue time_enable;

	// Weather
	final ForgeConfigSpec.BooleanValue weather_enable;

	// Heal
	final ForgeConfigSpec.BooleanValue heal_enable;

	// GM
	final ForgeConfigSpec.BooleanValue gm_enable;

	// Fly
	final ForgeConfigSpec.BooleanValue fly_enable;

	// God
	final ForgeConfigSpec.BooleanValue god_enable;

	// Top
	final ForgeConfigSpec.BooleanValue top_enable;

	// Mute
	final ForgeConfigSpec.BooleanValue mute_enable;

	// Kits
	final ForgeConfigSpec.BooleanValue kits_enable;
	final ForgeConfigSpec.BooleanValue kits_starting;
	final ForgeConfigSpec.ConfigValue<String> kits_starting_name;

	// Speed
	final ForgeConfigSpec.BooleanValue speed_enable;
	final ForgeConfigSpec.IntValue speed_max_walk;
	final ForgeConfigSpec.IntValue speed_max_fly;

	// Tpall
	final ForgeConfigSpec.BooleanValue tpall_enable;

	// Kickall
	final ForgeConfigSpec.BooleanValue kickall_enable;

	// Broadcast
	final ForgeConfigSpec.BooleanValue broadcast_enable;

	// Up
	final ForgeConfigSpec.BooleanValue up_enable;

	// AFK
	final ForgeConfigSpec.BooleanValue afk_auto;
	final ForgeConfigSpec.IntValue afk_auto_time;
	final ForgeConfigSpec.IntValue afk_auto_kick;
	final ForgeConfigSpec.BooleanValue afk_command;
	final ForgeConfigSpec.IntValue afk_command_cooldown;

	// Endc
	final ForgeConfigSpec.BooleanValue endc_enable;

	// Trash
	final ForgeConfigSpec.BooleanValue trash_enable;

	// Tempban
	final ForgeConfigSpec.BooleanValue tempban_enable;

	// Tempban
	final ForgeConfigSpec.BooleanValue check_enable;

	// Head
	final ForgeConfigSpec.BooleanValue head_enable;

	// Invsee
	final ForgeConfigSpec.BooleanValue invsee_enable;

	// Repair
	final ForgeConfigSpec.BooleanValue repair_enable;

	CommonConfig(final ForgeConfigSpec.Builder builder) {

		// Auto AFK
		builder.push("afk");
		afk_auto = builder.define("auto", true);
		afk_auto_time = builder.defineInRange("auto_time", 600, 1 , MAX);
		afk_auto_kick = builder.defineInRange("auto_kick", 0, 0 , MAX);
		builder.pop();

		builder.comment("Command Config").push("Commands");

		// Spawn
		builder.push("spawn");
		spawn_enable = builder
				.comment("Enable commands: /spawn, /setspawn")
				.define("enable", true);
		spawn_delay = builder.defineInRange("delay", 3, 0, MAX);
		spawn_cooldown = builder.defineInRange("cooldown",	0, 0, MAX);
		spawn_force_on_death = builder.define("force_spawn_on_death", false);
		builder.pop();

		// Homes
		builder.push("homes");
		homes_enable = builder
				.comment("Enable commands: /sethome, /delhome, /home")
				.define("enable", true);
		homes_delay = builder.defineInRange("delay", 3, 0, MAX);
		homes_cooldown = builder.defineInRange("cooldown",	0, 0, MAX);
		homes_limit = builder.defineInRange("limit",	3, 1, MAX);
		homes_limit_op = builder.defineInRange("limit_op",	5, 1, MAX);
		builder.pop();

		// Warps
		builder.push("warps");
		warps_enable = builder
				.comment("Enable commands: /setwarp, /delwarp, /warp")
				.define("enable", true);
		warps_delay = builder.defineInRange("delay", 3, 0, MAX);
		warps_cooldown = builder.defineInRange("cooldown",	0, 0, MAX);
		builder.pop();

		// Back
		builder.push("back");
		back_enable = builder
				.comment("Enable command: /back")
				.define("enable", true);
		back_death_enable = builder.define("enable_on_death", true);
		back_delay = builder.defineInRange("delay", 3, 0, MAX);
		back_cooldown = builder.defineInRange("cooldown",	0, 0, MAX);
		back_death_custom_cooldown = builder.defineInRange("death_custom_cooldown",	0, 0, MAX);
		builder.pop();

		// TPA
		builder.push("tpa");
		tpa_enable = builder
				.comment("Enable commands: /tpa, /tpahere, /tpaccept, /tpdeny")
				.define("enable", true);
		tpa_delay = builder.defineInRange("delay", 3, 0, MAX);
		tpa_cooldown = builder.defineInRange("cooldown",	0, 0, MAX);
		tpa_timeout = builder.defineInRange("timeout",	20, 0, MAX);
		builder.pop();

		// RNDTP
		builder.push("rndtp");
		rndtp_enable = builder
				.comment("Enable command: /rndtp")
				.define("enable", true);
		rndtp_delay = builder.defineInRange("delay", 3, 0, MAX);
		rndtp_cooldown = builder.defineInRange("cooldown",	0, 0, MAX);
		rndtp_range_min = builder.defineInRange("range_min", 500, 50 , 999);
		rndtp_range_max = builder.defineInRange("range_max", 2000, 1000 , MAX);
		builder.pop();

		// Suicide
		builder.push("suicide");
		suicide_enable = builder
				.comment("Enable command: /suicide")
				.define("enable", true);
		suicide_enable_player = builder.define("enable_player", true);
		suicide_player_cooldown = builder.defineInRange("cooldown_player", 20, 0 , MAX);
		builder.pop();

		// Time
		builder.push("time");
		time_enable = builder
				.comment("Enable commands: /day, /night")
				.define("enable", true);
		builder.pop();

		// Weather
		builder.push("weather");
		weather_enable = builder
				.comment("Enable commands: /sun, /rain, /thunder")
				.define("enable", true);
		builder.pop();

		// Heal
		builder.push("heal");
		heal_enable = builder
				.comment("Enable command: /heal")
				.define("enable", true);
		builder.pop();

		// GM
		builder.push("gm");
		gm_enable = builder
				.comment("Enable command: /gm")
				.define("enable", true);
		builder.pop();

		// Fly
		builder.push("fly");
		fly_enable = builder
				.comment("Enable command: /fly")
				.define("enable", true);
		builder.pop();

		// God
		builder.push("god");
		god_enable = builder
				.comment("Enable command: /god")
				.define("enable", true);
		builder.pop();

		// Top
		builder.push("top");
		top_enable = builder
				.comment("Enable command: /top")
				.define("enable", true);
		builder.pop();

		// Mute
		builder.push("mute");
		mute_enable = builder
				.comment("Enable commands: /mute, /unmute")
				.define("enable", true);
		builder.pop();

		// Kits
		builder.push("kits");
		kits_enable = builder
				.comment("Enable command: /kit")
				.define("enable", true);
		kits_starting = builder.define("starting_kit", true);
		kits_starting_name = builder.define("starting_kit_name", "tools");
		builder.pop();

		// Speed
		builder.push("speed");
		speed_enable = builder
				.comment("Enable command: /speed")
				.define("enable", true);
		speed_max_walk = builder.defineInRange("max_walk_speed", 20, 0 , 50);
		speed_max_fly = builder.defineInRange("max_fly_speed", 20, 0 , 50);
		builder.pop();

		// Tpall
		builder.push("tpall");
		tpall_enable = builder
				.comment("Enable command: /tpall")
				.define("enable", true);
		builder.pop();

		// Kickall
		builder.push("kickall");
		kickall_enable = builder
				.comment("Enable command: /kickall")
				.define("enable", true);
		builder.pop();

		// Broadcast
		builder.push("broadcast");
		broadcast_enable = builder
				.comment("Enable command: /broadcast")
				.define("enable", true);
		builder.pop();

		// Up
		builder.push("up");
		up_enable = builder
				.comment("Enable command: /up")
				.define("enable", true);
		builder.pop();

		// AFK
		builder.push("afk");
		afk_command = builder.define("command", true);
		afk_command_cooldown = builder.defineInRange("cooldown",	30, 0, MAX);
		builder.pop();

		// Endc
		builder.push("endc");
		endc_enable = builder
				.comment("Enable command: /endc")
				.define("enable", true);
		builder.pop();

		// Trash
		builder.push("trash");
		trash_enable = builder
				.comment("Enable command: /trash")
				.define("enable", true);
		builder.pop();

		// Tempban
		builder.push("tempban");
		tempban_enable = builder
				.comment("Enable commands: /tempban, /unban")
				.define("enable", true);
		builder.pop();

		// Check
		builder.push("tempban");
		check_enable = builder
				.comment("Enable command: /check")
				.define("enable", true);
		builder.pop();

		// Head
		builder.push("head");
		head_enable = builder
				.comment("Enable command: /head")
				.define("enable", true);
		builder.pop();

		// Invsee
		builder.push("invsee");
		invsee_enable = builder
				.comment("Enable command: /invsee")
				.define("enable", true);
		builder.pop();

		// Repair
		builder.push("repair");
		repair_enable = builder
				.comment("Enable command: /repair")
				.define("enable", true);
		builder.pop();
	}
}