package com.maciej916.maessentials.common.util;

import com.maciej916.maessentials.common.lib.Location;
import com.maciej916.maessentials.common.lib.player.EssentialPlayer;
import com.maciej916.maessentials.common.lib.teleport.TeleportRequest;
import com.maciej916.maessentials.common.lib.teleport.TeleportSimple;
import com.maciej916.maessentials.common.config.ModConfig;
import com.maciej916.maessentials.common.data.DataManager;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.server.ServerWorld;

import java.util.ArrayList;

import static com.maciej916.maessentials.common.util.LocationUtils.checkLocation;
import static com.maciej916.maessentials.common.util.ModUtils.*;
import static com.maciej916.maessentials.common.util.TimeUtils.currentTimestamp;

public final class TeleportUtils {

    private static final ArrayList<TeleportSimple> teleportSimple = new ArrayList<>();
    private static final ArrayList<TeleportRequest> teleportRequests = new ArrayList<>();

    public static boolean simpleTeleport(ServerPlayerEntity player, Location location, String teleport, long delay) {
        EssentialPlayer eslPlayer = DataManager.getPlayer(player);

        if (eslPlayer.getTemp().isTeleportActive()) {
            TextUtils.sendChatMessage(player, "teleport.maessentials.active");
            return false;
        }

        if (delay == 0) {
            TextUtils.sendChatMessage(player, "teleport.maessentials.teleported");
            doTeleport(player, location, true, true);
            return true;
        }

        eslPlayer.getTemp().setTeleportActive(new Location(player));
        TeleportSimple tpS = new TeleportSimple(player, location, teleport, delay);
        doSimpleTeleport(tpS);
        return true;
    }

    public static boolean requestTeleport(ServerPlayerEntity creator, ServerPlayerEntity player, ServerPlayerEntity target, long delay) {
        EssentialPlayer eslPlayer = DataManager.getPlayer(player);

        if (eslPlayer.getTemp().isTeleportActive()) {
            TextUtils.sendChatMessage(player, "teleport.maessentials.active");
            return false;
        }

        TeleportRequest existTpR = TeleportUtils.findRequest(creator, player, target);
        if (existTpR != null) {
            TextUtils.sendChatMessage(player, "tpa.maessentials.exist", target.getDisplayName());
            return false;
        }

        TeleportRequest tpR = new TeleportRequest(creator, player, target, delay);
        doRequetTeleport(tpR);
        return true;
    }

    public static void doSimpleTeleport(TeleportSimple simple) {
        teleportSimple.add(simple);
    }

    public static void doRequetTeleport(TeleportRequest request) {
        teleportRequests.add(request);
    }

    public static TeleportRequest findRequest(ServerPlayerEntity creator, ServerPlayerEntity player, ServerPlayerEntity target) {
        for (TeleportRequest tp : teleportRequests) {
            if (tp.getCreator() == creator && tp.getPlayer() == player && tp.getTarget() == target) {
                return tp;
            }
        }
        return null;
    }

    public static TeleportRequest findRequest(ServerPlayerEntity creator, ServerPlayerEntity target) {
        for (TeleportRequest tp : teleportRequests) {
            if (tp.getCreator() == target && (tp.getPlayer() == creator && tp.getTarget() == target) || (tp.getPlayer() == target && tp.getTarget() == creator)) {
                return tp;
            }
        }
        return null;
    }

    public static ArrayList<TeleportRequest> findRequest(ServerPlayerEntity player) {
        ArrayList<TeleportRequest> found = new ArrayList<>();
        for (TeleportRequest tp : teleportRequests) {
            if (tp.getPlayer() == player || tp.getTarget() == player) {
                found.add(tp);
            }
        }
        return found;
    }

    public static void acceptRequest(TeleportRequest request) {
        if (request.getDelay() == 0) {
            TextUtils.sendChatMessage(request.getPlayer(), "teleport.maessentials.tpaccept.request", request.getTargetName());
            TextUtils.sendChatMessage(request.getTarget(), "teleport.maessentials.tpaccept.target", request.getPlayerName());

            doTeleport(request.getPlayer(), request.getDestination(), true, true);
            teleportRequests.remove(request);
        } else {
            TextUtils.sendChatMessage(request.getPlayer(), "teleport.maessentials.tpaccept.request.wait", ModConfig.tpa_delay);
            TextUtils.sendChatMessage(request.getTarget(), "teleport.maessentials.tpaccept.target.wait", request.getPlayerName());

            EssentialPlayer eslPlayer = DataManager.getPlayer(request.getPlayer());
            eslPlayer.getTemp().setTeleportActive(new Location(request.getPlayer()));
            request.setAccepted();
        }
    }

