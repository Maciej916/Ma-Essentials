package com.maciej916.maessentials.commands;

import com.maciej916.maessentials.classes.Location;
import com.maciej916.maessentials.classes.player.EssentialPlayer;
import com.maciej916.maessentials.config.ConfigValues;
import com.maciej916.maessentials.data.DataManager;
import com.maciej916.maessentials.libs.Methods;
import com.maciej916.maessentials.libs.Teleport;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;

import static com.maciej916.maessentials.libs.Methods.simpleTeleport;

public class CommandSpawn {

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder<CommandSource> builder = Commands.literal("spawn").requires(source -> source.hasPermissionLevel(0));
        builder.executes(context -> spawn(context));
        dispatcher.register(builder);
    }

    private static int spawn(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        EssentialPlayer eslPlayer = DataManager.getPlayer(player);

        long cooldown = eslPlayer.getUsage().getTeleportCooldown("spawn", ConfigValues.spawn_cooldown);
        if (cooldown != 0) {
            player.sendMessage(Methods.formatText("maessentials.cooldown", cooldown));
            return Command.SINGLE_SUCCESS;
        }

        eslPlayer.getUsage().setCommandUsage("spawn");
        eslPlayer.saveData();

        Location location = DataManager.getWorld().getSpawn();
        if (simpleTeleport(player, location, "spawn", ConfigValues.spawn_delay)) {
            if (ConfigValues.spawn_delay == 0) {
                player.sendMessage(Methods.formatText("spawn.maessentials.success"));
            } else {
                player.sendMessage(Methods.formatText("spawn.maessentials.success.wait", ConfigValues.spawn_delay));
            }
        }

        return Command.SINGLE_SUCCESS;
    }
}