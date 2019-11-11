package com.maciej916.maessentials.commands;

import com.maciej916.maessentials.libs.Methods;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.util.text.TextComponent;

import static net.minecraft.block.EnderChestBlock.field_220115_d;

public class CommandEndc {

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder<CommandSource> builder = Commands.literal("endc").requires(source -> source.hasPermissionLevel(4));
        builder
                .executes(context -> endc(context))
                .then(Commands.argument("targetPlayer", EntityArgument.players())
                        .executes(context -> endcArgs(context)));

        dispatcher.register(builder);
    }

    private static int endc(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        doEndc(player, player);
        return Command.SINGLE_SUCCESS;
    }

    private static int endcArgs(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        ServerPlayerEntity targetPlayer = EntityArgument.getPlayer(context, "targetPlayer");
        doEndc(player, targetPlayer);
        return Command.SINGLE_SUCCESS;
    }

    private static void doEndc(ServerPlayerEntity player, ServerPlayerEntity target) {
        TextComponent endc = field_220115_d;
        if (player != target) {
            endc = Methods.formatText("endc.maessentials.open", target.getDisplayName().getString(), endc);
        }

        player.openContainer(new SimpleNamedContainerProvider((id, inv, items) -> {
            return ChestContainer.createGeneric9X3(id, inv, target.getInventoryEnderChest());
        }, endc));
    }
}