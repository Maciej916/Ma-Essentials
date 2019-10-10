package com.maciej916.maessentials.config;

public class ConfigValues {

    public static Boolean enableSpawn;
    public static Boolean enableTime;
    public static Boolean enableWeather;
    public static Boolean enableHomes;
    public static Boolean enableBack;
    public static Boolean enableSuicide;
    public static Boolean enableHeal;
    public static Boolean enableGm;
    public static Boolean enableFly;
    public static Boolean enableWarps;
    public static Boolean enableTpa;

    public static int maxHomes;
    public static int tpaRequestTeleportTime;
    public static int tpaRequestTimeout;

    public static void init() {
        enableSpawn = Config.enableSpawn.get();
        enableTime = Config.enableTime.get();
        enableWeather = Config.enableWeather.get();
        enableHomes = Config.enableHomes.get();
        enableBack = Config.enableBack.get();
        enableSuicide = Config.enableSuicide.get();
        enableHeal = Config.enableHeal.get();
        enableGm = Config.enableGm.get();
        enableFly = Config.enableFly.get();
        enableWarps = Config.enableWarps.get();
        enableTpa = Config.enableTpa.get();

        maxHomes = Config.maxHomes.get();
        tpaRequestTeleportTime = Config.tpaRequestTeleportTime.get();
        tpaRequestTimeout = Config.tpaRequestTimeout.get();
    }
}