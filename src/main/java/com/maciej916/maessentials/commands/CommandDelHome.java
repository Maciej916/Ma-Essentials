package com.maciej916.maessentials.commands;

import com.maciej916.maessentials.classes.player.EssentialPlayer;
import com.maciej916.maessentials.data.DataManager;
import com.maciej916.maessentials.libs.Methods;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;

public class CommandDelHome {
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder<CommandSource> builder = Commands.literal("delhome").requires(source -> source.hasPermissionLevel(0));
        builder
                .executes(context -> delHome(context))
                    .then(Commands.argument("homeName", StringArgumentType.string())
                            .suggests(Methods.HOME_SUGGEST)
                            .executes(context -> delHomeArgs(context)));
        dispatcher.register(builder);
    }

    private static int delHome(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        player.sendMessage(Methods.formatText("delhome.maessentials.specify_name"));
        return Command.SINGLE_SUCCESS;
    }

    private static int delHomeArgs(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        EssentialPlayer eslPlayer = DataManager.getPlayer(player);
        String name = StringArgumentType.getString(context, "homeName").toLowerCase();

        if (eslPlayer.getHomeData().getHome(name) == null) {
            player.sendMessage(Methods.formatText("home.maessentials.not_exist", name));
            return Command.SINGLE_SUCCESS;
        }

        eslPlayer.getHomeData().delHome(name);
        eslPlayer.saveHomes();
        player.sendMessage(Methods.formatText("delhome.maessentials.done", name));

        return Command.SINGLE_SUCCESS;
    }
}