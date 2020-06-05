package com.maciej916.maessentials.common.command.impl.player;

import com.maciej916.maessentials.common.command.BaseCommand;
import com.maciej916.maessentials.common.config.ModConfig;
import com.maciej916.maessentials.common.data.DataManager;
import com.maciej916.maessentials.common.lib.player.EssentialPlayer;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;

import static com.maciej916.maessentials.common.util.PlayerUtils.doSuicide;

public class PlayerSuicideCommand extends BaseCommand {

    public PlayerSuicideCommand(String command, int permissionLevel, boolean enabled) {
        super(command, permissionLevel, enabled);
    }

    @Override
    public LiteralArgumentBuilder<CommandSource> setExecution() {
        return builder.executes((context) -> execute(context.getSource()));
    }

    private int execute(CommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.asPlayer();
        EssentialPlayer eslPlayer = DataManager.getPlayer(player);

        long cooldown = eslPlayer.getUsage().getCommandCooldown("suicide", ModConfig.suicide_player_cooldown);
        if (cooldown != 0) {
            sendMessage(player, "maessentials.cooldown", cooldown);
            return Command.SINGLE_SUCCESS;
        }

        eslPlayer.getUsage().setCommandUsage("suicide");
        eslPlayer.saveData();

        doSuicide(player, player);

        return Command.SINGLE_SUCCESS;
    }

}
