package com.maciej916.maessentials.common.command.impl.home;

import com.maciej916.maessentials.common.command.BaseCommand;
import com.maciej916.maessentials.common.config.ModConfig;
import com.maciej916.maessentials.common.data.DataManager;
import com.maciej916.maessentials.common.lib.Location;
import com.maciej916.maessentials.common.lib.player.EssentialPlayer;
import com.maciej916.maessentials.common.util.ModUtils;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;

public class SetHomeCommand extends BaseCommand {

    public SetHomeCommand(String command, int permissionLevel, boolean enabled) {
        super(command, permissionLevel, enabled);
    }

    @Override
    public LiteralArgumentBuilder<CommandSource> setExecution() {
        return builder.executes((context) -> execute(context.getSource())).then(Commands.argument("homeName", StringArgumentType.string()).executes((context) -> execute(context.getSource(), StringArgumentType.getString(context, "homeName"))));
    }

    private int execute(CommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.asPlayer();
        doSetHome(player, "home");
        return Command.SINGLE_SUCCESS;
    }

    private int execute(CommandSource source, String homeName) throws CommandSyntaxException {
        ServerPlayerEntity player = source.asPlayer();
        doSetHome(player, homeName);
        return Command.SINGLE_SUCCESS;
    }

    private void doSetHome(ServerPlayerEntity player, String name) {
        EssentialPlayer eslPlayer = DataManager.getPlayer(player);

        int homes_limit = ModConfig.homes_limit;
        if (player.hasPermissionLevel(1)) {
            homes_limit = ModConfig.homes_limit_op;
        }

        if ((eslPlayer.getHomeData().getHomes().size() < homes_limit)  || (eslPlayer.getHomeData().getHomes().size() == homes_limit && eslPlayer.getHomeData().getHome(name) != null)) {
            eslPlayer.getHomeData().setHome(name, new Location(player));
            eslPlayer.saveHomes();
            sendMessage(player, "sethome.maessentials.done", name);
        } else {
            sendMessage(player, "sethome.maessentials.max_homes", homes_limit);
        }
    }

}
