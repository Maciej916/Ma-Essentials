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
import net.minecraft.util.text.TranslationTextComponent;

public class BroadcastCommand extends BaseCommand {

    public BroadcastCommand(String command, int permissionLevel, boolean enabled) {
        super(command, permissionLevel, enabled);
    }

    @Override
    public LiteralArgumentBuilder<CommandSource> setExecution() {
        return builder.then(Commands.argument("message", MessageArgument.message()).executes(context -> execute(context.getSource(), MessageArgument.getMessage(context, "message"))));
    }

    private int execute(CommandSource source, ITextComponent message) throws CommandSyntaxException {
        ServerPlayerEntity player = source.asPlayer();
        sendGlobalMessage(player.server.getPlayerList(), new TranslationTextComponent("broadcast.maessentials.success", message).getFormattedText());
        return Command.SINGLE_SUCCESS;
    }

}
