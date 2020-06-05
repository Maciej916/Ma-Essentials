package com.maciej916.maessentials.common.command.impl.admin;

import com.maciej916.maessentials.common.command.BaseCommand;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.MessageArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class KickallCommand extends BaseCommand {

    public KickallCommand(String command, int permissionLevel, boolean enabled) {
        super(command, permissionLevel, enabled);
    }

    @Override
    public LiteralArgumentBuilder<CommandSource> setExecution() {
        return builder.executes((context) -> execute(context.getSource())).then(Commands.argument("reason", MessageArgument.message()).executes(context -> execute(context.getSource(), MessageArgument.getMessage(context, "reason"))));
    }

    private int execute(CommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.asPlayer();
        doKickall(player, new StringTextComponent("No reason provided"));
        return Command.SINGLE_SUCCESS;
    }

    private int execute(CommandSource source, ITextComponent reason) throws CommandSyntaxException {
        ServerPlayerEntity player = source.asPlayer();
        doKickall(player, reason);
        return Command.SINGLE_SUCCESS;
    }

    private void doKickall(ServerPlayerEntity player, ITextComponent reason) {
        for (ServerPlayerEntity tp : player.server.getPlayerList().getPlayers()) {
            if (player.getUniqueID() != tp.getUniqueID()) {
                tp.connection.disconnect(reason);
            }
        }
        sendMessage(player, "kickall.maessentials.success");
    }

}
