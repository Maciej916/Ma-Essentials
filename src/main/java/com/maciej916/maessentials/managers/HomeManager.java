package com.maciej916.maessentials.managers;

import com.maciej916.maessentials.classes.Home;
import com.maciej916.maessentials.setup.Config;
import com.maciej916.maessentials.utils.Homes;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.command.CommandSource;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.entity.player.ServerPlayerEntity;
import org.apache.commons.io.FilenameUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class HomeManager {
    private static HashMap<UUID, Homes> uuidHomeHashMap = new HashMap<UUID, Homes>();

    public void init() {
        loadAllHomes();
    }

    public static final SuggestionProvider<CommandSource> HOME_SUGGEST = (context, builder) -> {
        ServerPlayerEntity player = context.getSource().asPlayer();
        Homes playerHomes = getPlayerHomes(player);
        return ISuggestionProvider.suggest(playerHomes.getHomeNames().stream().toArray(String[]::new), builder);
    };

    public static Homes getPlayerHomes(ServerPlayerEntity player) {
        UUID playerUUID = player.getUniqueID();
        if (uuidHomeHashMap.containsKey(playerUUID)) {
            Homes playerHomes = uuidHomeHashMap.get(playerUUID);
            return playerHomes;
        } else {
            Homes playerHomes = new Homes();
            uuidHomeHashMap.put(playerUUID, playerHomes);
            return playerHomes;
        }
    }

    public static void savePlayerHomes(ServerPlayerEntity player) {
        Homes playerHomes = getPlayerHomes(player);
        UUID playerUUID = player.getUniqueID();
        try {
            FileOutputStream fos = new FileOutputStream(Config.getMainCatalog()+"/homes/"+playerUUID.toString()+".dat");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(playerHomes.getHomes());
            oos.close();
            fos.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private static void loadPlayerHomes(String uuidString) {
        try {
            FileInputStream fis = new FileInputStream(Config.getMainCatalog()+"/homes/"+uuidString+".dat");
            ObjectInputStream ois = new ObjectInputStream(fis);
            ArrayList<Home> homeArrayList = (ArrayList) ois.readObject();
            UUID uid = UUID.fromString(uuidString);
            Homes playerHomes = new Homes(homeArrayList);
            uuidHomeHashMap.put(uid, playerHomes);
            ois.close();
            fis.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (ClassNotFoundException c) {
            c.printStackTrace();
        }
    }

    private void loadAllHomes() {
        File folder = new File(Config.getMainCatalog()+"/homes");
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles != null) {
            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile()) {
                    String uuidString = FilenameUtils.removeExtension(listOfFiles[i].getName());
                    loadPlayerHomes(uuidString);
                }
            }
        }
    }
}
