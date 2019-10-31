package com.maciej916.maessentials.commands;

import com.maciej916.maessentials.config.ConfigValues;
import com.maciej916.maessentials.libs.Methods;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;

public class CommandSpeed {

    private static final float flySpeedDefault = 0.05F;
    private static final float walkSpeedDefault = 0.1F;

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder<CommandSource> builder = Commands.literal("speed").requires(source -> source.hasPermissionLevel(2));
        builder
            .executes(context -> speed(context))
                .then(Commands.argument("speed", IntegerArgumentType.integer())
                    .executes(context -> speedArgs(context))
                        .then(Commands.argument("targetPlayer", EntityArgument.players())
                            .executes(context -> speedArgsPlayer(context))));

        dispatcher.register(builder);
    }

    private static int speed(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        player.sendMessage(Methods.formatText("maessentials.provide.player"));
        return Command.SINGLE_SUCCESS;
    }

    private static int speedArgs(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        Integer speed = IntegerArgumentType.getInteger(context, "speed");
        speedManage(player, player, speed);
        return Command.SINGLE_SUCCESS;
    }

    private static int speedArgsPlayer(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        ServerPlayerEntity requestedPlayer = EntityArgument.getPlayer(context, "targetPlayer");
        Integer speed = IntegerArgumentType.getInteger(context, "speed");
        speedManage(player, requestedPlayer, speed);
        return Command.SINGLE_SUCCESS;
    }

    private static void speedManage(ServerPlayerEntity player, ServerPlayerEntity targetPlayer, float speed) {
        if (targetPlayer.abilities.isFlying) {
            if (speed > ConfigValues.speed_max_fly) {
                player.sendMessage(Methods.formatText("speed.maessentials.max_fly", ConfigValues.speed_max_fly));
            } else {
                Float flySpeed = speed * flySpeedDefault;
                targetPlayer.abilities.setFlySpeed(flySpeed);

                if (player == targetPlayer) {
                    player.sendMessage(Methods.formatText("speed.maessentials.fly.self", speed));
                } else {
                    player.sendMessage(Methods.formatText("speed.maessentials.fly.other", targetPlayer.getDisplayName(), speed));
                    targetPlayer.sendMessage(Methods.formatText("speed.maessentials.fly.self", speed));
                }
            }
        } else {
            if (speed > ConfigValues.speed_max_walk) {
                player.sendMessage(Methods.formatText("speed.maessentials.max_walk", ConfigValues.speed_max_fly));
            } else {
                Float walkSpeed = speed * walkSpeedDefault;
                targetPlayer.abilities.setWalkSpeed(walkSpeed);

                if (player == targetPlayer) {
                    player.sendMessage(Methods.formatText("speed.maessentials.walk.self", speed));
                } else {
                    player.sendMessage(Methods.formatText("speed.maessentials.walk.other", targetPlayer.getDisplayName(), speed));
                    targetPlayer.sendMessage(Methods.formatText("speed.maessentials.walk.self", speed));
                }
            }
        }
        targetPlayer.sendPlayerAbilities();
    }
}