    public static void declineRequest(TeleportRequest request) {
        TextUtils.sendChatMessage(request.getPlayer(), "teleport.maessentials.tpdeny.request", request.getTargetName());
        TextUtils.sendChatMessage(request.getTarget(), "teleport.maessentials.tpdeny.target", request.getPlayerName());

        teleportRequests.remove(request);
    }

    public static void checkTeleports() {
        checkSimple();

        if (ModConfig.tpa_enable || isDev()) {
            checkRequest();
        }
    }

    private static void checkSimple() {
        try {
            ArrayList<TeleportSimple> del = new ArrayList<>();
            for (TeleportSimple tp : teleportSimple) {
                ServerPlayerEntity player = tp.getPlayer();
                EssentialPlayer eslPlayer = DataManager.getPlayer(player);
                Location playerLocation = new Location(player);
                Location tpLocation = eslPlayer.getTemp().getTeleportLocation();
                if (checkLocation(playerLocation, tpLocation)) {
                    if (currentTimestamp() >= tp.getTeleportTime()) {
                        TextUtils.sendChatMessage(player, "teleport.maessentials.teleported");

                        eslPlayer.getTemp().setTeleportNotActive();
                        eslPlayer.getUsage().setTeleportUsage(tp.getType());
                        eslPlayer.saveData();
                        doTeleport(player, tp.getDestination(), true, true);
                        del.add(tp);
                    }
                } else {
                    TextUtils.sendChatMessage(player, "teleport.maessentials.moved");

                    eslPlayer.getTemp().setTeleportNotActive();
                    del.add(tp);
                }
            }
            teleportSimple.removeAll(del);
        } catch(Exception e) {
            System.out.println("checkSimple error:");
            System.out.println(e);
        }
    }

    private static void checkRequest() {
        try {
            ArrayList<TeleportRequest> del = new ArrayList<>();
            for (TeleportRequest tp : teleportRequests) {
                if (tp.isAccepted()) {
                    ServerPlayerEntity player = tp.getPlayer();

                    if (player == null) continue;

                    EssentialPlayer eslPlayer = DataManager.getPlayer(player);
                    Location playerLocation = new Location(player);
                    Location tpLocation = eslPlayer.getTemp().getTeleportLocation();
                    if (checkLocation(playerLocation, tpLocation)) {
                        if (currentTimestamp() >= tp.getTeleportTime()) {
                            TextUtils.sendChatMessage(tp.getPlayer(), "teleport.maessentials.tpaccept.request", tp.getTargetName());
                            TextUtils.sendChatMessage(tp.getTarget(), "teleport.maessentials.tpaccept.target", tp.getPlayerName());

                            eslPlayer.getTemp().setTeleportNotActive();
                            eslPlayer.getUsage().setTeleportUsage("tpa");
                            eslPlayer.saveData();
                            doTeleport(tp.getPlayer(), new Location(tp.getTarget()), true, true);
                            del.add(tp);
                        }
                    } else {
                        TextUtils.sendChatMessage(tp.getPlayer(), "teleport.maessentials.moved.request");
                        TextUtils.sendChatMessage(tp.getTarget(), "teleport.maessentials.moved.target", tp.getPlayerName());

                        eslPlayer.getTemp().setTeleportNotActive();
                        del.add(tp);
                    }
                } else {
                    if (currentTimestamp() >= tp.getTimeout()) {
                        TextUtils.sendChatMessage(tp.getPlayer(), "teleport.maessentials.expired.target", tp.getTargetName());
                        TextUtils.sendChatMessage(tp.getTarget(), "teleport.maessentials.expired.request", tp.getPlayerName());

                        del.add(tp);
                    }
                }
            }
            teleportRequests.removeAll(del);
        } catch(Exception e) {
            System.out.println("checkRequest error:");
            System.out.println(e);
        }
    }

    public static void doTeleport(ServerPlayerEntity player, Location loc, boolean exact, boolean saveLastLocation) {
        try {
            if (saveLastLocation) {
                EssentialPlayer eslPlayer = DataManager.getPlayer(player);
                eslPlayer.getData().setLastLocation(new Location(player));
                eslPlayer.saveData();
            }

            ServerWorld worldDest = player.server.getWorld(loc.getWorld());
            if (worldDest == null) {
                TextUtils.sendChatMessage(player, "teleport.maessentials.teleport.invalid");
            }

            if (exact) {
                player.teleport(worldDest, loc.x, loc.y, loc.z, loc.rotationYaw, loc.rotationPitch);
            } else {
                player.teleport(worldDest, loc.x + 0.5, loc.y + 0.5, loc.z, player.rotationYaw, player.rotationPitch);
            }
        } catch (Exception e) {
            LogUtils.err("Failed to do teleport for player " + player.getDisplayName().getString() + ". Error: " + e.getMessage());
            TextUtils.sendChatMessage(player, "teleport.maessentials.teleport.failed");
        }
    }
}