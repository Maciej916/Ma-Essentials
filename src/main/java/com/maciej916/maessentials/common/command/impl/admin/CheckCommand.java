package com.maciej916.maessentials.common.command.impl.admin;

import com.maciej916.maessentials.common.command.BaseCommand;
import com.maciej916.maessentials.common.data.DataManager;
import com.maciej916.maessentials.common.lib.player.EssentialPlayer;
import com.maciej916.maessentials.common.lib.player.PlayerRestriction;
import com.maciej916.maessentials.common.util.TimeUtils;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.maciej916.maessentials.common.util.TimeUtils.currentTimestamp;

public class CheckCommand extends BaseCommand {

    public CheckCommand(String command, int permissionLevel, boolean enabled) {
        super(command, permissionLevel, enabled);
    }

    @Override
    public LiteralArgumentBuilder<CommandSource> setExecution() {
        return builder
                .then(Commands.literal("bans").executes((context) -> bansCheck(context.getSource())))
                .then(Commands.literal("mutes").executes((context) -> mutesCheck(context.getSource())));
    }

    private int bansCheck(CommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.asPlayer();
        HashMap<UUID, EssentialPlayer> players = DataManager.getPlayers();
        ArrayList<PlayerRestriction> restrictions = new ArrayList<>();
        for (Map.Entry<UUID, EssentialPlayer> entry : players.entrySet()) {
            EssentialPlayer eslPlayer = entry.getValue();
            PlayerRestriction restriction = eslPlayer.getRestrictions().getBan();
            if (eslPlayer.getRestrictions().isRestricted(restriction)) {
                String display = eslPlayer.getUsername() == null ? "-" : eslPlayer.getUsername();
                restrictions.add(new PlayerRestriction(display, eslPlayer.getPlayerUUID(), restriction));
            }
        }
        if (restrictions.size() > 0) {
            sendMessage(player, "check.maessentials.player.banned.list");

            for (PlayerRestriction restriction : restrictions) {
                if (restriction.getTime() == -1) {
                    sendMessage(player, "check.maessentials.player.banned.perm", restriction.getUsername(), restriction.getPlayerUUID(), restriction.getReason());
                } else {
                    String displayTime = TimeUtils.formatDate(restriction.getTime() - currentTimestamp());
                    sendMessage(player, "check.maessentials.player.banned", restriction.getUsername(), restriction.getPlayerUUID(), displayTime, restriction.getReason());
                }
            }
        } else {
            sendMessage(player, "check.maessentials.player.banned.empty");
        }
        return Command.SINGLE_SUCCESS;
    }

    private int mutesCheck(CommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.asPlayer();
        HashMap<UUID, EssentialPlayer> players = DataManager.getPlayers();
        ArrayList<PlayerRestriction> restrictions = new ArrayList<>();
        for (Map.Entry<UUID, EssentialPlayer> entry : players.entrySet()) {
            EssentialPlayer eslPlayer = entry.getValue();
            PlayerRestriction restriction = eslPlayer.getRestrictions().getMute();
            if (eslPlayer.getRestrictions().isRestricted(restriction)) {
                String display = eslPlayer.getUsername() == null ? "-" : eslPlayer.getUsername();
                restrictions.add(new PlayerRestriction(display, eslPlayer.getPlayerUUID(), restriction));
            }
        }
        if (restrictions.size() > 0) {
            sendMessage(player, "check.maessentials.player.muted.list");
            for (PlayerRestriction restriction : restrictions) {
                if (restriction.getTime() == -1) {
                    sendMessage(player, "check.maessentials.player.muted.perm", restriction.getUsername(), restriction.getPlayerUUID(), restriction.getReason());

                } else {
                    String displayTime = TimeUtils.formatDate(restriction.getTime() - currentTimestamp());
                    sendMessage(player, "check.maessentials.player.muted", restriction.getUsername(), restriction.getPlayerUUID(), displayTime, restriction.getReason());
                }
            }
        } else {
            sendMessage(player, "check.maessentials.player.muted.empty");
        }
        return Command.SINGLE_SUCCESS;
    }

}
