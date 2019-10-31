package com.maciej916.maessentials.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.maciej916.maessentials.MaEssentials;
import com.maciej916.maessentials.libs.Log;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import static org.apache.logging.log4j.core.util.Loader.getClassLoader;

public class Config {
    private static final ForgeConfigSpec.Builder server = new ForgeConfigSpec.Builder();
    private static final ForgeConfigSpec config;
    private static String mainCatalog;
    private static String worldCatalog;
    private final static int MAX = Integer.MAX_VALUE;

    // Spawn
    public static ForgeConfigSpec.BooleanValue spawn_enable;
    public static ForgeConfigSpec.IntValue spawn_delay;
    public static ForgeConfigSpec.IntValue spawn_cooldown;
    public static ForgeConfigSpec.BooleanValue spawn_force_on_death;

    // Homes
    public static ForgeConfigSpec.BooleanValue homes_enable;
    public static ForgeConfigSpec.IntValue homes_delay;
    public static ForgeConfigSpec.IntValue homes_cooldown;
    public static ForgeConfigSpec.IntValue homes_limit;

    // Warps
    public static ForgeConfigSpec.BooleanValue warps_enable;
    public static ForgeConfigSpec.IntValue warps_delay;
    public static ForgeConfigSpec.IntValue warps_cooldown;

    // Back
    public static ForgeConfigSpec.BooleanValue back_enable;
    public static ForgeConfigSpec.IntValue back_delay;
    public static ForgeConfigSpec.IntValue back_cooldown;
    public static ForgeConfigSpec.BooleanValue back_death_enable;

    // TPA
    public static ForgeConfigSpec.BooleanValue tpa_enable;
    public static ForgeConfigSpec.IntValue tpa_delay;
    public static ForgeConfigSpec.IntValue tpa_cooldown;
    public static ForgeConfigSpec.IntValue tpa_timeout;

    // RNDTP
    public static ForgeConfigSpec.BooleanValue rndtp_enable;
    public static ForgeConfigSpec.IntValue rndtp_delay;
    public static ForgeConfigSpec.IntValue rndtp_cooldown;
    public static ForgeConfigSpec.IntValue rndtp_range_min;
    public static ForgeConfigSpec.IntValue rndtp_range_max;

    // Suicide
    public static ForgeConfigSpec.BooleanValue suicide_enable;
    public static ForgeConfigSpec.BooleanValue suicide_enable_player;
    public static ForgeConfigSpec.IntValue suicide_player_cooldown;

    // Time
    public static ForgeConfigSpec.BooleanValue time_enable;

    // Weather
    public static ForgeConfigSpec.BooleanValue weather_enable;

    // Heal
    public static ForgeConfigSpec.BooleanValue heal_enable;

    // GM
    public static ForgeConfigSpec.BooleanValue gm_enable;

    // Fly
    public static ForgeConfigSpec.BooleanValue fly_enable;

    // God
    public static ForgeConfigSpec.BooleanValue god_enable;

    // Top
    public static ForgeConfigSpec.BooleanValue top_enable;

    // Mute
    public static ForgeConfigSpec.BooleanValue mute_enable;

    // Kits
    public static ForgeConfigSpec.BooleanValue kits_enable;
    public static ForgeConfigSpec.BooleanValue kits_starting;
    public static ForgeConfigSpec.ConfigValue<String> kits_starting_name;

    // Speed
    public static ForgeConfigSpec.BooleanValue speed_enable;
    public static ForgeConfigSpec.IntValue speed_max_walk;
    public static ForgeConfigSpec.IntValue speed_max_fly;

    static {
        setupConfig();
        config = server.build();
        Log.debug("Config has been initialized");
    }

