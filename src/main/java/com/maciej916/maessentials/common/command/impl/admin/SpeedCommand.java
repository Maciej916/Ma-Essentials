package com.maciej916.maessentials.common.command.impl.admin;

import com.maciej916.maessentials.common.command.BaseCommand;
import com.maciej916.maessentials.common.config.ModConfig;
import com.maciej916.maessentials.common.network.Networking;
import com.maciej916.maessentials.common.network.packets.PacketSpeed;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.network.NetworkDirection;

public class SpeedCommand extends BaseCommand {

    public SpeedCommand(String command, int permissionLevel, boolean enabled) {
        super(command, permissionLevel, enabled);
    }

    private static final float flySpeedDefault = 0.05F;
    private static final float walkSpeedDefault = 0.1F;

    @Override
    public LiteralArgumentBuilder<CommandSource> setExecution() {
        return builder.then(Commands.argument("speed", IntegerArgumentType.integer()).executes((context) -> execute(context.getSource(), IntegerArgumentType.getInteger(context, "speed"))).then(Commands.argument("targetPlayer", EntityArgument.players()).executes((context) -> execute(context.getSource(), IntegerArgumentType.getInteger(context, "speed"), EntityArgument.getPlayer(context, "targetPlayer")))));
    }

    private int execute(CommandSource source, int speed) throws CommandSyntaxException {
        ServerPlayerEntity player = source.asPlayer();
        doSpeed(player, speed, player);
        return Command.SINGLE_SUCCESS;
    }

    private int execute(CommandSource source, int speed, ServerPlayerEntity targetPlayer) throws CommandSyntaxException {
        ServerPlayerEntity player = source.asPlayer();
        doSpeed(player, speed, targetPlayer);
        return Command.SINGLE_SUCCESS;
    }

    private void doSpeed(ServerPlayerEntity player, int speed, ServerPlayerEntity target) {
        if (target.abilities.isFlying) {
            if (speed > ModConfig.speed_max_fly) {
                sendMessage(player, "speed.maessentials.max_fly", ModConfig.speed_max_fly);
            } else {
                float flySpeed = speed * flySpeedDefault;
                Networking.INSTANCE.sendTo(new PacketSpeed(false, flySpeed), target.connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
                if (player == target) {
                    sendMessage(player, "speed.maessentials.fly.self", speed);
                } else {
                    sendMessage(player, "speed.maessentials.fly.other", target.getDisplayName(), speed);
                    sendMessage(target, "speed.maessentials.fly.self", speed);
                }
            }
        } else {
            if (speed > ModConfig.speed_max_walk) {
                sendMessage(player, "speed.maessentials.max_walk", ModConfig.speed_max_fly);
            } else {
                float walkSpeed = speed * walkSpeedDefault;
                Networking.INSTANCE.sendTo(new PacketSpeed(true, walkSpeed), target.connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
                if (player == target) {
                    sendMessage(player, "speed.maessentials.walk.self", speed);
                } else {
                    sendMessage(player, "speed.maessentials.walk.other", target.getDisplayName(), speed);
                    sendMessage(target, "speed.maessentials.walk.self", speed);
                }
            }
        }
    }

}
