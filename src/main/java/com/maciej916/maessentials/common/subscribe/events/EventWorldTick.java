package com.maciej916.maessentials.common.subscribe.events;

import com.maciej916.maessentials.MaEssentials;
import com.maciej916.maessentials.common.lib.Location;
import com.maciej916.maessentials.common.lib.player.EssentialPlayer;
import com.maciej916.maessentials.common.config.ModConfig;
import com.maciej916.maessentials.common.data.DataManager;
import com.maciej916.maessentials.common.util.TeleportUtils;
import com.maciej916.maessentials.common.util.TextUtils;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.maciej916.maessentials.common.util.LocationUtils.checkDetailedLocation;
import static com.maciej916.maessentials.common.util.ModUtils.*;
import static com.maciej916.maessentials.common.util.TimeUtils.currentTimestamp;

@Mod.EventBusSubscriber(modid = MaEssentials.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EventWorldTick {

    private static int tickCounter = 0;

    @SubscribeEvent
    public static void worldTick(TickEvent.WorldTickEvent event) {
        if (event.phase == TickEvent.Phase.END){
            if (tickCounter == 20) {
                TeleportUtils.checkTeleports();
                if ((event.world.getServer() != null && event.world.getServer().isDedicatedServer() && ModConfig.afk_auto) || isDev()) {
                    checkAfk(event.world.getServer().getPlayerList());
                }
                tickCounter = 0;
            } else {
                tickCounter++;
            }
        }
    }

    public static void checkAfk(PlayerList players) {
        try {
            if (players != null && players.getPlayers().size() > 0) {
                for (int i = players.getPlayers().size() - 1; i >= 0; i--) {
                    ServerPlayerEntity player = players.getPlayers().get(i);
                    if (player != null) {
                        EssentialPlayer eslPlayer = DataManager.getPlayer(player);
                        Location playerLocation = new Location(player);
                        Location lastLocation = eslPlayer.getTemp().getLocation();
                        if (lastLocation != null && checkDetailedLocation(playerLocation, lastLocation)) {
                            if (!eslPlayer.getTemp().isAfk()) {
                                if (currentTimestamp() - ModConfig.afk_auto_time  >= eslPlayer.getTemp().getLastMoveTime()) {
                                    TextUtils.sendGlobalChatMessage(players, "afk.maessentials.afk.true", player.getDisplayName());
                                    eslPlayer.getTemp().setAfk(true);
                                }
                            } else {
                                if (ModConfig.afk_auto_kick != 0 && currentTimestamp() - ModConfig.afk_auto_kick  >= eslPlayer.getTemp().getLastMoveTime()) {
//                                    kickPlayer(player, new TranslationTextComponent("kick.maessentials.server"), new TranslationTextComponent("kick.maessentials.player").getFormattedText());
                                    kickPlayer(player, new TranslationTextComponent("kick.maessentials.server"), new TranslationTextComponent("kick.maessentials.player").getString());
                                }
                            }
                        } else {
                            if (eslPlayer.getTemp().isAfk()) {
                                TextUtils.sendGlobalChatMessage(players, "afk.maessentials.afk.false", player.getDisplayName());
                                eslPlayer.getTemp().setAfk(false);
                            }
                            eslPlayer.getTemp().setLocation(playerLocation);
                        }
                    }
                }
            }
        } catch(Exception e) {
            System.out.println("checkAfk error:");
            System.out.println(e);
        }
    }
}