    private static void setupConfig() {
        server.comment("Command Config").push("Commands");

        // Spawn
        server.push("spawn");
            spawn_enable = server
                .comment("Enable commands: /spawn, /setspawn")
                .define("enable", true);
            spawn_delay = server.defineInRange("delay", 3, 0, MAX);
            spawn_cooldown = server.defineInRange("cooldown",	0, 0, MAX);
            spawn_force_on_death = server.define("force_spawn_on_death", false);
        server.pop();

        // Homes
        server.push("homes");
            homes_enable = server
                .comment("Enable commands: /sethome, /delhome, /home")
                .define("enable", true);
            homes_delay = server.defineInRange("delay", 3, 0, MAX);
            homes_cooldown = server.defineInRange("cooldown",	0, 0, MAX);
            homes_limit = server.defineInRange("limit",	3, 1, MAX);
        server.pop();

        // Warps
        server.push("warps");
            warps_enable = server
                .comment("Enable commands: /setwarp, /delwarp, /warp")
                .define("enable", true);
            warps_delay = server.defineInRange("delay", 3, 0, MAX);
            warps_cooldown = server.defineInRange("cooldown",	0, 0, MAX);
        server.pop();

        // Back
        server.push("back");
            back_enable = server
                .comment("Enable command: /back")
                .define("enable", true);
            back_death_enable = server.define("enable_on_death", true);
            back_delay = server.defineInRange("delay", 3, 0, MAX);
            back_cooldown = server.defineInRange("cooldown",	0, 0, MAX);
        server.pop();

        // TPA
        server.push("tpa");
            tpa_enable = server
                .comment("Enable commands: /tpa, /tpahere, /tpaccept, /tpdeny")
                .define("enable", true);
            tpa_delay = server.defineInRange("delay", 3, 0, MAX);
            tpa_cooldown = server.defineInRange("cooldown",	0, 0, MAX);
            tpa_timeout = server.defineInRange("timeout",	20, 0, MAX);
        server.pop();

        // RNDTP
        server.push("rndtp");
            rndtp_enable = server
                .comment("Enable command: /rndtp")
                .define("enable", true);
            rndtp_delay = server.defineInRange("delay", 3, 0, MAX);
            rndtp_cooldown = server.defineInRange("cooldown",	0, 0, MAX);
            rndtp_range_min = server.defineInRange("range_min", 500, 50 , 999);
            rndtp_range_max = server.defineInRange("range_max", 2000, 1000 , MAX);
        server.pop();

        // Suicide
        server.push("suicide");
            suicide_enable = server
                .comment("Enable command: /suicide")
                .define("enable", true);
            suicide_enable_player = server.define("enable_player", true);
            suicide_player_cooldown = server.defineInRange("cooldown_player", 20, 0 , MAX);
        server.pop();

        // Time
        server.push("time");
            time_enable = server
                .comment("Enable commands: /day, /night")
                .define("enable", true);
        server.pop();

        // Weather
        server.push("weather");
            weather_enable = server
                .comment("Enable commands: /sun, /rain, /thunder")
                .define("enable", true);
        server.pop();

        // Heal
        server.push("heal");
        heal_enable = server
            .comment("Enable command: /heal")
            .define("enable", true);
        server.pop();

        // GM
        server.push("gm");
            gm_enable = server
                .comment("Enable command: /gm")
                .define("enable", true);
        server.pop();

        // Fly
        server.push("fly");
            fly_enable = server
                .comment("Enable command: /fly")
                .define("enable", true);
        server.pop();

        // God
        server.push("god");
            god_enable = server
                .comment("Enable command: /god")
                .define("enable", true);
        server.pop();

        // Top
        server.push("top");
        top_enable = server
                .comment("Enable command: /top")
                .define("enable", true);
        server.pop();

        // Mute
        server.push("mute");
        mute_enable = server
                .comment("Enable commands: /mute, /unmute")
                .define("enable", true);
        server.pop();

        // Kits
        server.push("kits");
        kits_enable = server
                .comment("Enable command: /kit")
                .define("enable", true);
        kits_starting = server.define("starting_kit", true);
        kits_starting_name = server.define("starting_kit_name", "tools");
        server.pop();

        // Speed
        server.push("speed");
        speed_enable = server
                .comment("Enable command: /speed")
                .define("enable", true);
        speed_max_walk = server.defineInRange("max_walk_speed", 20, 0 , 50);
        speed_max_fly = server.defineInRange("max_fly_speed", 20, 0 , 50);
        server.pop();
    }

    public static void loadConfig() {
        String path = FMLPaths.CONFIGDIR.get().resolve("ma-essentials.toml").toString();
        Log.debug("Loading config: " + path);
        final CommentedFileConfig file = CommentedFileConfig.builder(new File(path)).sync().autosave().writingMode(WritingMode.REPLACE).build();
        Log.debug("Built config: " + path);
        file.load();
        Log.debug("Loaded config: " + path);
        config.setConfig(file);
        ConfigValues.init();
    }

    public static void setupMainCatalog() {
        Log.log("Setup main catalog");
        mainCatalog = System.getProperty("user.dir") + "/ma-essentials/";
        Log.debug("Main catalog is: " + mainCatalog);
        try {
            Log.log("Creating main catalogs and files");

            new File(mainCatalog).mkdirs();

            File targetFile = new File(mainCatalog + "default_kits.json");
            if (!targetFile.exists()) {
                Log.log("Creating default_kits.json in main catalog");

                InputStream initialStream = MaEssentials.class.getResourceAsStream("/default_kits.json");
                byte[] buffer = new byte[initialStream.available()];
                initialStream.read(buffer);
                OutputStream outStream = new FileOutputStream(targetFile);
                outStream.write(buffer);
            }
        } catch (Exception e) {
            Log.log("Error in setupMainCatalog");
            System.out.println(e);
        }
    }

    public static void setupWorldCatalog(FMLServerStartingEvent event) {
        Log.log("Setup world catalog");

        mainCatalog = System.getProperty("user.dir") + "/ma-essentials/";
        if (event.getServer().isDedicatedServer()) {
            Log.log("Mod is running on server");
            worldCatalog = mainCatalog;
        } else {
            Log.log("Mod is running on client");
            worldCatalog = System.getProperty("user.dir") + "/saves/" + event.getServer().getFolderName() + "/ma-essentials/";
        }

        Log.debug("World catalog is: " + worldCatalog);

        try {
            Log.log("Creating world catalogs and files");

            new File(worldCatalog).mkdirs();
            new File(worldCatalog + "homes").mkdirs();
            new File(worldCatalog + "warps").mkdirs();
            new File(worldCatalog + "players").mkdirs();

            File targetFile = new File(mainCatalog + "default_kits.json");
            File destWorld = new File(worldCatalog + "kits.json");
            if (!destWorld.exists()) {
                Log.log("Kit file not exist, creating from default");
                Files.copy(targetFile.toPath(), destWorld.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception e) {
            Log.log("Error in setupWorldCatalog");
            System.out.println(e);
        }
    }

    public static String getMainCatalog() {
        return mainCatalog;
    }

    public static String getWorldCatalog() {
        return worldCatalog;
    }
}
