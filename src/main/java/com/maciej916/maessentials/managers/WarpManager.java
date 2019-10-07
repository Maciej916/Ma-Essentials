package com.maciej916.maessentials.managers;

import com.maciej916.maessentials.classes.Warp;
import com.maciej916.maessentials.setup.Config;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.command.CommandSource;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.entity.player.ServerPlayerEntity;
import org.apache.commons.io.FilenameUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WarpManager {
private static HashMap<String, Warp> warpsHashMap = new HashMap<String, Warp>();

    public void init() {
        loadAllWarps();
    }

    public static final SuggestionProvider<CommandSource> WARP_SUGGEST = (context, builder) -> {
        List<String> keys = new ArrayList<>(warpsHashMap.keySet());
        return ISuggestionProvider.suggest(keys.stream().toArray(String[]::new), builder);
    };

    public static HashMap<String, Warp> getAllWarps() {
        return warpsHashMap;
    }

    public static Warp getWarp(String warpName) {
        if (warpsHashMap.containsKey(warpName)) {
            return warpsHashMap.get(warpName);
        }
        return null;
    }

    public static boolean setWarp(ServerPlayerEntity player, String newWarpName) {
        if (getWarp(newWarpName) == null) {
            Warp newWarp = new Warp(player, newWarpName);
            warpsHashMap.put(newWarpName, newWarp);
            saveWarp(newWarp);
            return true;
        }
        return false;
    }

    public static boolean delWarp(String newWarpName) {
        if (getWarp(newWarpName) != null) {
            warpsHashMap.remove(newWarpName);
            deleteWarp(newWarpName);
            return true;
        }
        return false;
    }

    private static void deleteWarp(String warpName) {
        File file = new File(Config.getMainCatalog()+"/warps/"+warpName+".dat");
        file.delete();
    }

    private static void saveWarp(Warp warp) {
        try {
            FileOutputStream fos = new FileOutputStream(Config.getMainCatalog()+"/warps/"+warp.getWarpName()+".dat");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(warp);
            oos.close();
            fos.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private static void loadWarp(String warpName) {
        try {
            FileInputStream fis = new FileInputStream(Config.getMainCatalog()+"/warps/"+warpName+".dat");
            ObjectInputStream ois = new ObjectInputStream(fis);

            Warp thisWarp = (Warp) ois.readObject();
            warpsHashMap.put(warpName, thisWarp);
            ois.close();
            fis.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (ClassNotFoundException c) {
            c.printStackTrace();
        }
    }

    private static void loadAllWarps() {
        File folder = new File(Config.getMainCatalog()+"/warps");
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles != null) {
            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile()) {
                    String warpName = FilenameUtils.removeExtension(listOfFiles[i].getName());
                    loadWarp(warpName);
                }
            }
        }
    }
}
