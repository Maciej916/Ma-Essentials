package com.maciej916.maessentials.libs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.maciej916.maessentials.MaEssentials.MODID;
import static com.maciej916.maessentials.libs.Methods.isDev;

public class Log {
    private static final Logger LOGGER = LogManager.getLogger();

    public static void log(String text) {
        LOGGER.info(MODID + " " + text);
    }

    public static void err(String text) {
        LOGGER.error(MODID + " " + text);
    }

    public static void debug(String text) {
        if (isDev()) {
            LOGGER.debug(MODID + " " + text);
        }
    }
}
