package com.maciej916.maessentials.common.command.impl.warp;

import com.maciej916.maessentials.common.command.BaseCommand;
import com.maciej916.maessentials.common.data.DataManager;
import com.maciej916.maessentials.common.lib.Location;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;

public class SetWarpCommand extends BaseCommand {

    public SetWarpCommand(String command, int permissionLevel, boolean enabled) {
        super(command, permissionLevel, enabled);
    }

    @Override
    public LiteralArgumentBuilder<CommandSource> setExecution() {
        return builder.executes((context) -> execute(context.getSource())).then(Commands.argument("warpName", StringArgumentType.string()).executes((context) -> execute(context.getSource(), StringArgumentType.getString(context, "warpName"))));
    }

    private int execute(CommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.asPlayer();
        sendMessage(player, "warp.maessentials.specify_name");
        return Command.SINGLE_SUCCESS;
    }

    private int execute(CommandSource source, String warpName) throws CommandSyntaxException {
        ServerPlayerEntity player = source.asPlayer();

        if (DataManager.getWarp().setWarp(warpName, new Location(player))) {
            sendMessage(player, "setwarp.maessentials.success", warpName);
        } else {
            sendMessage(player, "setwarp.maessentials.exist", warpName);
        }

        return Command.SINGLE_SUCCESS;
    }

}
