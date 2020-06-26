package com.maciej916.maessentials.common.command.impl.admin;

import com.maciej916.maessentials.client.container.ContainerProvider;
import com.maciej916.maessentials.client.container.impl.InvseeContainer;
import com.maciej916.maessentials.common.command.BaseCommand;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.network.NetworkHooks;

public class InvseeCommand extends BaseCommand {

    public InvseeCommand(String command, int permissionLevel, boolean enabled) {
        super(command, permissionLevel, enabled);
    }

    @Override
    public LiteralArgumentBuilder<CommandSource> setExecution() {
        return builder.then(Commands.argument("targetPlayer", EntityArgument.players()).executes((context) -> execute(context.getSource(), EntityArgument.getPlayer(context, "targetPlayer"))));
    }

    private int execute(CommandSource source, ServerPlayerEntity targetPlayer) throws CommandSyntaxException {
        ServerPlayerEntity player = source.asPlayer();
        if (player == targetPlayer) {
            sendMessage(player, "invsee.maessentials.self");
        } else {
            NetworkHooks.openGui(player, new ContainerProvider(
                    new TranslationTextComponent("inv.maessentials.open", targetPlayer.getDisplayName().getString()).getString(), (i, inv, playerEntity) -> new InvseeContainer(i, inv, targetPlayer)
            ), buf -> buf.writeUniqueId(targetPlayer.getUniqueID()));
        }
        return Command.SINGLE_SUCCESS;
    }

}
