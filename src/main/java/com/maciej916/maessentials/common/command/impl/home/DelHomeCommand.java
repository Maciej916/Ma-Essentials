package com.maciej916.maessentials.common.command.impl.home;

import com.maciej916.maessentials.common.command.BaseCommand;
import com.maciej916.maessentials.common.data.DataManager;
import com.maciej916.maessentials.common.lib.player.EssentialPlayer;
import com.maciej916.maessentials.common.util.ModUtils;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;

public class DelHomeCommand extends BaseCommand {

    public DelHomeCommand(String command, int permissionLevel, boolean enabled) {
        super(command, permissionLevel, enabled);
    }

    @Override
    public LiteralArgumentBuilder<CommandSource> setExecution() {
        return builder.executes((context) -> execute(context.getSource())).then(Commands.argument("homeName", StringArgumentType.string()).suggests(ModUtils.HOME_SUGGEST).executes((context) -> execute(context.getSource(), StringArgumentType.getString(context, "homeName"))));
    }

    private int execute(CommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.asPlayer();
        sendMessage(player, "home.maessentials.specify_name");
        return Command.SINGLE_SUCCESS;
    }

    private int execute(CommandSource source, String homeName) throws CommandSyntaxException {
        ServerPlayerEntity player = source.asPlayer();
        EssentialPlayer eslPlayer = DataManager.getPlayer(player);

        if (eslPlayer.getHomeData().getHome(homeName) == null) {
            sendMessage(player, "home.maessentials.not_exist", homeName);
            return Command.SINGLE_SUCCESS;
        }

        eslPlayer.getHomeData().delHome(homeName);
        eslPlayer.saveHomes();

        sendMessage(player, "delhome.maessentials.done", homeName);

        return Command.SINGLE_SUCCESS;
    }

}
