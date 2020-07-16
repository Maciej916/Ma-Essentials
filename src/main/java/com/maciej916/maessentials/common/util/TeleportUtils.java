package com.maciej916.maessentials.common.util;

import com.maciej916.maessentials.common.enums.EnumColor;
import com.maciej916.maessentials.common.enums.EnumLang;
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
            TextUtils.sendChatMessage(player, EnumLang.TELEPORT_ACTIVE.translateColored(EnumColor.RED));
            return false;
        }

        if (delay == 0) {
            TextUtils.sendChatMessage(player, EnumLang.TELEPORT_DONE.translateColored(EnumColor.DARK_GREEN));
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
            TextUtils.sendChatMessage(player, EnumLang.TELEPORT_ACTIVE.translateColored(EnumColor.RED));
            return false;
        }

        TeleportRequest existTpR = TeleportUtils.findRequest(creator, player, target);
        if (existTpR != null) {
            TextUtils.sendChatMessage(player, EnumLang.TPA_EXIST.translateColored(EnumColor.RED, EnumLang.GENERIC.translateColored(EnumColor.DARK_RED, target.getDisplayName())));
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
            TextUtils.sendChatMessage(request.getPlayer(), EnumLang.TPACCEPT_REQUEST.translate(EnumLang.GENERIC.translateColored(EnumColor.AQUA, request.getTargetName())));
            TextUtils.sendChatMessage(request.getTarget(), EnumLang.TPACCEPT_TARGET.translate(EnumLang.GENERIC.translateColored(EnumColor.AQUA, request.getPlayerName())));

            doTeleport(request.getPlayer(), request.getDestination(), true, true);
            teleportRequests.remove(request);
        } else {
            TextUtils.sendChatMessage(request.getPlayer(), EnumLang.TPACCEPT_REQUEST_WAIT.translate(EnumLang.GENERIC.translateColored(EnumColor.RED, ModConfig.tpa_delay)));
            TextUtils.sendChatMessage(request.getTarget(), EnumLang.TPACCEPT_TARGET_WAIT.translate(EnumLang.GENERIC.translateColored(EnumColor.AQUA, request.getPlayerName())));

            EssentialPlayer eslPlayer = DataManager.getPlayer(request.getPlayer());
            eslPlayer.getTemp().setTeleportActive(new Location(request.getPlayer()));
            request.setAccepted();
        }
    }

    public static void declineRequest(TeleportRequest request) {
        TextUtils.sendChatMessage(request.getPlayer(), EnumLang.TPDENY_REQUEST.translate(EnumLang.GENERIC.translateColored(EnumColor.AQUA, request.getTargetName())));
        TextUtils.sendChatMessage(request.getTarget(), EnumLang.TPDENY_TARGET.translate(EnumLang.GENERIC.translateColored(EnumColor.AQUA, request.getPlayerName())));
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
                        TextUtils.sendChatMessage(player, EnumLang.TELEPORT_DONE.translateColored(EnumColor.DARK_GREEN));
                        TextUtils.sendActionMessage(player, EnumLang.TELEPORT_DONE.translateColored(EnumColor.DARK_GREEN));

                        eslPlayer.getTemp().setTeleportNotActive();
                        eslPlayer.getUsage().setTeleportUsage(tp.getType());
                        eslPlayer.saveData();
                        doTeleport(player, tp.getDestination(), true, true);
                        del.add(tp);
                    } else {
//                        player.getServerWorld().playSound(null, new BlockPos(player.getPositionVec()), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 0.5F, 1.0F);
                        TextUtils.sendActionMessage(player, EnumLang.TELEPORT_IN.translateColored(EnumColor.YELLOW, tp.getTeleportTime() - currentTimestamp()));
                    }
                } else {
                    TextUtils.sendChatMessage(player, EnumLang.TELEPORT_MOVED.translateColored(EnumColor.RED));
                    TextUtils.sendActionMessage(player, EnumLang.TELEPORT_MOVED.translateColored(EnumColor.RED));

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
                            TextUtils.sendChatMessage(tp.getPlayer(), EnumLang.TPACCEPT_REQUEST.translate(EnumLang.GENERIC.translateColored(EnumColor.AQUA, tp.getTargetName())));
                            TextUtils.sendChatMessage(tp.getTarget(), EnumLang.TPACCEPT_TARGET.translate(EnumLang.GENERIC.translateColored(EnumColor.AQUA, tp.getPlayerName())));

                            eslPlayer.getTemp().setTeleportNotActive();
                            eslPlayer.getUsage().setTeleportUsage("tpa");
                            eslPlayer.saveData();
                            doTeleport(tp.getPlayer(), new Location(tp.getTarget()), true, true);
                            del.add(tp);
                        }
                    } else {
                        TextUtils.sendChatMessage(tp.getPlayer(), EnumLang.TPA_MOVED_TARGET.translateColored(EnumColor.RED));
                        TextUtils.sendChatMessage(tp.getTarget(), EnumLang.TPA_MOVED_REQUEST.translateColored(EnumColor.RED, EnumLang.GENERIC.translateColored(EnumColor.DARK_RED, tp.getPlayerName())));
                        eslPlayer.getTemp().setTeleportNotActive();
                        del.add(tp);
                    }
                } else {
                    if (currentTimestamp() >= tp.getTimeout()) {
                        TextUtils.sendChatMessage(tp.getPlayer(), EnumLang.TPA_EXPIRED_TARGET.translate(EnumLang.GENERIC.translateColored(EnumColor.AQUA, tp.getTargetName())));
                        TextUtils.sendChatMessage(tp.getTarget(), EnumLang.TPA_EXPIRED_REQUEST.translate(EnumLang.GENERIC.translateColored(EnumColor.AQUA, tp.getPlayerName())));
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
                TextUtils.sendChatMessage(player, EnumLang.TELEPORT_INVALID.translateColored(EnumColor.DARK_RED));
            }

            if (exact) {
                player.teleport(worldDest, loc.x, loc.y, loc.z, loc.rotationYaw, loc.rotationPitch);
            } else {
                player.teleport(worldDest, loc.x + 0.5, loc.y + 0.5, loc.z, player.rotationYaw, player.rotationPitch);
            }
        } catch (Exception e) {
            LogUtils.err("Failed to do teleport for player " + player.getDisplayName().getString() + ". Error: " + e.getMessage());
            TextUtils.sendChatMessage(player, EnumLang.TELEPORT_FAILED.translateColored(EnumColor.DARK_RED));
        }
    }
}