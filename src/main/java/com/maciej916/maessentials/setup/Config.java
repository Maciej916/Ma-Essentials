package com.maciej916.maessentials.setup;

import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

import java.io.File;

public class Config {
    private static String mainCatalog;

    public void init(FMLServerStartingEvent event) {
        if (event.getServer().isDedicatedServer()) {
            mainCatalog = System.getProperty("user.dir") + "/mods/MaEssentials";
        } else {
            mainCatalog = System.getProperty("user.dir") + "/saves/" + event.getServer().getFolderName() + "/MaEssentials";
        }

        new File(mainCatalog).mkdirs();
        new File(mainCatalog + "/homes").mkdirs();
        new File(mainCatalog + "/warps").mkdirs();
    }

    public static String getMainCatalog() {
        return mainCatalog;
    }
}
