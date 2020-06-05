package com.maciej916.maessentials.common.command.impl.admin;

import com.maciej916.maessentials.common.command.BaseCommand;
import com.maciej916.maessentials.common.data.DataManager;
import com.maciej916.maessentials.common.lib.player.EssentialPlayer;
import com.maciej916.maessentials.common.lib.player.PlayerRestriction;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;

import static com.maciej916.maessentials.common.util.TimeUtils.currentTimestamp;

public class UnbanCommand extends BaseCommand {

    public UnbanCommand(String command, int permissionLevel, boolean enabled) {
        super(command, permissionLevel, enabled);
    }

    @Override
    public LiteralArgumentBuilder<CommandSource> setExecution() {
        return builder.then(Commands.argument("targetPlayer", StringArgumentType.word()).executes((context) -> execute(context.getSource(), StringArgumentType.getString(context, "targetPlayer"))));
    }

    private int execute(CommandSource source, String targetPlayer) throws CommandSyntaxException {
        ServerPlayerEntity player = source.asPlayer();
        EssentialPlayer eslTargetPlayer = DataManager.getPlayer(targetPlayer);

        if (eslTargetPlayer == null) {
            sendMessage(player, "maessentials.not_found.player", targetPlayer);
        } else {
            PlayerRestriction ban = eslTargetPlayer.getRestrictions().getBan();

            if (ban == null || (currentTimestamp() > ban.getTime() && ban.getTime() != -1)) {
                sendMessage(player, "unban.maessentials.not_banned", eslTargetPlayer.getUsername());
            } else {
                eslTargetPlayer.getRestrictions().unBan();
                eslTargetPlayer.saveData();
                sendGlobalMessage(player.server.getPlayerList(), "unban.maessentials.success", eslTargetPlayer.getUsername());
            }
        }

        return Command.SINGLE_SUCCESS;
    }

}
