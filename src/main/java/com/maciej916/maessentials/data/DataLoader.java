package com.maciej916.maessentials.data;

import com.maciej916.maessentials.classes.Homes;
import com.maciej916.maessentials.classes.Location;
import com.maciej916.maessentials.config.Config;
import com.maciej916.maessentials.libs.Json;
import com.maciej916.maessentials.libs.Log;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

public class DataLoader {
    public static void init(FMLServerStartingEvent event) {
        DataManager.cleanPlayerData();
        DataManager.cleanWarpData();

        loadModData(event);
        loadKitsData();
        loadWarps();

        loadPlayerData();
        loadPlayerHomes();

        Log.debug("Mod data loaded");
    }

    private static ArrayList<String> loadCatalog(String catalog) {
        File folder = new File(Config.getWorldCatalog() + catalog);
        File[] listOfFiles = folder.listFiles();
        ArrayList<String> data = new ArrayList<>();
        if (listOfFiles != null) {
            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile()) {
                    String fileName = FilenameUtils.removeExtension(listOfFiles[i].getName());
                    data.add(fileName);
                }
            }
        }
        return data;
    }

    private static void loadModData(FMLServerStartingEvent event) {
        if (new File(Config.getWorldCatalog() + "data.json").isFile()) {
            Log.debug("Loading mod data");
            ModData modData = new ModData();
            modData = (ModData) Json.load("data", modData);
            DataManager.setModData(modData);
        } else {
            WorldInfo worldData = event.getServer().getWorld(DimensionType.getById(0)).getWorldInfo();
            Location spawnLocation = new Location(worldData.getSpawnX(), worldData.getSpawnY(), worldData.getSpawnZ(), 0);
            DataManager.getModData().setSpawnPoint(spawnLocation);
            DataManager.saveModData();
        }
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
        ArrayList<String> data = loadCatalog("warps");
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
        ArrayList<String> data = loadCatalog("players");
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
        ArrayList<String> data = loadCatalog("homes");
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
