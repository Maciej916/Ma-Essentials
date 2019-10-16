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

    @SerializedName("last_teleport_time")
    private long lastTeleportTime;

    @SerializedName("randomtp_time")
    private long rndtpTime;

    public PlayerData() { }

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

    public void setLastTeleportTime(long lastTeleportTime) {
        this.lastTeleportTime = lastTeleportTime;
    }

    public long getLastTeleportTime() {
        return lastTeleportTime;
    }
}
