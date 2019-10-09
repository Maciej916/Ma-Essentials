package com.maciej916.maessentials.libs;

import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;

import java.util.Optional;

import static com.maciej916.maessentials.MaEssentials.MODID;

public class Methods {

    private static String getVersion() {
        Optional<? extends ModContainer> o = ModList.get().getModContainerById(MODID);
        if (o.isPresent()) {
            return o.get().getModInfo().getVersion().toString();
        }
        return "NONE";
    }

    public static boolean isDev() {
        String version = getVersion();
        return version.equals("NONE");
    }
}
