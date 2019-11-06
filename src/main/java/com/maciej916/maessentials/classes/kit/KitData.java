package com.maciej916.maessentials.classes.kit;

import java.util.HashMap;
import java.util.Map;

public class KitData {

    private Map<String, Kit> kits = new HashMap<>();

    public Map<String, Kit> getKits() {
        return kits;
    }

    public Kit getKit(String name) {
        return kits.get(name);
    }

}
