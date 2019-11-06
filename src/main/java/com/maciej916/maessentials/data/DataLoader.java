package com.maciej916.maessentials.data;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.maciej916.maessentials.MaEssentials;
import com.maciej916.maessentials.classes.Location;
import com.maciej916.maessentials.classes.home.HomeData;
import com.maciej916.maessentials.classes.kit.KitData;
import com.maciej916.maessentials.classes.player.EssentialPlayer;
import com.maciej916.maessentials.classes.world.WorldData;
import com.maciej916.maessentials.classes.warp.WarpData;
import com.maciej916.maessentials.config.ConfigValues;
import com.maciej916.maessentials.data.old.OldPlayerData;
import com.maciej916.maessentials.data.old.ProfileUpdater;
import com.maciej916.maessentials.libs.Log;
import com.maciej916.maessentials.libs.Methods;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.maciej916.maessentials.libs.Methods.fileExist;

public class DataLoader {

    public static void setupMain(FMLCommonSetupEvent event) {
        try {
            Log.log("Setup main");
            ConfigValues.mainCatalog = System.getProperty("user.dir") + "/" + MaEssentials.MODID + "/";
            Log.debug("Main catalog is: " + ConfigValues.mainCatalog);

            new File(ConfigValues.mainCatalog).mkdirs();
            File targetFile = new File(ConfigValues.mainCatalog + "default_kits.json");
            if (!targetFile.exists()) {
                Log.log("Creating default_kits.json in main catalog");
                InputStream initialStream = MaEssentials.class.getResourceAsStream("/default_kits.json");
                byte[] buffer = new byte[initialStream.available()];
                initialStream.read(buffer);
                OutputStream outStream = new FileOutputStream(targetFile);
                outStream.write(buffer);
            }
        } catch (Exception e) {
            Log.err("Error in setupMain");
            throw new Error(e);
        }
    }

    public static void setupWorld(FMLServerStartingEvent event) {
        try {
            Log.log("Setup world");
            if (event.getServer().isDedicatedServer()) {
                Log.log("Mod is running on server");
                ConfigValues.worldCatalog = ConfigValues.mainCatalog;
            } else {
                Log.log("Mod is running on client");
                ConfigValues.worldCatalog = System.getProperty("user.dir") + "/saves/" + event.getServer().getFolderName() + "/" + MaEssentials.MODID + "/";
            }
            Log.debug("World catalog is: " + ConfigValues.worldCatalog);

            new File(ConfigValues.worldCatalog).mkdirs();
            new File(ConfigValues.worldCatalog + "homes").mkdirs();
            new File(ConfigValues.worldCatalog + "warps").mkdirs();
            new File(ConfigValues.worldCatalog + "players").mkdirs();

            if (!fileExist(ConfigValues.worldCatalog + "data.json")) {
                WorldInfo worldInfo = event.getServer().getWorld(DimensionType.getById(0)).getWorldInfo();
                Location spawnLocation = new Location(worldInfo.getSpawnX(), worldInfo.getSpawnY(), worldInfo.getSpawnZ(), 0);
                DataManager.getWorld().setSpawn(spawnLocation);
                DataManager.getWorld().saveData();
            }

            if (!fileExist(ConfigValues.worldCatalog + "kits.json")) {
                File def = new File(ConfigValues.mainCatalog + "default_kits.json");
                File des = new File(ConfigValues.worldCatalog + "kits.json");
                Files.copy(def.toPath(), des.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception e) {
            Log.err("Error in setupWorld");
            throw new Error(e);
        }
    }

    public static void load() {
        Log.log("Loading data");
        try {
            Log.debug("Clean data");
            DataManager.cleanData();
            Log.debug("Loading world...");
            loadWorld();
            Log.debug("Loading warps...");
            loadWarps();
            Log.debug("Loading kits...");
            loadKits();
            Log.debug("Loading players...");
            loadPlayers();
            Log.debug("Loading homes...");
            loadHomes();
            Log.log("Data loaded");
        } catch (Exception e) {
            Log.err("Error while loading data!");
            throw new Error(e);
        }
    }

    private static void loadWorld() throws Exception {
        WorldData worldData = new Gson().fromJson(Methods.loadFile(ConfigValues.worldCatalog, "data"), WorldData.class);
        DataManager.setWorldData(worldData);
    }

    private static void loadWarps() throws Exception {
        Map<String, Location> data = new HashMap<>();
        Methods.catalogFiles(ConfigValues.worldCatalog + "warps").forEach((n) -> {
            try {
                Location warp = new Gson().fromJson(Methods.loadFile(ConfigValues.worldCatalog + "warps/", n), Location.class);
                data.put(n, warp);
            } catch (Exception e) {
                Log.err("Failed to load warp: " + n);
            }
        });
        WarpData warpData = new WarpData(data);
        DataManager.setWarpData(warpData);
    }

    private static void loadKits() throws Exception {
        try {
            KitData kitData = new Gson().fromJson(Methods.loadFile(ConfigValues.worldCatalog, "kits"), KitData.class);
            DataManager.setKitData(kitData);
        } catch (JsonParseException ex) {
            Log.err("Failed to load kits!");
            Log.err(ex.toString());
        }
    }

    private static void loadPlayers() throws Exception {
        Methods.catalogFiles(ConfigValues.worldCatalog + "players").forEach((n) -> {
            try {
                try {
                    EssentialPlayer eslPlayer = new Gson().fromJson(Methods.loadFile(ConfigValues.worldCatalog + "players/", n), EssentialPlayer.class);
                    if (eslPlayer.getPlayerUUID() == null) {
                        Log.debug("Failed to load using new player, fallback to old.");
                        throw new Exception();
                    }
                    DataManager.setPlayerData(eslPlayer);
                } catch (Exception e) {
                    Log.log("Updading player profile: " + n);
                    OldPlayerData oldProfile = new Gson().fromJson(Methods.loadFile(ConfigValues.worldCatalog + "players/", n), OldPlayerData.class);
                    UUID playerUUID = UUID.fromString(n);
                    EssentialPlayer eslPlayer = ProfileUpdater.updateProfie(playerUUID, oldProfile);
                    eslPlayer.saveData();
                    DataManager.setPlayerData(eslPlayer);
                }
            } catch (Exception e) {
                Log.err("Failed to load player: " + n);
            }
        });
    }

    private static void loadHomes() throws Exception {
        Methods.catalogFiles(ConfigValues.worldCatalog + "homes").forEach((n) -> {
            try {
                HomeData homes = new Gson().fromJson(Methods.loadFile(ConfigValues.worldCatalog + "homes/", n), HomeData.class);
                UUID playerUUID = UUID.fromString(n);
                DataManager.getPlayer(playerUUID).setHomeData(homes);
            } catch (Exception e) {
                Log.err("Failed to load player homes: " + n);
            }
        });
    }
}
