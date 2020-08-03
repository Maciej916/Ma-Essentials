package com.maciej916.maessentials.common.command.impl.admin;

import com.maciej916.maessentials.common.command.BaseCommand;
import com.maciej916.maessentials.common.data.DataManager;
import com.maciej916.maessentials.common.lib.player.EssentialPlayer;
import com.maciej916.maessentials.common.util.PlayerUtils;
import com.maciej916.maessentials.common.util.TextUtils;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.GameType;

public class FlyCommand extends BaseCommand {

    public FlyCommand(String command, int permissionLevel, boolean enabled) {
        super(command, permissionLevel, enabled);
    }

    @Override
    public LiteralArgumentBuilder<CommandSource> setExecution() {
        return builder.executes((context) -> execute(context.getSource())).then(Commands.argument("targetPlayer", EntityArgument.players()).executes(context -> execute(context.getSource(), EntityArgument.getPlayer(context, "targetPlayer"))));
    }

    private int execute(CommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.asPlayer();
        doFly(player, player);
        return Command.SINGLE_SUCCESS;
    }

    private int execute(CommandSource source, ServerPlayerEntity targetPlayer) throws CommandSyntaxException {
        ServerPlayerEntity player = source.asPlayer();
        doFly(player, targetPlayer);
        return Command.SINGLE_SUCCESS;
    }

    private void doFly(ServerPlayerEntity player, ServerPlayerEntity target) {
        if (target.interactionManager.getGameType() == GameType.CREATIVE || target.interactionManager.getGameType() == GameType.SPECTATOR) {
            if (player == target) {
                sendMessage(target,"maessentials.invaild_gamemode");
            } else {
                sendMessage(target,"maessentials.invaild_gamemode.player", target.getDisplayName());
            }
            return;
        }

        EssentialPlayer eslTargetPlayer = DataManager.getPlayer(target);
        eslTargetPlayer.getData().setFlyEnabled(!target.abilities.allowFlying);
        eslTargetPlayer.saveData();

        PlayerUtils.setFlying(player, target, !target.abilities.allowFlying, true);
    }

}
