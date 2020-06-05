package com.maciej916.maessentials.common.command.impl.admin;

import com.maciej916.maessentials.common.command.BaseCommand;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.GameType;

public class HealCommand extends BaseCommand {

    public HealCommand(String command, int permissionLevel, boolean enabled) {
        super(command, permissionLevel, enabled);
    }

    @Override
    public LiteralArgumentBuilder<CommandSource> setExecution() {
        return builder.executes((context) -> execute(context.getSource())).then(Commands.argument("targetPlayer", EntityArgument.players()).executes(context -> execute(context.getSource(), EntityArgument.getPlayer(context, "targetPlayer"))));
    }

    private int execute(CommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.asPlayer();
        doHeal(player, player);
        return Command.SINGLE_SUCCESS;
    }

    private int execute(CommandSource source, ServerPlayerEntity targetPlayer) throws CommandSyntaxException {
        ServerPlayerEntity player = source.asPlayer();
        doHeal(player, targetPlayer);
        return Command.SINGLE_SUCCESS;
    }

    private void doHeal(ServerPlayerEntity player, ServerPlayerEntity target) {
        if (target.interactionManager.getGameType() == GameType.CREATIVE || target.interactionManager.getGameType() == GameType.SPECTATOR) {
            if (player == target) {
                sendMessage(target, "maessentials.invaild_gamemode");
            } else {
                sendMessage(target, "maessentials.invaild_gamemode.player", target.getDisplayName());
            }
            return;
        }

        target.setHealth(player.getMaxHealth());
        target.getFoodStats().setFoodLevel(20);
        target.extinguish();
        target.clearActivePotions();

        if (player == target) {
            sendMessage(target, "heal.maessentials.self");
        } else {
            sendMessage(player, "heal.maessentials.player", target.getDisplayName());
            sendMessage(target, "heal.maessentials.player.target", player.getDisplayName());
        }
    }

}
