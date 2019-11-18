package com.maciej916.maessentials.commands;

import com.maciej916.maessentials.classes.player.EssentialPlayer;
import com.maciej916.maessentials.classes.player.PlayerRestriction;
import com.maciej916.maessentials.data.DataManager;
import com.maciej916.maessentials.libs.Methods;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;

import static com.maciej916.maessentials.libs.Methods.currentTimestamp;

public class CommandUnban {

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder<CommandSource> builder = Commands.literal("unban").requires(source -> source.hasPermissionLevel(2));
        builder
                .executes(context -> unban(context))
                        .then(Commands.argument("targetPlayer", EntityArgument.players())
                                .executes(context -> unbanArgs(context)));

        dispatcher.register(builder);
    }

    private static int unban(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        player.sendMessage(Methods.formatText("maessentials.provide.player"));
        return Command.SINGLE_SUCCESS;
    }

    private static int unbanArgs(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        ServerPlayerEntity requestedPlayer = EntityArgument.getPlayer(context, "targetPlayer");
        doUnban(player, requestedPlayer);
        return Command.SINGLE_SUCCESS;
    }

    private static void doUnban(ServerPlayerEntity player, ServerPlayerEntity target) {
        EssentialPlayer eslTargetPlayer = DataManager.getPlayer(target);
        PlayerRestriction ban = eslTargetPlayer.getRestrictions().getBan();

        if (ban == null || (currentTimestamp() > ban.getTime() && ban.getTime() != -1)) {
            player.sendMessage(Methods.formatText("unban.maessentials.not_banned", target.getDisplayName()));
            return;
        }

        eslTargetPlayer.getRestrictions().unBan();
        eslTargetPlayer.saveData();

        player.server.getPlayerList().sendMessage(Methods.formatText("unban.maessentials.success", target.getDisplayName()));
    }
}
