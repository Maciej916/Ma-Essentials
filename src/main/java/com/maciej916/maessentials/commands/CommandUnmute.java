package com.maciej916.maessentials.commands;

import com.maciej916.maessentials.classes.player.EssentialPlayer;
import com.maciej916.maessentials.classes.player.PlayerRestriction;
import com.maciej916.maessentials.data.DataManager;
import com.maciej916.maessentials.libs.Methods;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;

import static com.maciej916.maessentials.libs.Methods.currentTimestamp;

public class CommandUnmute {
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("unmute").requires((source) -> source.hasPermissionLevel(2))
                .executes((context) -> unmute(context.getSource()))
                .then(Commands.argument("targetPlayer", StringArgumentType.word()).executes((context) -> unmute(context.getSource(), StringArgumentType.getString(context, "targetPlayer"))))
        );
    }

    private static int unmute(CommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.asPlayer();
        player.sendMessage(Methods.formatText("maessentials.provide.player"));
        return Command.SINGLE_SUCCESS;
    }

    private static int unmute(CommandSource source, String targetPlayer) throws CommandSyntaxException {
        ServerPlayerEntity player = source.asPlayer();
        EssentialPlayer eslTargetPlayer = DataManager.getPlayer(targetPlayer);

        if (eslTargetPlayer == null) {
            player.sendMessage(Methods.formatText("maessentials.not_found.player", targetPlayer));
        } else {
            PlayerRestriction mute = eslTargetPlayer.getRestrictions().getMute();

            if (mute == null || (currentTimestamp() > mute.getTime() && mute.getTime() != -1)) {
                player.sendMessage(Methods.formatText("unmmute.maessentials.not_muted", eslTargetPlayer.getUsername()));
            } else {
                eslTargetPlayer.getRestrictions().unMute();
                eslTargetPlayer.saveData();
                player.server.getPlayerList().sendMessage(Methods.formatText("unmmute.maessentials.success", eslTargetPlayer.getUsername()));
            }
        }

        return Command.SINGLE_SUCCESS;
    }
}
