package com.maciej916.maessentials.commands;

import com.maciej916.maessentials.classes.player.EssentialPlayer;
import com.maciej916.maessentials.classes.player.PlayerRestriction;
import com.maciej916.maessentials.data.DataManager;
import com.maciej916.maessentials.libs.Methods;
import com.maciej916.maessentials.libs.Time;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.maciej916.maessentials.libs.Methods.currentTimestamp;

public class CommandCheck {

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("check").requires((source) -> source.hasPermissionLevel(2))
            .then(Commands.literal("bans").executes((context) -> bansCheck(context.getSource())))
            .then(Commands.literal("mutes").executes((context) -> mutesCheck(context.getSource())))
//            .then(Commands.literal("player").then(Commands.argument("targetPlayer", EntityArgument.players()).executes((cmd) -> playerCheck(cmd.getSource()))))
        );
    }

    private static int bansCheck(CommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.asPlayer();
        HashMap<UUID, EssentialPlayer> players = DataManager.getPlayers();
        ArrayList<PlayerRestriction> restrictions = new ArrayList<>();
        for (Map.Entry<UUID, EssentialPlayer> entry : players.entrySet()) {
            EssentialPlayer eslPlayer = entry.getValue();
            PlayerRestriction restriction = eslPlayer.getRestrictions().getBan();
            if (eslPlayer.getRestrictions().isRestricted(restriction)) {
                restrictions.add(restriction);
            }
        }
        if (restrictions.size() > 0) {
            player.sendMessage(Methods.formatText("check.maessentials.player.banned.list"));
            for (PlayerRestriction restriction : restrictions) {
                if (restriction.getTime() == -1) {
                    player.sendMessage(Methods.formatText("check.maessentials.player.banned.perm", player.getDisplayName(), restriction.getReason()));
                } else {
                    String displayTime = Time.formatDate(restriction.getTime() - currentTimestamp());
                    player.sendMessage(Methods.formatText("check.maessentials.player.banned", player.getDisplayName(), displayTime, restriction.getReason()));
                }
            }
        } else {
            player.sendMessage(Methods.formatText("check.maessentials.player.banned.empty"));
        }
        return Command.SINGLE_SUCCESS;
    }

    private static int mutesCheck(CommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.asPlayer();
        HashMap<UUID, EssentialPlayer> players = DataManager.getPlayers();
        ArrayList<PlayerRestriction> restrictions = new ArrayList<>();
        for (Map.Entry<UUID, EssentialPlayer> entry : players.entrySet()) {
            EssentialPlayer eslPlayer = entry.getValue();
            PlayerRestriction restriction = eslPlayer.getRestrictions().getMute();
            if (eslPlayer.getRestrictions().isRestricted(restriction)) {
                restrictions.add(restriction);
            }
        }
        if (restrictions.size() > 0) {
            player.sendMessage(Methods.formatText("check.maessentials.player.muted.list"));
            for (PlayerRestriction restriction : restrictions) {
                if (restriction.getTime() == -1) {
                    player.sendMessage(Methods.formatText("check.maessentials.player.muted.perm", player.getDisplayName(), restriction.getReason()));
                } else {
                    String displayTime = Time.formatDate(restriction.getTime() - currentTimestamp());
                    player.sendMessage(Methods.formatText("check.maessentials.player.muted", player.getDisplayName(), displayTime, restriction.getReason()));
                }
            }
        } else {
            player.sendMessage(Methods.formatText("check.maessentials.player.muted.empty"));
        }
        return Command.SINGLE_SUCCESS;
    }

}
