package com.maciej916.maessentials.common.command.impl.teleport_request;

import com.maciej916.maessentials.common.command.BaseCommand;
import com.maciej916.maessentials.common.lib.teleport.TeleportRequest;
import com.maciej916.maessentials.common.util.TeleportUtils;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;

import java.util.ArrayList;

public class TpdenyCommand extends BaseCommand {

    public TpdenyCommand(String command, int permissionLevel, boolean enabled) {
        super(command, permissionLevel, enabled);
    }

    @Override
    public LiteralArgumentBuilder<CommandSource> setExecution() {
        return builder.executes((context) -> execute(context.getSource())).then(Commands.argument("targetPlayer", EntityArgument.players()).executes(context -> execute(context.getSource(), EntityArgument.getPlayer(context, "targetPlayer"))));
    }


    private int execute(CommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.asPlayer();
        ArrayList<TeleportRequest> requests = TeleportUtils.findRequest(player);
        if (requests.size() == 1) {
            TeleportUtils.declineRequest(requests.get(0));
        } else if (requests.size() > 1) {
            sendMessage(player, "maessentials.specify.player");
        } else {
            sendMessage(player, "tpa.maessentials.no_request");
        }
        return Command.SINGLE_SUCCESS;
    }

    private int execute(CommandSource source, ServerPlayerEntity targetPlayer) throws CommandSyntaxException {
        ServerPlayerEntity player = source.asPlayer();
        TeleportRequest tpR = TeleportUtils.findRequest(player, targetPlayer);
        if (tpR != null) {
            TeleportUtils.declineRequest(tpR);
        } else {
            sendMessage(player, "tpa.maessentials.not_found");
        }
        return Command.SINGLE_SUCCESS;
    }

}
