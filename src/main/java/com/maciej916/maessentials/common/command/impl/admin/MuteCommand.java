package com.maciej916.maessentials.common.command.impl.admin;

import com.maciej916.maessentials.common.command.BaseCommand;
import com.maciej916.maessentials.common.data.DataManager;
import com.maciej916.maessentials.common.lib.player.EssentialPlayer;
import com.maciej916.maessentials.common.lib.player.PlayerRestriction;
import com.maciej916.maessentials.common.util.TimeUtils;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.command.arguments.MessageArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.ITextComponent;

import static com.maciej916.maessentials.common.util.TimeUtils.currentTimestamp;

public class MuteCommand extends BaseCommand {

    public MuteCommand(String command, int permissionLevel, boolean enabled) {
        super(command, permissionLevel, enabled);
    }

    @Override
    public LiteralArgumentBuilder<CommandSource> setExecution() {
        return builder
                .then(Commands.argument("targetPlayer", EntityArgument.players()).executes(context -> execute(context.getSource(), EntityArgument.getPlayer(context, "targetPlayer")))
                        .then(Commands.argument("time", StringArgumentType.word()).executes(context -> execute(context.getSource(), EntityArgument.getPlayer(context, "targetPlayer"), StringArgumentType.getString(context, "time")))
                                .then(Commands.argument("reason", MessageArgument.message()).executes(context -> execute(context.getSource(), EntityArgument.getPlayer(context, "targetPlayer"), StringArgumentType.getString(context, "time"), MessageArgument.getMessage(context, "reason"))))));
    }

    private int execute(CommandSource source, ServerPlayerEntity targetPlayer) throws CommandSyntaxException {
        ServerPlayerEntity player = source.asPlayer();
        sendMessage(player, "mute.maessentials.provide.time");
        return Command.SINGLE_SUCCESS;
    }

    private int execute(CommandSource source, ServerPlayerEntity targetPlayer, String time) throws CommandSyntaxException {
        ServerPlayerEntity player = source.asPlayer();
        doMute(player, targetPlayer, time, "No reason provided");
        return Command.SINGLE_SUCCESS;
    }


    private int execute(CommandSource source, ServerPlayerEntity targetPlayer, String time, ITextComponent reason) throws CommandSyntaxException {
        ServerPlayerEntity player = source.asPlayer();
        doMute(player, targetPlayer, time, reason.getString());
        return Command.SINGLE_SUCCESS;
    }

    private void doMute(ServerPlayerEntity player, ServerPlayerEntity targetPlayer, String time, String reason) {
        EssentialPlayer eslTargetPlayer = DataManager.getPlayer(targetPlayer);
        PlayerRestriction mute = eslTargetPlayer.getRestrictions().getMute();

        long mutetime = TimeUtils.parseDate(time, true);
        if (time.equals("perm")) {
            mutetime = -1;
        }

        if (mutetime == 0) {
            sendMessage(player, "maessentials.illegal_date");
            return;
        }

        if (mute != null && (currentTimestamp() < mute.getTime() ||  mute.getTime() == -1)) {
            sendMessage(player, "mute.maessentials.already_muted", targetPlayer.getDisplayName());
            return;
        }

        eslTargetPlayer.getRestrictions().setMute(mutetime, reason);
        eslTargetPlayer.saveData();

        if (mutetime == -1) {
            sendGlobalMessage(player.server.getPlayerList(), "mute.maessentials.success.perm", targetPlayer.getDisplayName(), player.getDisplayName(), reason);
            sendMessage(player, "mute.maessentials.success.perm.target");
        } else {
            String displayTime = TimeUtils.formatDate(mutetime - currentTimestamp());
            sendGlobalMessage(player.server.getPlayerList(), "mute.maessentials.success", targetPlayer.getDisplayName(), player.getDisplayName(), displayTime, reason);
            sendMessage(player, "mute.maessentials.success.target", displayTime);
        }

        sendMessage(targetPlayer, "mute.maessentials.success.target.reason", reason);
    }

}
