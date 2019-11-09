package com.maciej916.maessentials.commands;

import com.maciej916.maessentials.classes.Location;
import com.maciej916.maessentials.classes.player.EssentialPlayer;
import com.maciej916.maessentials.config.ConfigValues;
import com.maciej916.maessentials.data.DataManager;
import com.maciej916.maessentials.libs.Methods;
import com.maciej916.maessentials.libs.Teleport;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;

import static com.maciej916.maessentials.libs.Methods.simpleTeleport;

public class CommandHome {
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder<CommandSource> builder = Commands.literal("home").requires(source -> source.hasPermissionLevel(0));
        builder
                .executes(context -> home(context))
                .then(Commands.argument("homeName", StringArgumentType.string())
                        .suggests(Methods.HOME_SUGGEST)
                        .executes(context -> homeArgs(context)));
        dispatcher.register(builder);
    }

    private static int home(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        doHome(player, "home");
        return Command.SINGLE_SUCCESS;
    }

    private static int homeArgs(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        String homeName = StringArgumentType.getString(context, "homeName").toLowerCase();
        doHome(player, homeName);
        return Command.SINGLE_SUCCESS;
    }

    private static void doHome(ServerPlayerEntity player, String name) {
        EssentialPlayer eslPlayer = DataManager.getPlayer(player);

        if (eslPlayer.getHomeData().getHomes().size() == 0) {
            player.sendMessage(Methods.formatText("home.maessentials.no_homes"));
            return;
        }

        Location location = eslPlayer.getHomeData().getHome(name);
        if (location == null) {
            player.sendMessage(Methods.formatText("home.maessentials.not_exist", name));
            return;
        }

        long cooldown = eslPlayer.getUsage().getTeleportCooldown("home", ConfigValues.homes_cooldown);
        if (cooldown != 0) {
            player.sendMessage(Methods.formatText("maessentials.cooldown", cooldown));
            return;
        }

        eslPlayer.getUsage().setTeleportUsage("home");
        eslPlayer.saveData();

        if (simpleTeleport(player, location, "home", ConfigValues.homes_delay)) {
            if (ConfigValues.homes_delay == 0) {
                player.sendMessage(Methods.formatText("home.maessentials.teleport", name));
            } else {
                player.sendMessage(Methods.formatText("home.maessentials.teleport.wait", name, ConfigValues.homes_delay));
            }
        }
    }
}