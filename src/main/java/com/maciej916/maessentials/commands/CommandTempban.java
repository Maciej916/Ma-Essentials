package com.maciej916.maessentials.commands;

import com.maciej916.maessentials.classes.player.EssentialPlayer;
import com.maciej916.maessentials.classes.player.PlayerRestriction;
import com.maciej916.maessentials.data.DataManager;
import com.maciej916.maessentials.libs.Methods;
import com.maciej916.maessentials.libs.Time;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.command.arguments.MessageArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import static com.maciej916.maessentials.libs.Methods.currentTimestamp;

public class CommandTempban {

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder<CommandSource> builder = Commands.literal("tempban").requires(source -> source.hasPermissionLevel(2));
        builder
                .executes(context -> tempban(context))
                        .then(Commands.argument("targetPlayer", EntityArgument.players())
                        .executes(context -> tempbanPlayer(context))
                                .then(Commands.argument("time", StringArgumentType.word())
                                .executes(context -> tempbanPlayerTime(context))
                                        .then(Commands.argument("reason", MessageArgument.message())
                                        .executes(context -> tempbanPlayerReason(context)))));

        dispatcher.register(builder);
    }

    private static int tempban(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        player.sendMessage(Methods.formatText("maessentials.provide.player"));
        return Command.SINGLE_SUCCESS;
    }

    private static int tempbanPlayer(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        player.sendMessage(Methods.formatText("tempban.maessentials.provide.time"));
        return Command.SINGLE_SUCCESS;
    }

    private static int tempbanPlayerTime(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        ServerPlayerEntity targetPlayer = EntityArgument.getPlayer(context, "targetPlayer");
        String time = StringArgumentType.getString(context, "time");
        doTempban(player, targetPlayer, time, "No reason provided");
        return Command.SINGLE_SUCCESS;
    }

    private static int tempbanPlayerReason(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        ServerPlayerEntity targetPlayer = EntityArgument.getPlayer(context, "targetPlayer");
        String time = StringArgumentType.getString(context, "time").toLowerCase();
        ITextComponent reason = MessageArgument.getMessage(context, "reason");
        doTempban(player, targetPlayer, time, reason.getString());
        return Command.SINGLE_SUCCESS;
    }

    private static void doTempban(ServerPlayerEntity player, ServerPlayerEntity target, String time, String reason) {
        EssentialPlayer eslTargetPlayer = DataManager.getPlayer(target);
        PlayerRestriction ban = eslTargetPlayer.getRestrictions().getBan();

        long banTime = Time.parseDate(time, true);
        if (time.equals("perm")) {
            banTime = -1;
        }

        if (banTime == 0) {
            player.sendMessage(Methods.formatText("maessentials.illegal_date"));
            return;
        }

        if (ban != null && (currentTimestamp() < ban.getTime() ||  ban.getTime() == -1)) {
            player.sendMessage(Methods.formatText("tempban.maessentials.already_banned", target.getDisplayName()));
            return;
        }

        eslTargetPlayer.getRestrictions().setBan(banTime, reason);
        eslTargetPlayer.saveData();

        if (banTime == -1) {
            player.server.getPlayerList().sendMessage(Methods.formatText("tempban.maessentials.success.perm", target.getDisplayName(), player.getDisplayName(), reason));
            target.connection.disconnect(Methods.formatText("tempban.maessentials.success.perm.target", player.getDisplayName(), reason));
        } else {
            String displayTime = Time.formatDate(banTime - currentTimestamp());
            player.server.getPlayerList().sendMessage(Methods.formatText("tempban.maessentials.success", target.getDisplayName(), player.getDisplayName(), displayTime, reason));
            target.connection.disconnect(Methods.formatText("tempban.maessentials.success.target", player.getDisplayName(), displayTime, reason));
        }
    }
}
