package com.maciej916.maessentials.data;

import com.maciej916.maessentials.MaEssentials;
import com.maciej916.maessentials.classes.Homes;
import com.maciej916.maessentials.classes.Location;
import com.maciej916.maessentials.config.ConfigValues;
import com.maciej916.maessentials.libs.Json;
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
import java.util.ArrayList;
import java.util.UUID;

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

            if (!new File(ConfigValues.worldCatalog + "data.json").exists()) {
                WorldInfo worldData = event.getServer().getWorld(DimensionType.getById(0)).getWorldInfo();
                Location spawnLocation = new Location(worldData.getSpawnX(), worldData.getSpawnY(), worldData.getSpawnZ(), 0);
                DataManager.getModData().setSpawnPoint(spawnLocation);
                DataManager.saveModData();
            }

            File destWorld = new File(ConfigValues.worldCatalog + "kits.json");
            if (!destWorld.exists()) {
                File targetFile = new File(ConfigValues.mainCatalog + "default_kits.json");
                Log.log("Kit file not exist, creating from default");
                Files.copy(targetFile.toPath(), destWorld.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception e) {
            Log.err("Error in setupWorld");
            throw new Error(e);
        }
    }

    public static void load() {
        DataManager.cleanPlayerData();
        DataManager.cleanWarpData();

        loadModData();
        loadKitsData();
        loadWarps();

        loadPlayerData();
        loadPlayerHomes();

        Log.debug("Mod data loaded");
    }

    private static void loadModData() {
        Log.debug("Loading mod data");
        ModData modData = new ModData();
        modData = (ModData) Json.load("data", modData);
        DataManager.setModData(modData);
    }

    private static void loadKitsData() {
        try {
            Log.debug("Loading kits data");
            KitsData kitsData = new KitsData();
            kitsData = (KitsData) Json.load("kits", kitsData);
            DataManager.setKitsData(kitsData);
        } catch (Exception e) {
            Log.err("Error while loading kits");
            System.out.println(e);
        }
    }

    private static void loadWarps() {
        ArrayList<String> data = Methods.catalogFiles(ConfigValues.worldCatalog + "warps");

        for (String fileName : data) {
            Log.debug("Loading warp: " + fileName);
            try {
                Location warpLocation = new Location();
                warpLocation = (Location) Json.load("warps/" + fileName, warpLocation);
                DataManager.getWarpData().addWarp(fileName, warpLocation);
            } catch (Exception e) {
                Log.err("Error while loading warp: " + fileName);
                System.out.println(e);
            }
        }
    }

    private static void loadPlayerData() {
        ArrayList<String> data = Methods.catalogFiles(ConfigValues.worldCatalog + "players");

        for (String fileName : data) {
            UUID playerUUID = UUID.fromString(fileName);
            Log.debug("Loading homes for player: " + playerUUID);
            try {
                PlayerData playerData = new PlayerData();
                playerData = (PlayerData) Json.load("players/" + fileName, playerData);
                playerData.setPlayerUUID(playerUUID);
                DataManager.setPlayerData(playerUUID, playerData);
            } catch (Exception e) {
                Log.err("Error while loading data for player: " + playerUUID);
                System.out.println(e);
            }
        }
    }

    private static void loadPlayerHomes() {
        ArrayList<String> data = Methods.catalogFiles(ConfigValues.worldCatalog + "homes");

        for (String fileName : data) {
            UUID playerUUID = UUID.fromString(fileName);
            Log.debug("Loading data for player: " + playerUUID);
            try {
                Homes homes = new Homes();
                homes = (Homes) Json.load("homes/" + playerUUID, homes);
                DataManager.getPlayerData(playerUUID).setHomes(homes);
            } catch (Exception e) {
                Log.err("Error while loading homes for player: " + playerUUID);
                System.out.println(e);
            }
        }
    }
}
