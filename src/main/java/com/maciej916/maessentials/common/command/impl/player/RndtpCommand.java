package com.maciej916.maessentials.common.command.impl.player;

import com.maciej916.maessentials.common.command.BaseCommand;
import com.maciej916.maessentials.common.config.ModConfig;
import com.maciej916.maessentials.common.data.DataManager;
import com.maciej916.maessentials.common.lib.Location;
import com.maciej916.maessentials.common.lib.player.EssentialPlayer;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.server.ServerWorld;

import static com.maciej916.maessentials.common.util.LocationUtils.findRandomTp;
import static com.maciej916.maessentials.common.util.TeleportUtils.simpleTeleport;

public class RndtpCommand extends BaseCommand {

    public RndtpCommand(String command, int permissionLevel, boolean enabled) {
        super(command, permissionLevel, enabled);
    }

    @Override
    public LiteralArgumentBuilder<CommandSource> setExecution() {
        return builder.executes((context) -> execute(context.getSource()));
    }

    private int execute(CommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.asPlayer();
        ServerWorld world = source.getWorld();
        EssentialPlayer eslPlayer = DataManager.getPlayer(player);

        long cooldown = eslPlayer.getUsage().getTeleportCooldown("rndtp", ModConfig.rndtp_cooldown);
        if (cooldown != 0) {
            sendMessage(player, "maessentials.cooldown", cooldown);
            return Command.SINGLE_SUCCESS;
        }

        Location spawnLocation = DataManager.getWorld().getSpawn();
        if (spawnLocation.getDimensionID() != player.dimension.getId()) {
            sendMessage(player, "rndtp.maessentials.dimension");
            return Command.SINGLE_SUCCESS;
        }

        Location location = findRandomTp(world, spawnLocation, player, 0);
        if (location == null) {
            sendMessage(player, "rndtp.maessentials.not_found");
            return Command.SINGLE_SUCCESS;
        }

        eslPlayer.getUsage().setCommandUsage("rndtp");
        eslPlayer.saveData();
        if (simpleTeleport(player, location, "rndtp", ModConfig.rndtp_delay)) {
            if (ModConfig.rndtp_delay == 0) {
                sendMessage(player, "rndtp.maessentials.teleport");
            } else {
                sendMessage(player, "rndtp.maessentials.teleport.wait", ModConfig.rndtp_delay);
            }
        }

        return Command.SINGLE_SUCCESS;
    }

}
