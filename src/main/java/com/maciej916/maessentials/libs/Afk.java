package com.maciej916.maessentials.libs;

import com.maciej916.maessentials.classes.Location;
import com.maciej916.maessentials.classes.player.EssentialPlayer;
import com.maciej916.maessentials.config.ConfigValues;
import com.maciej916.maessentials.data.DataManager;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.text.StringTextComponent;

import static com.maciej916.maessentials.libs.Methods.*;

public class Afk {

    public static void checkAfk(PlayerList players) {
        for (ServerPlayerEntity player : players.getPlayers()) {
            EssentialPlayer eslPlayer = DataManager.getPlayer(player);
            Location playerLocation = new Location(player);
            Location lastLocation = eslPlayer.getTemp().getLocation();
            if (lastLocation != null && checkDetailedLocation(playerLocation, lastLocation)) {
                if (!eslPlayer.getTemp().isAfk()) {
                    if (currentTimestamp() - ConfigValues.afk_auto_time  >= eslPlayer.getTemp().getLastMoveTime()) {
                        player.sendMessage(Methods.formatText("afk.maessentials.afk.true", player.getDisplayName()));
                        eslPlayer.getTemp().setAfk(true);
                    }
                } else {
                    if (ConfigValues.afk_auto_kick != 0 && currentTimestamp() - ConfigValues.afk_auto_kick  >= eslPlayer.getTemp().getLastMoveTime()) {
                        kickPlayer(player, new StringTextComponent("Server"), "Being afk for too long!");
                    }
                }
            } else {
                if (eslPlayer.getTemp().isAfk()) {
                    player.sendMessage(Methods.formatText("afk.maessentials.afk.false", player.getDisplayName()));
                    eslPlayer.getTemp().setAfk(false);
                }
                eslPlayer.getTemp().setLocation(playerLocation);
            }
        }
    }
}

