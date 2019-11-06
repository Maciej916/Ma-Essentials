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
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

import java.util.Set;

public class CommandWarp {
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder<CommandSource> builder = Commands.literal("warp").requires(source -> source.hasPermissionLevel(0));
        builder
                .executes(context -> warp(context))
                        .then(Commands.argument("warpName", StringArgumentType.string())
                                .suggests(Methods.WARP_SUGGEST)
                                .executes(context -> warpArgs(context)));
        dispatcher.register(builder);

        LiteralArgumentBuilder<CommandSource> builder2 = Commands.literal("warps").requires(source -> source.hasPermissionLevel(0));
        builder2.executes(context -> warp(context));
        dispatcher.register(builder2);
    }

    private static int warp(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();

        Set<String> warps = DataManager.getWarp().getWarps().keySet();
        TextComponent warpList = Methods.formatText("warp.maessentials.list", warps.size());
        if (warps.size() != 0) {
            int i = 1;
            for (String name : warps) {
                ClickEvent clickEvent = new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/warp " + name);
                HoverEvent eventHover = new HoverEvent(HoverEvent.Action.SHOW_TEXT, Methods.formatText("warp.maessentials.list.warp", "/warp " + name));

                TextComponent kit = Methods.formatText("warp.maessentials.list.warp", name);
                kit.getStyle().setClickEvent(clickEvent);
                kit.getStyle().setHoverEvent(eventHover);

                warpList.appendSibling(kit);
                if (warps.size() != i) {
                    warpList.appendSibling(new StringTextComponent(", "));
                    i++;
                }
            }
        } else {
            warpList.appendSibling(new StringTextComponent("-"));
        }

        player.sendMessage(warpList);
        return Command.SINGLE_SUCCESS;
    }

    private static int warpArgs(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        String warpName = StringArgumentType.getString(context, "warpName").toLowerCase();
        doWarp(player, warpName);
        return Command.SINGLE_SUCCESS;
    }

    private static void doWarp(ServerPlayerEntity player, String name) {
        EssentialPlayer eslPlayer = DataManager.getPlayer(player);

        Location location = DataManager.getWarp().getWarp(name);
        if (location == null) {
            player.sendMessage(Methods.formatText("warp.maessentials.not_exist", name));
            return;
        }

        long cooldown = eslPlayer.getUsage().getCommandCooldown("warp", ConfigValues.warps_cooldown);
        if (cooldown != 0) {
            player.sendMessage(Methods.formatText("maessentials.cooldown", cooldown));
            return;
        }

        eslPlayer.getUsage().setCommandUsage("warp");
        eslPlayer.saveData();

        if (ConfigValues.warps_delay == 0) {
            player.sendMessage(Methods.formatText("warp.maessentials.success", name));
        } else {
            player.sendMessage(Methods.formatText("warp.maessentials.success.wait", name, ConfigValues.warps_delay));
        }

        Teleport.teleportPlayer(player, location, true, ConfigValues.warps_delay);
    }
}
