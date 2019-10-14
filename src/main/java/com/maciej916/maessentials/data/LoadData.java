package com.maciej916.maessentials.data;

import com.maciej916.maessentials.MaEssentials;
import com.maciej916.maessentials.classes.EssentialPlayer;
import com.maciej916.maessentials.classes.Homes;
import com.maciej916.maessentials.classes.Location;
import com.maciej916.maessentials.config.Config;
import com.maciej916.maessentials.libs.JsonMethods;
import com.maciej916.maessentials.libs.Log;
import com.maciej916.maessentials.libs.PlayerHomes;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

public class LoadData {
    public static void init(FMLServerStartingEvent event) {
        DataManager.cleanPlayerData();



        PlayerData.cleanData();
        WarpData.cleanData();

        loadModData(event);
        loadWarps();




        loadPlayerData();
        loadPlayerHomes();

        Log.debug("Mod data loaded.");
    }


    private static ArrayList<String> loadCatalog(String catalog) {
        File folder = new File(Config.getMainCatalog() + catalog);
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

    private static void loadPlayerData() {
        ArrayList<String> data = loadCatalog("players");
        for (String fileName : data) {
            UUID playerUUID = UUID.fromString(fileName);
            Log.debug("Loading homes for player: " + playerUUID);
            try {
                EssentialPlayer playerData = new EssentialPlayer();
                playerData = (EssentialPlayer) JsonMethods.load("players/" + fileName, playerData);
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
                homes = (Homes) JsonMethods.load("homes/" + playerUUID, homes);
                DataManager.getPlayerData(playerUUID).setHomes(homes);
            } catch (Exception e) {
                Log.err("Error while loading homes for player: " + playerUUID);
                System.out.println(e);
            }
        }
    }









    private static void loadModData(FMLServerStartingEvent event) {
        if (new File(Config.getMainCatalog() + "data.json").isFile()) {
            Log.debug("Loading mod data");
            MaEssentials.modData = (ModData) JsonMethods.load("data", MaEssentials.modData);
        } else {
            WorldInfo worldData = event.getServer().getWorld(DimensionType.getById(0)).getWorldInfo();
            MaEssentials.modData.setSpawnPoint(new Location(worldData.getSpawnX(), worldData.getSpawnY(), worldData.getSpawnZ(), 0));
            saveModData();
        }
    }

    public static void saveModData() {
        Log.debug("Saving mod data");
        JsonMethods.save(MaEssentials.modData, "data");
    }

//    private static void loadPlayerHomes() {
//        File folder = new File(Config.getMainCatalog() + "homes");
//        File[] listOfFiles = folder.listFiles();
//        if (listOfFiles != null) {
//            for (int i = 0; i < listOfFiles.length; i++) {
//                if (listOfFiles[i].isFile()) {
//                    String uuidString = FilenameUtils.removeExtension(listOfFiles[i].getName());
//                    UUID playerUUID = UUID.fromString(uuidString);
//                    PlayerHomes homes = new PlayerHomes();
//                    homes = (PlayerHomes) JsonMethods.load("homes/" + uuidString, homes);
//                    PlayerData.addPlayerHomes(playerUUID, homes);
//                }
//            }
//        }
//    }

    public static void savePlayerHome(ServerPlayerEntity player, Object homes) {
        UUID uuid = player.getUniqueID();
        PlayerHomes playerHomes = (PlayerHomes) homes;
        JsonMethods.save(playerHomes, "homes/" + uuid);
    }

    private static void loadWarps() {
        File folder = new File(Config.getMainCatalog() + "warps");
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles != null) {
            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile()) {
                    String warpName = FilenameUtils.removeExtension(listOfFiles[i].getName());
                    Location warp = new Location();
                    warp = (Location) JsonMethods.load("warps/" + warpName, warp);
                    WarpData.addWarp(warpName, warp);
                }
            }
        }
    }

    public static void saveWarp(String name, Location location) {
        JsonMethods.save(location, "warps/" + name);
    }

    public static void removeWarp(String name) {
        File file = new File(Config.getMainCatalog()+"warps/" + name + ".json");
        file.delete();
    }

}
