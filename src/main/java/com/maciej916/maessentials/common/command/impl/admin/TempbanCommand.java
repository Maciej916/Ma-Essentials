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
import net.minecraft.util.text.TranslationTextComponent;

import static com.maciej916.maessentials.common.util.TimeUtils.currentTimestamp;

public class TempbanCommand extends BaseCommand {

    public TempbanCommand(String command, int permissionLevel, boolean enabled) {
        super(command, permissionLevel, enabled);
    }

    @Override
    public LiteralArgumentBuilder<CommandSource> setExecution() {
        return builder
                .then(Commands.argument("targetPlayer", EntityArgument.players())
                        .executes(context -> execute(context.getSource(), EntityArgument.getPlayer(context, "targetPlayer")))
                        .then(Commands.argument("time", StringArgumentType.word()).executes(context -> execute(context.getSource(), EntityArgument.getPlayer(context, "targetPlayer"), StringArgumentType.getString(context, "time").toLowerCase()))
                                .then(Commands.argument("reason", MessageArgument.message()).executes(context -> execute(context.getSource(), EntityArgument.getPlayer(context, "targetPlayer"), StringArgumentType.getString(context, "time").toLowerCase(), MessageArgument.getMessage(context, "reason"))))));
    }

    private int execute(CommandSource source, ServerPlayerEntity targetPlayer) throws CommandSyntaxException {
        ServerPlayerEntity player = source.asPlayer();
        sendMessage(player, "tempban.maessentials.provide.time");
        return Command.SINGLE_SUCCESS;
    }

    private int execute(CommandSource source, ServerPlayerEntity targetPlayer, String time) throws CommandSyntaxException {
        ServerPlayerEntity player = source.asPlayer();
        doTempban(player, targetPlayer, time, "No reason provided");
        return Command.SINGLE_SUCCESS;
    }

    private int execute(CommandSource source, ServerPlayerEntity targetPlayer, String time, ITextComponent reason) throws CommandSyntaxException {
        ServerPlayerEntity player = source.asPlayer();
        doTempban(player, targetPlayer, time, reason.getString());
        return Command.SINGLE_SUCCESS;
    }

    private void doTempban(ServerPlayerEntity player, ServerPlayerEntity target, String time, String reason) {
        EssentialPlayer eslTargetPlayer = DataManager.getPlayer(target);
        PlayerRestriction ban = eslTargetPlayer.getRestrictions().getBan();

        long banTime = TimeUtils.parseDate(time, true);
        if (time.equals("perm")) {
            banTime = -1;
        }

        if (banTime == 0) {
            sendMessage(player, "maessentials.illegal_date");
            return;
        }

        if (ban != null && (currentTimestamp() < ban.getTime() ||  ban.getTime() == -1)) {
            sendMessage(player, "tempban.maessentials.already_banned", target.getDisplayName());
            return;
        }

        eslTargetPlayer.getRestrictions().setBan(banTime, reason);
        eslTargetPlayer.saveData();

        if (banTime == -1) {
            sendGlobalMessage(player.server.getPlayerList(), "tempban.maessentials.success.perm", target.getDisplayName(), player.getDisplayName(), reason);
            target.connection.disconnect(new TranslationTextComponent("tempban.maessentials.success.perm.target", player.getDisplayName(), reason));
        } else {
            String displayTime = TimeUtils.formatDate(banTime - currentTimestamp());
            sendGlobalMessage(player.server.getPlayerList(), "tempban.maessentials.success", target.getDisplayName(), player.getDisplayName(), displayTime, reason);
            target.connection.disconnect(new TranslationTextComponent("tempban.maessentials.success.target", player.getDisplayName(), displayTime, reason));
        }
    }

}
