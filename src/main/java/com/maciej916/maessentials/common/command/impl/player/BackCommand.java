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

import static com.maciej916.maessentials.common.util.TeleportUtils.simpleTeleport;
import static com.maciej916.maessentials.common.util.TimeUtils.currentTimestamp;

public class BackCommand extends BaseCommand {

    public BackCommand(String command, int permissionLevel, boolean enabled) {
        super(command, permissionLevel, enabled);
    }

    @Override
    public LiteralArgumentBuilder<CommandSource> setExecution() {
        return builder.executes((context) -> execute(context.getSource()));
    }

    private int execute(CommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.asPlayer();
        EssentialPlayer eslPlayer = DataManager.getPlayer(player);

        Location location = eslPlayer.getData().getLastLocation();
        if (location == null) {
            sendMessage(player, "back.maessentials.not_found");
            return Command.SINGLE_SUCCESS;
        }

        long deathTime = eslPlayer.getData().getLastDeath();
        boolean recentlyDied = deathTime + ModConfig.back_death_custom_cooldown > currentTimestamp();

        if (ModConfig.back_death_custom_cooldown != 0 && recentlyDied) {
            long cooldown = eslPlayer.getUsage().getTeleportCooldown("back", ModConfig.back_death_custom_cooldown);
            if (cooldown != 0) {
                sendMessage(player, "back.maessentials.cooldown.teleport", cooldown);
                return Command.SINGLE_SUCCESS;
            }
        } else {
            long cooldown = eslPlayer.getUsage().getTeleportCooldown("back", ModConfig.back_cooldown);
            if (cooldown != 0) {
                sendMessage(player, "maessentials.cooldown.teleport", cooldown);
                return Command.SINGLE_SUCCESS;
            }
        }

        eslPlayer.getUsage().setCommandUsage("back");
        eslPlayer.saveData();

        if (simpleTeleport(player, location, "back", ModConfig.back_delay)) {
            if (ModConfig.back_delay == 0) {
                sendMessage(player, "back.maessentials.success");
            } else {
                sendMessage(player, "back.maessentials.success.wait", ModConfig.back_delay);
            }
        }

        return Command.SINGLE_SUCCESS;
    }

}
