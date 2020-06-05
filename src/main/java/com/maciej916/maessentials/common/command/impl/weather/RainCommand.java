package com.maciej916.maessentials.common.command.impl.weather;

import com.maciej916.maessentials.common.command.BaseCommand;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldInfo;

public class RainCommand extends BaseCommand {

    public RainCommand(String command, int permissionLevel, boolean enabled) {
        super(command, permissionLevel, enabled);
    }

    @Override
    public LiteralArgumentBuilder<CommandSource> setExecution() {
        return builder.executes((context) -> execute(context.getSource()));
    }

    private int execute(CommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.asPlayer();

        for (ServerWorld serverworld : source.getServer().getWorlds()) {
            WorldInfo worldData = serverworld.getWorldInfo();
            worldData.setRaining(true);
            worldData.setThundering(false);
            worldData.setClearWeatherTime(0);
            worldData.setRainTime(6000);
        }

        sendMessage(player, "rain.maessentials.success");
        return Command.SINGLE_SUCCESS;
    }

}
