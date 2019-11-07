package com.maciej916.maessentials.libs;

import com.maciej916.maessentials.classes.Location;
import com.maciej916.maessentials.classes.player.EssentialPlayer;
import com.maciej916.maessentials.classes.teleport.TeleportRequest;
import com.maciej916.maessentials.classes.teleport.TeleportSimple;
import com.maciej916.maessentials.config.ConfigValues;
import com.maciej916.maessentials.data.DataManager;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.server.ServerWorld;

import java.util.*;

import static com.maciej916.maessentials.libs.Methods.isDev;

public class Teleport {

    private static ArrayList<TeleportSimple> simple_teleports = new ArrayList<>();
    private static ArrayList<TeleportRequest> request_teleports = new ArrayList<>();

    public static void doSimpleTeleport(TeleportSimple simple) {
        if (simple.getDelay() == 0) {
            simple.getPlayer().sendMessage(Methods.formatText("teleport.maessentials.teleported"));
            doTeleport(simple.getPlayer(), simple.getDestination(), true, true);
        } else {

            // TODO
            // Set player location

            simple_teleports.add(simple);
        }
    }

    public static void doRequetTeleport(TeleportRequest request) {
        if (request.getDelay() == 0) {
            request.getPlayer().sendMessage(Methods.formatText("teleport.maessentials.tpaccept.request", request.getTargetName()));
            request.getTarget().sendMessage(Methods.formatText("teleport.maessentials.tpaccept.target", request.getPlayerName()));
            doTeleport(request.getPlayer(), request.getDestination(), true, true);
        } else {
            request_teleports.add(request);
        }
    }

    public static void acceptRequest(TeleportRequest request) {
        request.getPlayer().sendMessage(Methods.formatText("teleport.maessentials.tpaccept.request.wait", ConfigValues.tpa_delay));
        request.getTarget().sendMessage(Methods.formatText("teleport.maessentials.tpaccept.target.wait", request.getPlayerName()));
        request.setAccepted();
    }

    public static void declineRequest(TeleportRequest request) {
        request.getPlayer().sendMessage(Methods.formatText("teleport.maessentials.tpdeny.request", request.getTargetName()));
        request.getTarget().sendMessage(Methods.formatText("teleport.maessentials.tpdeny.target", request.getPlayerName()));
        request_teleports.remove(request);
    }

    public static void checkTeleports(String xxx) {
        checkSimple();
        checkRequest();
    }

    public static void checkSimple() {
        // TODO
    }

    public static void checkRequest() {
        // TODO
    }

    public static void doTeleport(ServerPlayerEntity player, Location loc, boolean exact, boolean saveLastLocation) {
        if (saveLastLocation) {
            EssentialPlayer eslPlayer = DataManager.getPlayer(player);
            eslPlayer.getData().setLastLocation(new Location(player));
            eslPlayer.saveData();
        }

        ServerWorld worldDest = player.server.getWorld(loc.getDimension());
        if (exact) {
            player.teleport(worldDest, loc.x, loc.y, loc.z, loc.rotationYaw, loc.rotationPitch);
        } else {
            player.teleport(worldDest, loc.x + 0.5, loc.y + 0.5, loc.z, player.rotationYaw, player.rotationPitch);
        }
    }







    // TO REMOVE

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

    public static void teleportPlayer(ServerPlayerEntity player, Location loc, boolean exact, int delay) {
        if (delay == 0) {
            player.sendMessage(Methods.formatText("teleport.maessentials.teleported"));
            doTeleport(player, loc, exact, true);
        } else {
            long currentTime = System.currentTimeMillis() / 1000;
            Teleport teleport = new Teleport(player, loc, currentTime + delay, exact);
            activeTeleports.add(teleport);
        }
    }

    public static void teleportRequest(ServerPlayerEntity creatorPlayer, ServerPlayerEntity tpPlayer, ServerPlayerEntity tpTargetPlayer, boolean exact) {
        if (ConfigValues.tpa_delay == 0) {
            tpPlayer.sendMessage(Methods.formatText("teleport.maessentials.tpaccept.request", tpTargetPlayer.getDisplayName()));
            tpTargetPlayer.sendMessage(Methods.formatText("teleport.maessentials.tpaccept.target", tpPlayer.getDisplayName()));
            doTeleport(tpPlayer, new Location(tpTargetPlayer), true, true);
        } else {
            long currentTime = System.currentTimeMillis() / 1000;
            Teleport teleport = new Teleport(creatorPlayer, tpPlayer, tpTargetPlayer , currentTime + ConfigValues.tpa_timeout, exact);
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
        tpr.tpPlayer.sendMessage(Methods.formatText("teleport.maessentials.tpaccept.request.wait", ConfigValues.tpa_delay));
        tpr.tpTargetPlayer.sendMessage(Methods.formatText("teleport.maessentials.tpaccept.target.wait", tpr.tpPlayer.getDisplayName()));
        tpr.loc = new Location(tpr.tpPlayer);
        tpr.time = currentTime + ConfigValues.tpa_delay;
        tpr.accepted = true;
    }

    public static void declineTrade(Teleport tpr) {
        tpr.tpPlayer.sendMessage(Methods.formatText("teleport.maessentials.tpdeny.request", tpr.tpTargetPlayer.getDisplayName()));
        tpr.tpTargetPlayer.sendMessage(Methods.formatText("teleport.maessentials.tpdeny.target", tpr.tpPlayer.getDisplayName()));
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
                        tp.creatorPlayer.sendMessage(Methods.formatText("teleport.maessentials.teleported"));
                        doTeleport(tp.creatorPlayer, tp.dest, tp.exact, true);
                        delTp.add(tp);
                    }
                } else {
                    tp.creatorPlayer.sendMessage(Methods.formatText("teleport.maessentials.moved"));
                    delTp.add(tp);
                }
            } else {
                if (ConfigValues.tpa_enable || isDev()) {
                    if (tp.loc != null) {
                        Location playerLocation = new Location(tp.tpPlayer);
                        if (Methods.isLocationSame(playerLocation, tp.loc)) {
                            if (tp.timeout <= currentTime && !tp.accepted) {
                                tp.tpPlayer.sendMessage(Methods.formatText("teleport.maessentials.expired.target", tp.tpTargetPlayer.getDisplayName()));
                                tp.tpTargetPlayer.sendMessage(Methods.formatText("teleport.maessentials.expired.request", tp.tpPlayer.getDisplayName()));
                                delTp.add(tp);
                            } else {
                                if (tp.time <= currentTime && tp.accepted) {
                                    tp.tpPlayer.sendMessage(Methods.formatText("teleport.maessentials.tpaccept.request", tp.tpTargetPlayer.getDisplayName()));
                                    tp.tpTargetPlayer.sendMessage(Methods.formatText("teleport.maessentials.tpaccept.target", tp.tpPlayer.getDisplayName()));
                                    doTeleport(tp.tpPlayer, new Location(tp.tpTargetPlayer), tp.exact, true);
                                    delTp.add(tp);
                                }
                            }
                        } else {
                            tp.tpPlayer.sendMessage(Methods.formatText("teleport.maessentials.moved.request"));
                            tp.tpTargetPlayer.sendMessage(Methods.formatText("teleport.maessentials.moved.target", tp.tpPlayer.getDisplayName()));
                            delTp.add(tp);
                        }
                    }
                }
            }
        }
        activeTeleports.removeAll(delTp);
    }

}