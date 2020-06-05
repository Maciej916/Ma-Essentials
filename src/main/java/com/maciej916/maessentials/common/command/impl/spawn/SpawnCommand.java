package com.maciej916.maessentials.common.command.impl.spawn;

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

import static com.maciej916.maessentials.common.util.TeleportUtils.simpleTeleport;

public class SpawnCommand extends BaseCommand {

    public SpawnCommand(String command, int permissionLevel, boolean enabled) {
        super(command, permissionLevel, enabled);
    }

    @Override
    public LiteralArgumentBuilder<CommandSource> setExecution() {
        return builder.executes((context) -> execute(context.getSource()));
    }

    private int execute(CommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.asPlayer();
        EssentialPlayer eslPlayer = DataManager.getPlayer(player);

        long cooldown = eslPlayer.getUsage().getTeleportCooldown("spawn", ModConfig.spawn_cooldown);
        if (cooldown != 0) {
            sendMessage(player, "maessentials.cooldown.teleport", cooldown);
            return Command.SINGLE_SUCCESS;
        }

        eslPlayer.getUsage().setCommandUsage("spawn");
        eslPlayer.saveData();

        Location location = DataManager.getWorld().getSpawn();
        if (simpleTeleport(player, location, "spawn", ModConfig.spawn_delay)) {
            if (ModConfig.spawn_delay == 0) {
                sendMessage(player, "spawn.maessentials.success");
            } else {
                sendMessage(player, "spawn.maessentials.success.wait", ModConfig.spawn_delay);
            }
        }

        return Command.SINGLE_SUCCESS;
    }

}
