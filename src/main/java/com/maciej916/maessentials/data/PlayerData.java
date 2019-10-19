package com.maciej916.maessentials.data;

import com.google.gson.annotations.SerializedName;
import com.maciej916.maessentials.classes.Homes;
import com.maciej916.maessentials.classes.Location;
import com.maciej916.maessentials.libs.Log;
import net.minecraft.entity.player.ServerPlayerEntity;

import java.util.HashMap;
import java.util.UUID;

public class PlayerData {
    private transient UUID playerUUID;
    private transient Homes homes = new Homes();

    @SerializedName("last_location")
    private Location lastLocation;

    @SerializedName("spawn_time")
    private long spawnTime;

    @SerializedName("home_time")
    private long homeTime;

    @SerializedName("warp_time")
    private long warpTime;

    @SerializedName("back_time")
    private long backtime;

    @SerializedName("teleport_request_time")
    private long teleportRequestTime;

    @SerializedName("rndtp_time")
    private long rndtpTime;

    @SerializedName("suicide_time")
    private long suicideTime;

    public PlayerData() { }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public void setPlayerUUID(UUID playerUUID) {
        this.playerUUID = playerUUID;
    }

    public void setHomes(Homes homes) {
        this.homes = homes;
    }

    public boolean setHome(ServerPlayerEntity player, String name) {
        Location homeLocation = new Location(player);
        if (homes.addHome(homeLocation, name)) {
            Log.debug("Player " + playerUUID + " created home " + name);
            DataManager.savePlayerHome(playerUUID, homes);
            return true;
        } else {
            return false;
        }
    }

    public boolean delHome(String name) {
        if (homes.removeHome(name)) {
            Log.debug("Player " + playerUUID + " deleted home " + name);
            DataManager.savePlayerHome(playerUUID, homes);
            return true;
        } else {
            return false;
        }
    }

    public HashMap<String, Location> getHomes() {
        return homes.getHomes();
    }

    public void setLastLocation(Location lastLocation) {
        this.lastLocation = lastLocation;
    }

    public Location getLastLocation() {
        return lastLocation;
    }

    public void setSpawnTime(long spawnTime) {
        this.spawnTime = spawnTime;
    }

    public long getSpawnTime() {
        return spawnTime;
    }

    public void setHomeTime(long homeTime) {
        this.homeTime = homeTime;
    }

    public long getHomeTime() {
        return homeTime;
    }

    public void setWarpTime(long warpTime) {
        this.warpTime = warpTime;
    }

    public long getWarpTime() {
        return warpTime;
    }

    public void setBacktime(long backtime) {
        this.backtime = backtime;
    }

    public long getBacktime() {
        return backtime;
    }

    public void setTeleportRequestTime(long teleportRequestTime) {
        this.teleportRequestTime = teleportRequestTime;
    }

    public long getTeleportRequestTime() {
        return teleportRequestTime;
    }

    public void setRndtpTime(long rndtpTime) {
        this.rndtpTime = rndtpTime;
    }

    public long getRndtpTime() {
        return rndtpTime;
    }

    public void setSuicideTime(long suicideTime) {
        this.suicideTime = suicideTime;
    }

    public long getSuicideTime() {
        return suicideTime;
    }
}

