package com.maciej916.maessentials.common.lib;

import java.util.HashMap;
import java.util.Map;

public class Storage {
    private Map<String, Object> data = new HashMap<>();

    public Storage() {

    }

    public Storage(Map<String, Object> data) {
        this.data = data;
    }

    public void setData(String name, Object newData) {
        this.data.put(name, newData);
    }

    public void setData(String key, String name, Object setData) {
        if (!this.data.containsKey(key)) {
            this.data.put(key, new HashMap<String, Object>());
        }
        ((Map)this.data.get(key)).put(name, setData);
    }

    public Map<String, Object> getData() {
        return data;
    }

    public Object getData(String name) {
        return this.data.get(name);
    }

    public Object getData(String key, String name) {
        return ((Map)this.data.get(key)).get(name);
    }

    public void delData(String name) {
        this.data.remove(name);
    }

    public void delData(String key, String name) {
        ((Map)this.data.get(key)).remove(name);
    }
}
