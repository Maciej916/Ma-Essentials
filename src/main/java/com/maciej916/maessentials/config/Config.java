package com.maciej916.maessentials.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.maciej916.maessentials.libs.Log;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.File;

public class Config {
    private static final ForgeConfigSpec.Builder server = new ForgeConfigSpec.Builder();
    private static final ForgeConfigSpec config;
    private static String mainCatalog;

    public static ForgeConfigSpec.BooleanValue enableSpawn;
    public static ForgeConfigSpec.BooleanValue enableTime;
    public static ForgeConfigSpec.BooleanValue enableWeather;
    public static ForgeConfigSpec.BooleanValue enableHomes;
    public static ForgeConfigSpec.BooleanValue enableBack;
    public static ForgeConfigSpec.BooleanValue enableSuicide;
    public static ForgeConfigSpec.BooleanValue enableHeal;
    public static ForgeConfigSpec.BooleanValue enableGm;
    public static ForgeConfigSpec.BooleanValue enableFly;
    public static ForgeConfigSpec.BooleanValue enableWarps;
    public static ForgeConfigSpec.BooleanValue enableTpa;


    public static ForgeConfigSpec.IntValue maxHomes;
    public static ForgeConfigSpec.IntValue tpaRequestTeleportTime;
    public static ForgeConfigSpec.IntValue tpaRequestTimeout;

    static {
        setupConfig();
        config = server.build();
        Log.debug("Config has been initialized");
    }

    private static void setupConfig() {
        server.comment("Command Config").push("Commands");

        enableSpawn = server
                .comment("Enable: /spawn, /setspawn")
                .define("enableSpawn", true);

        enableTime = server
                .comment("Enable: /day, /night")
                .define("enableTime", true);

        enableWeather = server
                .comment("Enable: /sun, /rain, /thunder")
                .define("enableWeather", true);

        enableHomes = server
                .comment("Enable: /sethome, /delhome, /home")
                .define("enableHomes", true);

        enableBack = server
                .comment("Enable: /back")
                .define("enableBack", true);

        enableSuicide = server
                .comment("Enable: /suicide")
                .define("enableSuicide", true);

        enableHeal = server
                .comment("Enable: /heal")
                .define("enableHeal", true);

        enableGm = server
                .comment("Enable: /gm")
                .define("enableGm", true);

        enableFly = server
                .comment("Enable: /fly")
                .define("enableFly", true);

        enableWarps = server
                .comment("Enable: /setwarp, /delwarp, /warp")
                .define("enableWarps", true);

        enableTpa = server
                .comment("Enable: /tpa, /tpahere, /tpaccept, /tpdeny")
                .define("enableTpa", true);

        maxHomes = server.defineInRange("maxHomes", 5, 1 , 999);
        tpaRequestTeleportTime = server.defineInRange("tpaRequestTeleportTime", 3, 0 , 300);
        tpaRequestTimeout = server.defineInRange("tpaRequestTimeout", 20, 0 , 300);

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

    public static void setupMainCatalog(FMLServerStartingEvent event) {
        Log.debug("Creating main catalog");
        if (event.getServer().isDedicatedServer()) {
            Log.debug("Mod is running on server");
            mainCatalog = System.getProperty("user.dir") + "/ma-essentials/";
        } else {
            Log.debug("Mod is running on client");
            mainCatalog = System.getProperty("user.dir") + "/saves/" + event.getServer().getFolderName() + "/ma-essentials/";
        }
        Log.debug("Main catalog is: " + mainCatalog);
        new File(mainCatalog).mkdirs();
        new File(mainCatalog + "homes").mkdirs();
        new File(mainCatalog + "warps").mkdirs();
    }

    public static String getMainCatalog() {
        return mainCatalog;
    }
}
