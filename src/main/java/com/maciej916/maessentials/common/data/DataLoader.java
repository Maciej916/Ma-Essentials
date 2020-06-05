package com.maciej916.maessentials.common.data;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.maciej916.maessentials.MaEssentials;
import com.maciej916.maessentials.common.lib.Location;
import com.maciej916.maessentials.common.lib.home.HomeData;
import com.maciej916.maessentials.common.lib.kit.KitData;
import com.maciej916.maessentials.common.lib.player.EssentialPlayer;
import com.maciej916.maessentials.common.lib.warp.WarpData;
import com.maciej916.maessentials.common.lib.world.WorldData;
import com.maciej916.maessentials.common.config.ModConfig;
import com.maciej916.maessentials.common.data.old.OldProfile;
import com.maciej916.maessentials.common.data.old.OldWorld;
import com.maciej916.maessentials.common.data.old.ProfileUpdater;
import com.maciej916.maessentials.common.util.FileUtils;
import com.maciej916.maessentials.common.util.LogUtils;
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

import static com.maciej916.maessentials.common.util.FileUtils.fileExist;

public class DataLoader {

    public static void setupMain(FMLCommonSetupEvent event) {
        try {
            LogUtils.log("Setup main");
            ModConfig.mainCatalog = System.getProperty("user.dir") + "/" + MaEssentials.MODID + "/";
            LogUtils.debug("Main catalog is: " + ModConfig.mainCatalog);

            new File(ModConfig.mainCatalog).mkdirs();
            File targetFile = new File(ModConfig.mainCatalog + "default_kits.json");
            if (!targetFile.exists()) {
                LogUtils.log("Creating default_kits.json in main catalog");
                InputStream initialStream = MaEssentials.class.getResourceAsStream("/default_kits.json");
                byte[] buffer = new byte[initialStream.available()];
                initialStream.read(buffer);
                OutputStream outStream = new FileOutputStream(targetFile);
                outStream.write(buffer);
            }
        } catch (Exception e) {
            LogUtils.err("Error in setupMain");
            throw new Error(e);
        }
    }

