package com.maciej916.maessentials.common.command.impl.time;

import com.maciej916.maessentials.common.command.BaseCommand;
import com.maciej916.maessentials.common.config.ModConfig;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.server.ServerWorld;

public class NightCommand extends BaseCommand {

    public NightCommand(String command, int permissionLevel, boolean enabled) {
        super(command, permissionLevel, enabled);
    }

    @Override
    public boolean isEnabled() {
        return ModConfig.time_enable;
    }

    @Override
    public LiteralArgumentBuilder<CommandSource> setExecution() {
        return builder.executes((context) -> execute(context.getSource()));
    }

    private int execute(CommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.asPlayer();

        for (ServerWorld serverworld : player.getServer().getWorlds()) {
            serverworld.func_241114_a_(20000);
        }

        sendMessage(player, "night.maessentials.success");
        return Command.SINGLE_SUCCESS;
    }

}
