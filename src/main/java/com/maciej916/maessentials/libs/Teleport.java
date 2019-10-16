package com.maciej916.maessentials.libs;

import com.maciej916.maessentials.classes.Location;
import com.maciej916.maessentials.config.ConfigValues;
import com.maciej916.maessentials.data.DataManager;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;

import java.util.*;

import static com.maciej916.maessentials.libs.Methods.isDev;

public class Teleport {
    private static ArrayList<Teleport> activeTeleports = new ArrayList<>();

    private ServerPlayerEntity creatorPlayer;
    private ServerPlayerEntity tpPlayer;
    private ServerPlayerEntity tpTargetPlayer;
    private Location loc;
    private Location dest;
    private long time;
    private long timeout;
    private boolean exact;
    private boolean accepted;

    private Teleport(ServerPlayerEntity creatorPlayer, Location dest, long time, boolean exact) {
        this.creatorPlayer = creatorPlayer;
        this.loc = new Location(creatorPlayer);
        this.dest = dest;
        this.time = time;
        this.exact = exact;
    }

    private Teleport(ServerPlayerEntity creatorPlayer, ServerPlayerEntity tpPlayer, ServerPlayerEntity tpTargetPlayer, long timeout, boolean exact) {
        this.creatorPlayer = creatorPlayer;
        this.tpPlayer = tpPlayer;
        this.tpTargetPlayer = tpTargetPlayer;
        this.exact = exact;
        this.timeout = timeout;
        this.accepted = false;
    }

    public static void teleportPlayer(ServerPlayerEntity player, Location loc, boolean exact) {
        if (ConfigValues.teleportTime == 0) {
            player.sendMessage(Methods.formatText("teleport.maessentials.teleported", TextFormatting.WHITE));
            doTeleport(player, loc, exact);
        } else {
            long currentTime = System.currentTimeMillis() / 1000;
            Teleport teleport = new Teleport(player, loc, currentTime + ConfigValues.teleportTime, exact);
            activeTeleports.add(teleport);
        }
    }

    public static void teleportRequest(ServerPlayerEntity creatorPlayer, ServerPlayerEntity tpPlayer, ServerPlayerEntity tpTargetPlayer, boolean exact) {
        if (ConfigValues.teleportTime == 0) {
            tpPlayer.sendMessage(Methods.formatText("teleport.maessentials.tpaccept.request", TextFormatting.WHITE, tpTargetPlayer.getDisplayName()));
            tpTargetPlayer.sendMessage(Methods.formatText("teleport.maessentials.tpaccept.target", TextFormatting.WHITE, tpPlayer.getDisplayName()));
            doTeleport(tpPlayer, new Location(tpTargetPlayer), true);
        } else {
            long currentTime = System.currentTimeMillis() / 1000;
            Teleport teleport = new Teleport(creatorPlayer, tpPlayer, tpTargetPlayer , currentTime + ConfigValues.teleportRequestTimeout, exact);
            activeTeleports.add(teleport);
        }
    }

    public static ArrayList<Teleport> getPlayerTeleports(ServerPlayerEntity player) {
        ArrayList<Teleport> foundTp = new ArrayList<>();
        for (Teleport tp : activeTeleports) {
            if (tp.tpPlayer != null) {
                if (tp.tpPlayer == player || tp.tpTargetPlayer == player) {
                    foundTp.add(tp);
                }
            } else {
                if (tp.creatorPlayer == player) {
                    foundTp.add(tp);
                }
            }
        }
        return foundTp;
    }

    public static Teleport findTeleportRequest(ServerPlayerEntity creatorPlayer, ServerPlayerEntity tpPlayer, ServerPlayerEntity tpTargetPlayer) {
        for (Teleport tp : activeTeleports) {
            if (tp.tpPlayer != null) {
                if (tp.creatorPlayer == creatorPlayer && tp.tpPlayer == tpPlayer && tp.tpTargetPlayer == tpTargetPlayer) {
                    return tp;
                }
            }
        }
        return null;
    }

    public static Teleport findTeleportRequest(ServerPlayerEntity tpPlayer, ServerPlayerEntity tpTargetPlayer) {
        for (Teleport tp : activeTeleports) {
            if (tp.tpPlayer != null) {
                if (tp.creatorPlayer == tpTargetPlayer && (tp.tpPlayer == tpPlayer && tp.tpTargetPlayer == tpTargetPlayer) || (tp.tpPlayer == tpTargetPlayer && tp.tpTargetPlayer == tpPlayer)) {
                    return tp;
                }
            }
        }
        return null;
    }

