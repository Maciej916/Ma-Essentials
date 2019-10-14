package com.maciej916.maessentials.libs;

import com.maciej916.maessentials.config.ConfigValues;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.maciej916.maessentials.MaEssentials.MODID;

public class Log {
    private static final Logger LOGGER = LogManager.getLogger();

    public static void log(String text) {
        LOGGER.info(MODID + " " + text);
    }

    public static void err(String text) {
        LOGGER.error(MODID + " " + text);
    }

    public static void debug(String text) {
        if (ConfigValues.debugMessages) {
            LOGGER.debug(MODID + " " + text);
        }
    }
}
