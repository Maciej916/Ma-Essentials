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

public class CommandBack {

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder<CommandSource> builder = Commands.literal("back").requires(source -> source.hasPermissionLevel(0));
        builder.executes(context -> back(context));
        dispatcher.register(builder);
    }

    private static int back(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        EssentialPlayer eslPlayer = DataManager.getPlayer(player);

        Location location = eslPlayer.getData().getLastLocation();
        if (location == null) {
            player.sendMessage(Methods.formatText("back.maessentials.not_found"));
            return Command.SINGLE_SUCCESS;
        }

        long cooldown = eslPlayer.getUsage().getCommandCooldown("back", ConfigValues.back_cooldown);
        if (cooldown != 0) {
            player.sendMessage(Methods.formatText("maessentials.cooldown", cooldown));
            return Command.SINGLE_SUCCESS;
        }

        eslPlayer.getUsage().setCommandUsage("back");
        eslPlayer.saveData();

        if (ConfigValues.back_delay == 0) {
            player.sendMessage(Methods.formatText("back.maessentials.success"));
        } else {
            player.sendMessage(Methods.formatText("back.maessentials.success.wait", ConfigValues.back_delay));
        }

        Teleport.teleportPlayer(player, location, true, ConfigValues.back_delay);
        return Command.SINGLE_SUCCESS;
    }
}