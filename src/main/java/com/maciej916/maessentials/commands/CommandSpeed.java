package com.maciej916.maessentials.commands;

import com.maciej916.maessentials.config.ConfigValues;
import com.maciej916.maessentials.libs.Methods;
import com.maciej916.maessentials.network.Networking;
import com.maciej916.maessentials.network.PacketSpeed;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.network.NetworkDirection;

public class CommandSpeed {

    private static final float flySpeedDefault = 0.05F;
    private static final float walkSpeedDefault = 0.1F;

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("speed").requires((source) -> source.hasPermissionLevel(2))
                .then(Commands.argument("speed", IntegerArgumentType.integer()).executes((context) -> speed(context.getSource(), IntegerArgumentType.getInteger(context, "speed")))
                        .then(Commands.argument("targetPlayer", EntityArgument.players()).executes((context) -> speed(context.getSource(), IntegerArgumentType.getInteger(context, "speed"), EntityArgument.getPlayer(context, "targetPlayer"))))
                )
        );
    }

    private static int speed(CommandSource source, int speed) throws CommandSyntaxException {
        ServerPlayerEntity player = source.asPlayer();
        doSpeed(player, speed, player);
        return Command.SINGLE_SUCCESS;
    }

    private static int speed(CommandSource source, int speed, ServerPlayerEntity target) throws CommandSyntaxException {
        ServerPlayerEntity player = source.asPlayer();
        doSpeed(player, speed, target);
        return Command.SINGLE_SUCCESS;
    }

    private static void doSpeed(ServerPlayerEntity player, int speed, ServerPlayerEntity target) {
        if (target.abilities.isFlying) {
            if (speed > ConfigValues.speed_max_fly) {
                player.sendMessage(Methods.formatText("speed.maessentials.max_fly", ConfigValues.speed_max_fly));
            } else {
                float flySpeed = speed * flySpeedDefault;
                Networking.INSTANCE.sendTo(new PacketSpeed(false, flySpeed), target.connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
                if (player == target) {
                    player.sendMessage(Methods.formatText("speed.maessentials.fly.self", speed));
                } else {
                    player.sendMessage(Methods.formatText("speed.maessentials.fly.other", target.getDisplayName(), speed));
                    target.sendMessage(Methods.formatText("speed.maessentials.fly.self", speed));
                }
            }
        } else {
            if (speed > ConfigValues.speed_max_walk) {
                player.sendMessage(Methods.formatText("speed.maessentials.max_walk", ConfigValues.speed_max_fly));
            } else {
                float walkSpeed = speed * walkSpeedDefault;
                Networking.INSTANCE.sendTo(new PacketSpeed(true, walkSpeed), target.connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
                if (player == target) {
                    player.sendMessage(Methods.formatText("speed.maessentials.walk.self", speed));
                } else {
                    player.sendMessage(Methods.formatText("speed.maessentials.walk.other", target.getDisplayName(), speed));
                    target.sendMessage(Methods.formatText("speed.maessentials.walk.self", speed));
                }
            }
        }
    }
}