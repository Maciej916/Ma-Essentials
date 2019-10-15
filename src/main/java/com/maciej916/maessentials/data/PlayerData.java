package com.maciej916.maessentials.data;

import com.maciej916.maessentials.classes.Location;
import com.maciej916.maessentials.classes.TeleportRequest;
import com.maciej916.maessentials.config.ConfigValues;
import com.maciej916.maessentials.libs.PlayerHomes;
import com.maciej916.maessentials.libs.Teleport;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.command.CommandSource;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class PlayerData {
    private static HashMap<UUID, PlayerHomes> playerHomes = new HashMap<>();
    private static HashMap<ServerPlayerEntity, Location> playerLastLocation = new HashMap<>();
    private static ArrayList<TeleportRequest> teleportRequests = new ArrayList<>();

    public static final SuggestionProvider<CommandSource> HOME_SUGGEST = (context, builder) -> {
        ServerPlayerEntity player = context.getSource().asPlayer();
        return ISuggestionProvider.suggest(getPlayerHomes(player).getHomeNames().stream().toArray(String[]::new), builder);
    };

    public static void addPlayerHomes(UUID uuid, PlayerHomes homes) {
        playerHomes.put(uuid, homes);
    }

    public static void cleanData() {
        playerHomes.clear();
        playerLastLocation.clear();
        teleportRequests.clear();
    }

    public static PlayerHomes getPlayerHomes(ServerPlayerEntity player) {
        UUID playerUUID = player.getUniqueID();
        if (playerHomes.containsKey(playerUUID)) {
            return playerHomes.get(playerUUID);
        } else {
            PlayerHomes playerHome = new PlayerHomes();
            playerHomes.put(playerUUID, playerHome);
            return playerHome;
        }
    }

    public static Location getPlayerLastLoc(ServerPlayerEntity player) {
        return playerLastLocation.get(player);
    }

    public static void setPlayerLastLocation(ServerPlayerEntity player) {
        if (playerLastLocation.containsKey(player)){
            playerLastLocation.remove(player);
        }
        playerLastLocation.put(player, new Location(player));
    }

    public static ArrayList<TeleportRequest> getTeleportRequests(ServerPlayerEntity player) {
        ArrayList<TeleportRequest> userTpaRequests = new ArrayList<>();
        for (TeleportRequest tpa : teleportRequests) {
            if (tpa.getTpPlayer() == player || tpa.getTpTargetPlayer() == player) {
                userTpaRequests.add(tpa);
            }
        }
        return userTpaRequests;
    }

    public static TeleportRequest findTeleportRequest(ServerPlayerEntity tpPlayer, ServerPlayerEntity tpToPlayer) {
        for (TeleportRequest tpa : teleportRequests) {
            if (tpa.getCreatorPlayer() != tpPlayer) {
                if ((tpa.getTpPlayer() == tpPlayer && tpa.getTpTargetPlayer() == tpToPlayer) || (tpa.getTpPlayer() == tpToPlayer && tpa.getTpTargetPlayer() == tpPlayer)) {
                    return tpa;
                }
            }
        }
        return null;
    }

    public static TeleportRequest findTeleportRequest(ServerPlayerEntity creatorPlayer, ServerPlayerEntity tpPlayer, ServerPlayerEntity tpTargetPlayer) {
        for (TeleportRequest tpa : teleportRequests) {
            if (tpa.getCreatorPlayer() == creatorPlayer && tpa.getTpPlayer() == tpPlayer && tpa.getTpTargetPlayer() == tpTargetPlayer) {
                return tpa;
            }
        }
        return null;
    }

    public static boolean requestTeleport(ServerPlayerEntity creatorPlayer, ServerPlayerEntity tpPlayer, ServerPlayerEntity tpTargetPlayer) {
        if (findTeleportRequest(creatorPlayer, tpPlayer, tpTargetPlayer) == null) {
            TeleportRequest newTpa = new TeleportRequest(creatorPlayer, tpPlayer, tpTargetPlayer, ConfigValues.tpaRequestTeleportTime, ConfigValues.tpaRequestTimeout);
            teleportRequests.add(newTpa);
            return true;
        } else {
            return false;
        }
    }

    public static void acceptTeleportRequest(TeleportRequest tpa) {
        if (ConfigValues.tpaRequestTeleportTime != 0) {
            tpa.getTpPlayer().sendMessage(new TranslationTextComponent("command.maessentials.tpaccept.wait", ConfigValues.tpaRequestTeleportTime, true));
            tpa.setAcceptLocation(new Location(tpa.getTpPlayer()));
        } else {
            tpa.getTpPlayer().sendMessage(new TranslationTextComponent("command.maessentials.tpaccept.request", tpa.getTpPlayer().getDisplayName(), true));
            tpa.getTpTargetPlayer().sendMessage(new TranslationTextComponent("command.maessentials.tpaccept.target", tpa.getTpTargetPlayer().getDisplayName(), true));
            Teleport.teleportPlayer(tpa.getTpPlayer(), new Location(tpa.getTpTargetPlayer()), true);
            teleportRequests.remove(tpa);
        }
    }

    public static void denyTeleportRequest(TeleportRequest tpa) {
        teleportRequests.remove(tpa);
    }

    private static boolean didPlayerMoved(ServerPlayerEntity player, Location location) {
        if (location.x != player.posX || location.y != player.posY || location.z != player.posZ) {
            return true;
        }
        return false;
    }

    public static void checkTeleportRequests() {
        if (ConfigValues.enableTpa) {
            ArrayList<TeleportRequest> found = new ArrayList<>();
            for (TeleportRequest tpa : teleportRequests) {
                if (tpa.countdwnTimeout() == 0) {
                    tpa.getTpPlayer().sendMessage(new TranslationTextComponent("command.maessentials.tpa.expired.request", tpa.getTpTargetPlayer().getDisplayName(), true));
                    tpa.getTpTargetPlayer().sendMessage(new TranslationTextComponent("command.maessentials.tpa.expired.target", tpa.getTpPlayer().getDisplayName(), true));
                    found.add(tpa);
                } else {
                    if (ConfigValues.tpaRequestTeleportTime != 0) {
                        if (tpa.getAcceptLocation() != null) {
                            if (!didPlayerMoved(tpa.getTpPlayer(), tpa.getAcceptLocation())) {
                                if (tpa.countDownTeleport() == 0) {
                                    tpa.getTpPlayer().sendMessage(new TranslationTextComponent("command.maessentials.tpaccept.request", tpa.getTpPlayer().getDisplayName(), true));
                                    tpa.getTpTargetPlayer().sendMessage(new TranslationTextComponent("command.maessentials.tpaccept.target", tpa.getTpTargetPlayer().getDisplayName(), true));
                                    Teleport.teleportPlayer(tpa.getTpPlayer(), new Location(tpa.getTpTargetPlayer()), true);
                                    found.add(tpa);
                                }
                            } else {
                                tpa.getTpPlayer().sendMessage(new TranslationTextComponent("command.maessentials.tpa.moved.request"));
                                tpa.getTpTargetPlayer().sendMessage(new TranslationTextComponent("command.maessentials.tpa.moved.target", tpa.getTpPlayer().getDisplayName(), true));
                                found.add(tpa);
                            }
                        }
                    }
                }
            }
            teleportRequests.removeAll(found);
        }
    }
}