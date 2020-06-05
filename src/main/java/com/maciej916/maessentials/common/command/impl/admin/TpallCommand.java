package com.maciej916.maessentials.common.command.impl.admin;

import com.maciej916.maessentials.common.command.BaseCommand;
import com.maciej916.maessentials.common.lib.Location;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;

import static com.maciej916.maessentials.common.util.TeleportUtils.doTeleport;

public class TpallCommand extends BaseCommand {

    public TpallCommand(String command, int permissionLevel, boolean enabled) {
        super(command, permissionLevel, enabled);
    }

    @Override
    public LiteralArgumentBuilder<CommandSource> setExecution() {
        return builder.executes((context) -> execute(context.getSource()));
    }

    private int execute(CommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.asPlayer();
        Location location = new Location(player);

        for (ServerPlayerEntity tp : player.server.getPlayerList().getPlayers()) {
            if (player.getUniqueID() != tp.getUniqueID()) {
                doTeleport(tp, location, true, true);
                sendMessage(tp, "tpall.maessentials.teleported", player.getDisplayName());
            }
        }

        sendMessage(player, "tpall.maessentials.success");
        return Command.SINGLE_SUCCESS;
    }

}
