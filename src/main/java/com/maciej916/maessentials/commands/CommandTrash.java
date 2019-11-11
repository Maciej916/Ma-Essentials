package com.maciej916.maessentials.commands;

import com.maciej916.maessentials.libs.Methods;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;

public class CommandTrash {

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder<CommandSource> builder = Commands.literal("trash").requires(source -> source.hasPermissionLevel(0));
        builder.executes(context -> trash(context));
        dispatcher.register(builder);
    }

    private static int trash(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();

        player.openContainer(new SimpleNamedContainerProvider((id, inv, items) -> {
            return ChestContainer.createGeneric9X4(id, inv);
        }, Methods.formatText("trash.maessentials.open")));

        return Command.SINGLE_SUCCESS;
    }
}