    public static void setupWorld(FMLServerStartingEvent event) {
        try {
            LogUtils.log("Setup world");
            if (event.getServer().isDedicatedServer()) {
                LogUtils.log("Mod is running on server");
                ModConfig.worldCatalog = ModConfig.mainCatalog;
            } else {
                LogUtils.log("Mod is running on client");
                ModConfig.worldCatalog = System.getProperty("user.dir") + "/saves/" + event.getServer().getFolderName() + "/" + MaEssentials.MODID + "/";
            }
            LogUtils.debug("World catalog is: " + ModConfig.worldCatalog);

            new File(ModConfig.worldCatalog).mkdirs();
            new File(ModConfig.worldCatalog + "homes").mkdirs();
            new File(ModConfig.worldCatalog + "warps").mkdirs();
            new File(ModConfig.worldCatalog + "players").mkdirs();

            if (!fileExist(ModConfig.worldCatalog + "data.json")) {
                WorldInfo worldInfo = event.getServer().getWorld(DimensionType.getById(0)).getWorldInfo();
                Location spawnLocation = new Location(worldInfo.getSpawnX(), worldInfo.getSpawnY(), worldInfo.getSpawnZ(), 0);
                DataManager.getWorld().setSpawn(spawnLocation);
                DataManager.getWorld().saveData();
            }

            if (!fileExist(ModConfig.worldCatalog + "kits.json")) {
                File def = new File(ModConfig.mainCatalog + "default_kits.json");
                File des = new File(ModConfig.worldCatalog + "kits.json");
                Files.copy(def.toPath(), des.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception e) {
            LogUtils.err("Error in setupWorld");
            throw new Error(e);
        }
    }

    public static void load() {
        LogUtils.log("Loading data");
        try {
            LogUtils.debug("Clean data");
            DataManager.cleanData();
            LogUtils.debug("Loading world...");
            loadWorld();
            LogUtils.debug("Loading warps...");
            loadWarps();
            LogUtils.debug("Loading kits...");
            loadKits();
            LogUtils.debug("Loading players...");
            loadPlayers();
            LogUtils.debug("Loading homes...");
            loadHomes();
            LogUtils.log("Data loaded");
        } catch (Exception e) {
            LogUtils.err("Error while loading data!");
            throw new Error(e);
        }
    }

    private static void loadWorld() throws Exception {
        try {
            WorldData worldData = new Gson().fromJson(FileUtils.loadFile(ModConfig.worldCatalog, "data"), WorldData.class);
            if (worldData.getSpawn() == null) {
                LogUtils.debug("Failed to load new world data, fallback to old.");
                throw new Exception();
            }
            DataManager.setWorldData(worldData);
        } catch (Exception e) {
            LogUtils.log("Updading world data.");
            OldWorld oldWorld = new Gson().fromJson(FileUtils.loadFile(ModConfig.worldCatalog, "data"), OldWorld.class);
            DataManager.getWorld().setSpawn(oldWorld.spawnLocation);
            DataManager.getWorld().saveData();
        }
    }

    private static void loadWarps() throws Exception {
        Map<String, Location> data = new HashMap<>();
        FileUtils.catalogFiles(ModConfig.worldCatalog + "warps").forEach((n) -> {
            try {
                Location warp = new Gson().fromJson(FileUtils.loadFile(ModConfig.worldCatalog + "warps/", n), Location.class);
                data.put(n, warp);
            } catch (Exception e) {
                LogUtils.err("Failed to load warp: " + n);
            }
        });
        WarpData warpData = new WarpData(data);
        DataManager.setWarpData(warpData);
    }

    private static void loadKits() throws Exception {
        try {
            KitData kitData = new Gson().fromJson(FileUtils.loadFile(ModConfig.worldCatalog, "kits"), KitData.class);
            DataManager.setKitData(kitData);
        } catch (JsonParseException ex) {
            LogUtils.err("Failed to load kits!");
            LogUtils.err(ex.toString());
        }
    }

    private static void loadPlayers() throws Exception {
        FileUtils.catalogFiles(ModConfig.worldCatalog + "players").forEach((n) -> {
            try {
                try {
                    EssentialPlayer eslPlayer = new Gson().fromJson(FileUtils.loadFile(ModConfig.worldCatalog + "players/", n), EssentialPlayer.class);
                    if (eslPlayer.getPlayerUUID() == null) {
                        LogUtils.debug("Failed to load using new player, fallback to old.");
                        throw new Exception();
                    }
                    DataManager.setPlayerData(eslPlayer);
                } catch (Exception e) {
                    LogUtils.log("Updading player profile: " + n);
                    OldProfile oldProfile = new Gson().fromJson(FileUtils.loadFile(ModConfig.worldCatalog + "players/", n), OldProfile.class);
                    UUID playerUUID = UUID.fromString(n);
                    EssentialPlayer eslPlayer = ProfileUpdater.updateProfie(playerUUID, oldProfile);
                    eslPlayer.saveData();
                    DataManager.setPlayerData(eslPlayer);
                }
            } catch (Exception e) {
                LogUtils.err("Failed to load player: " + n);
            }
        });
    }

    private static void loadHomes() throws Exception {
        FileUtils.catalogFiles(ModConfig.worldCatalog + "homes").forEach((n) -> {
            try {
                HomeData homes = new Gson().fromJson(FileUtils.loadFile(ModConfig.worldCatalog + "homes/", n), HomeData.class);
                UUID playerUUID = UUID.fromString(n);
                DataManager.getPlayer(playerUUID).setHomeData(homes);
            } catch (Exception e) {
                LogUtils.err("Failed to load player homes: " + n);
            }
        });
    }
}
