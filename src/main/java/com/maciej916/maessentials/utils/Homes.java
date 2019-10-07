package com.maciej916.maessentials.utils;

import com.maciej916.maessentials.classes.Home;
import com.maciej916.maessentials.managers.HomeManager;
import net.minecraft.entity.player.ServerPlayerEntity;

import java.util.ArrayList;
import java.util.List;

public class Homes {
    private static ArrayList<Home> userHomes = new ArrayList<Home>();

    public Homes() {}

    public Homes(ArrayList<Home> newUserHomes) {
        this.userHomes = newUserHomes;
    }

    public Home setHome(ServerPlayerEntity player, String newHomeName) {
        if (getHome(newHomeName) != null) {
            delHome(player, newHomeName);
        }
        Home newHome = new Home(player, newHomeName);
        userHomes.add(newHome);
        HomeManager.savePlayerHomes(player);
        return newHome;
    }

    public void delHome(ServerPlayerEntity player, String delHomeName) {
        for (int i=0; i < userHomes.size(); i++){
            Home thisHome = userHomes.get(i);
            if (thisHome.getHomeName().equals(delHomeName)) {
                userHomes.remove(i);
                HomeManager.savePlayerHomes(player);
            }
        }
    }

    public Home getHome(String homeName) {
        for (int i=0; i < userHomes.size(); i++){
            Home thisHome = userHomes.get(i);
            if (thisHome.getHomeName().equals(homeName)) {
                return thisHome;
            }
        }
        return null;
    }

    public List<String> getHomeNames() {
        List<String> homeNames = new ArrayList<>();
        for (int i=0; i < userHomes.size(); i++){
            Home thisHome = userHomes.get(i);
            homeNames.add(thisHome.getHomeName());
        }
        return homeNames;
    }

    public ArrayList<Home> getHomes() {
        return userHomes;
    }

}
