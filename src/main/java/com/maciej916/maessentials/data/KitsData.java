package com.maciej916.maessentials.data;

import com.google.gson.annotations.SerializedName;
import com.maciej916.maessentials.classes.KitItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class KitsData implements Serializable {
    @SerializedName("kits")
    HashMap<String, KitsData> kits = new HashMap<>();

    private long duration;
    private ArrayList<KitItem> items;

    public KitsData() {}

    private KitsData(long duration, ArrayList<KitItem> items) {
        this.duration = duration;
        this.items = items;
    }

    public HashMap<String, KitsData> getKits() {
        return kits;
    }

    public KitsData getKit(String name) {
        return kits.get(name);
    }

    public ArrayList<KitItem> getItems() {
        return items;
    }

    public long getDuration() {
        return duration;
    }
}
