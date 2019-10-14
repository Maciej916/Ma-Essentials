package com.maciej916.maessentials.classes;

import com.google.gson.annotations.SerializedName;
import com.maciej916.maessentials.data.DataManager;
import com.maciej916.maessentials.libs.Log;
import net.minecraft.entity.player.ServerPlayerEntity;

import java.security.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EssentialPlayer {
    private transient UUID playerUUID;
    private transient Homes homes = new Homes();
    private transient ArrayList<TeleportRequest> tpRequest = new ArrayList<>();

    @SerializedName("last_location")
    private Location lastLocation;

    @SerializedName("tpa_time")
    private Timestamp tpRequestTime;

    @SerializedName("randomtp_time")
    private Timestamp rndtpTime;

    public EssentialPlayer() { }

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
        DataManager.savePlayerData(playerUUID, this);
    }

    public Location getLastLocation() {
        return lastLocation;
    }


}
