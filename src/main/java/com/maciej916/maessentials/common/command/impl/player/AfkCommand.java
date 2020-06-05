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

public class AfkCommand extends BaseCommand {

    public AfkCommand(String command, int permissionLevel, boolean enabled) {
        super(command, permissionLevel, enabled);
    }

    @Override
    public LiteralArgumentBuilder<CommandSource> setExecution() {
        return builder.executes((context) -> execute(context.getSource()));
    }

    private int execute(CommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.asPlayer();
        EssentialPlayer eslPlayer = DataManager.getPlayer(player);

        long cooldown = eslPlayer.getUsage().getCommandCooldown("afk", ModConfig.afk_command_cooldown);
        if (cooldown != 0) {
            sendMessage(player, "maessentials.cooldown", cooldown);
            return Command.SINGLE_SUCCESS;
        }

        if (!eslPlayer.getTemp().isAfk()) {
            eslPlayer.getUsage().setCommandUsage("afk");
            eslPlayer.saveData();

            sendMessage(player, "afk.maessentials.afk.true", player.getDisplayName());
            eslPlayer.getTemp().setLocation(new Location(player));
            eslPlayer.getTemp().setAfk(true);
        }

        return Command.SINGLE_SUCCESS;
    }

}
