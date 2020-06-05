package com.maciej916.maessentials.common.command.impl.warp;

import com.maciej916.maessentials.common.command.BaseCommand;
import com.maciej916.maessentials.common.config.ModConfig;
import com.maciej916.maessentials.common.data.DataManager;
import com.maciej916.maessentials.common.lib.Location;
import com.maciej916.maessentials.common.lib.player.EssentialPlayer;
import com.maciej916.maessentials.common.util.ModUtils;
import com.maciej916.maessentials.common.util.PlayerUtils;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;

import static com.maciej916.maessentials.common.util.TeleportUtils.simpleTeleport;

public class WarpCommand extends BaseCommand {

    public WarpCommand(String command, int permissionLevel, boolean enabled) {
        super(command, permissionLevel, enabled);
    }

    @Override
    public LiteralArgumentBuilder<CommandSource> setExecution() {
        return builder.executes((context) -> execute(context.getSource())).then(Commands.argument("warpName", StringArgumentType.string()).suggests(ModUtils.WARP_SUGGEST).executes((context) -> execute(context.getSource(), StringArgumentType.getString(context, "warpName"))));
    }

    private int execute(CommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.asPlayer();
        PlayerUtils.warpList(player);
        return Command.SINGLE_SUCCESS;
    }

    private int execute(CommandSource source, String warpName) throws CommandSyntaxException {
        ServerPlayerEntity player = source.asPlayer();
        EssentialPlayer eslPlayer = DataManager.getPlayer(player);

        Location location = DataManager.getWarp().getWarp(warpName);
        if (location == null) {
            sendMessage(player, "warp.maessentials.not_exist", warpName);
            return Command.SINGLE_SUCCESS;
        }

        long cooldown = eslPlayer.getUsage().getTeleportCooldown("warp", ModConfig.warps_cooldown);
        if (cooldown != 0) {
            sendMessage(player, "maessentials.cooldown.teleport", cooldown);
            return Command.SINGLE_SUCCESS;
        }

        eslPlayer.getUsage().setCommandUsage("warp");
        eslPlayer.saveData();

        if (simpleTeleport(player, location, "warp", ModConfig.warps_delay)) {
            if (ModConfig.warps_delay == 0) {
                sendMessage(player, "warp.maessentials.success", warpName);
            } else {
                sendMessage(player, "warp.maessentials.success.wait", warpName, ModConfig.warps_delay);
            }
        }

        return Command.SINGLE_SUCCESS;
    }

}
