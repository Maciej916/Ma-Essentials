package com.maciej916.maessentials.commands;

import com.maciej916.maessentials.libs.Methods;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.util.text.TextComponent;

public class CommandInvsee {

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("invsee").requires((source) -> source.hasPermissionLevel(4))
                .then(Commands.argument("targetPlayer", EntityArgument.players()).executes((context) -> invsee(context.getSource(), EntityArgument.getPlayer(context, "targetPlayer"))))
        );
    }

    private static int invsee(CommandSource source, ServerPlayerEntity target) throws CommandSyntaxException {
        ServerPlayerEntity player = source.asPlayer();
        if (player == target) {
            player.sendMessage(Methods.formatText("invsee.maessentials.self"));
        } else {
            TextComponent name = Methods.formatText("inv.maessentials.open", target.getDisplayName().getString(), "Inventory");
            player.openContainer(new SimpleNamedContainerProvider((id, inv, items) -> {
                return new ChestContainer(ContainerType.GENERIC_9X4, id, player.inventory, target.inventory, 4);
            }, name));
        }
        return Command.SINGLE_SUCCESS;
    }

}