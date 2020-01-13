package com.maciej916.maessentials.commands;

import com.maciej916.maessentials.classes.Location;
import com.maciej916.maessentials.classes.player.EssentialPlayer;
import com.maciej916.maessentials.config.ConfigValues;
import com.maciej916.maessentials.data.DataManager;
import com.maciej916.maessentials.libs.Methods;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;

import static com.maciej916.maessentials.libs.Methods.currentTimestamp;
import static com.maciej916.maessentials.libs.Methods.simpleTeleport;

public class CommandBack {

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("back").requires((source) -> source.hasPermissionLevel(0))
                .executes((context) -> back(context.getSource()))
        );
    }

    private static int back(CommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.asPlayer();
        EssentialPlayer eslPlayer = DataManager.getPlayer(player);

        Location location = eslPlayer.getData().getLastLocation();
        if (location == null) {
            player.sendMessage(Methods.formatText("back.maessentials.not_found"));
            return Command.SINGLE_SUCCESS;
        }

        long deathTime = eslPlayer.getData().getLastDeath();
        boolean recentlyDied = deathTime + ConfigValues.back_death_custom_cooldown > currentTimestamp();

        if (ConfigValues.back_death_custom_cooldown != 0 && recentlyDied) {
            long cooldown = eslPlayer.getUsage().getTeleportCooldown("back", ConfigValues.back_death_custom_cooldown);
            if (cooldown != 0) {
                player.sendMessage(Methods.formatText("back.maessentials.cooldown.teleport", cooldown));
                return Command.SINGLE_SUCCESS;
            }
        } else {
            long cooldown = eslPlayer.getUsage().getTeleportCooldown("back", ConfigValues.back_cooldown);
            if (cooldown != 0) {
                player.sendMessage(Methods.formatText("maessentials.cooldown.teleport", cooldown));
                return Command.SINGLE_SUCCESS;
            }
        }

        eslPlayer.getUsage().setCommandUsage("back");
        eslPlayer.saveData();

        if (simpleTeleport(player, location, "back", ConfigValues.back_delay)) {
            if (ConfigValues.back_delay == 0) {
                player.sendMessage(Methods.formatText("back.maessentials.success"));
            } else {
                player.sendMessage(Methods.formatText("back.maessentials.success.wait", ConfigValues.back_delay));
            }
        }

        return Command.SINGLE_SUCCESS;
    }
}