    public static void acceptTeleport(Teleport tpr) {
        long currentTime = System.currentTimeMillis() / 1000;
        tpr.tpPlayer.sendMessage(Methods.formatText("teleport.maessentials.tpaccept.request.wait", TextFormatting.WHITE, ConfigValues.teleportTime));
        tpr.tpTargetPlayer.sendMessage(Methods.formatText("teleport.maessentials.tpaccept.target.wait", TextFormatting.WHITE, tpr.tpPlayer.getDisplayName()));
        tpr.loc = new Location(tpr.tpPlayer);
        tpr.time = currentTime + ConfigValues.teleportTime;
        tpr.accepted = true;
    }

    public static void declineTrade(Teleport tpr) {
        tpr.tpPlayer.sendMessage(Methods.formatText("command.maessentials.tpr.tpdeny.request", TextFormatting.WHITE, tpr.tpTargetPlayer.getDisplayName()));
        tpr.tpTargetPlayer.sendMessage(Methods.formatText("command.maessentials.tpr.tpdeny.target", TextFormatting.WHITE, tpr.tpPlayer.getDisplayName()));
        activeTeleports.remove(tpr);
    }

    public static void checkTeleports() {
        ArrayList<Teleport> delTp = new ArrayList<>();
        for (Teleport tp : activeTeleports) {
            long currentTime = System.currentTimeMillis() / 1000;
            if (tp.tpPlayer == null) {
                Location playerLocation = new Location(tp.creatorPlayer);
                if (Methods.isLocationSame(playerLocation, tp.loc)) {
                    if (tp.time <= currentTime) {
                        tp.creatorPlayer.sendMessage(Methods.formatText("teleport.maessentials.teleported", TextFormatting.WHITE));
                        doTeleport(tp.creatorPlayer, tp.dest, tp.exact);
                        delTp.add(tp);
                    }
                } else {
                    tp.creatorPlayer.sendMessage(Methods.formatText("teleport.maessentials.moved", TextFormatting.WHITE));
                    delTp.add(tp);
                }
            } else {
                if (ConfigValues.enableTpa || isDev()) {
                    if (tp.loc != null) {
                        Location playerLocation = new Location(tp.tpPlayer);
                        if (Methods.isLocationSame(playerLocation, tp.loc)) {
                            if (tp.timeout <= currentTime) {
                                tp.tpPlayer.sendMessage(Methods.formatText("teleport.maessentials.tpr.expired.target", TextFormatting.WHITE, tp.tpTargetPlayer.getDisplayName()));
                                tp.tpTargetPlayer.sendMessage(Methods.formatText("teleport.maessentials.tpr.expired.request", TextFormatting.WHITE, tp.tpPlayer.getDisplayName()));
                                delTp.add(tp);
                            } else {
                                if (tp.time <= currentTime && tp.accepted) {
                                    tp.tpPlayer.sendMessage(Methods.formatText("teleport.maessentials.tpaccept.request", TextFormatting.WHITE, tp.tpTargetPlayer.getDisplayName()));
                                    tp.tpTargetPlayer.sendMessage(Methods.formatText("teleport.maessentials.tpaccept.target", TextFormatting.WHITE, tp.tpPlayer.getDisplayName()));
                                    doTeleport(tp.tpPlayer, new Location(tp.tpTargetPlayer), tp.exact);
                                    delTp.add(tp);
                                }
                            }
                        } else {
                            tp.tpPlayer.sendMessage(Methods.formatText("teleport.maessentials.tpr.moved.request", TextFormatting.WHITE));
                            tp.tpTargetPlayer.sendMessage(Methods.formatText("teleport.maessentials.tpr.moved.target", TextFormatting.WHITE, tp.tpPlayer.getDisplayName()));
                            delTp.add(tp);
                        }
                    }
                }
            }
        }
        activeTeleports.removeAll(delTp);
    }

    private static void doTeleport(ServerPlayerEntity player, Location loc, boolean exact) {
        UUID playerUUID = player.getUniqueID();
        Location currentLocation = new Location(player);
        DataManager.getPlayerData(player).setLastLocation(currentLocation);

        long currentTime = System.currentTimeMillis() / 1000;
        DataManager.getPlayerData(player).setLastTeleportTime(currentTime);

        DataManager.savePlayerData(playerUUID, DataManager.getPlayerData(playerUUID));

        if (player.dimension == DimensionType.THE_END && loc.getDimension() == DimensionType.OVERWORLD) {
            player.queuedEndExit = true;
        }

        ServerWorld worldDest = player.server.getWorld(loc.getDimension());
        if (exact) {
            player.teleport(worldDest, loc.x, loc.y, loc.z, loc.rotationYaw, loc.rotationPitch);
        } else {
            player.teleport(worldDest, loc.x + 0.5, loc.y + 0.5, loc.z, player.rotationYaw, player.rotationPitch);
        }
    }
}