package com.maciej916.maessentials.common.command.impl.warp;

import com.maciej916.maessentials.common.command.BaseCommand;
import com.maciej916.maessentials.common.data.DataManager;
import com.maciej916.maessentials.common.lib.Location;
import com.maciej916.maessentials.common.util.ModUtils;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;

public class DelWarpCommand extends BaseCommand {

    public DelWarpCommand(String command, int permissionLevel, boolean enabled) {
        super(command, permissionLevel, enabled);
    }

    @Override
    public LiteralArgumentBuilder<CommandSource> setExecution() {
        return builder.executes((context) -> execute(context.getSource())).then(Commands.argument("warpName", StringArgumentType.string()).suggests(ModUtils.WARP_SUGGEST).executes((context) -> execute(context.getSource(), StringArgumentType.getString(context, "warpName"))));
    }

    private int execute(CommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.asPlayer();
        sendMessage(player, "warp.maessentials.specify_name");
        return Command.SINGLE_SUCCESS;
    }

    private int execute(CommandSource source, String warpName) throws CommandSyntaxException {
        ServerPlayerEntity player = source.asPlayer();

        if (DataManager.getWarp().delWarp(warpName)) {
            sendMessage(player, "delwarp.maessentials.success", warpName);
        } else {
            sendMessage(player, "warp.maessentials.not_exist", warpName);
        }

        return Command.SINGLE_SUCCESS;
    }

}
