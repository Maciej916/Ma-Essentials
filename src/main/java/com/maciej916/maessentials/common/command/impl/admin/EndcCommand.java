package com.maciej916.maessentials.common.command.impl.admin;

import com.maciej916.maessentials.client.container.ContainerProvider;
import com.maciej916.maessentials.client.container.impl.EndcContainer;
import com.maciej916.maessentials.client.container.impl.InvseeContainer;
import com.maciej916.maessentials.common.command.BaseCommand;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.network.NetworkHooks;

public class EndcCommand extends BaseCommand {

    public EndcCommand(String command, int permissionLevel, boolean enabled) {
        super(command, permissionLevel, enabled);
    }

    @Override
    public LiteralArgumentBuilder<CommandSource> setExecution() {
        return builder.executes((context) -> execute(context.getSource())).then(Commands.argument("targetPlayer", EntityArgument.players()).executes(context -> execute(context.getSource(), EntityArgument.getPlayer(context, "targetPlayer"))));
    }

    private int execute(CommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.asPlayer();
        doEndc(player, player);
        return Command.SINGLE_SUCCESS;
    }

    private int execute(CommandSource source, ServerPlayerEntity targetPlayer) throws CommandSyntaxException {
        ServerPlayerEntity player = source.asPlayer();
        doEndc(player, targetPlayer);
        return Command.SINGLE_SUCCESS;
    }

    private void doEndc(ServerPlayerEntity player, ServerPlayerEntity targetPlayer) {
        TextComponent endc = new TranslationTextComponent("container.enderchest");
        if (player != targetPlayer) {
            endc = new TranslationTextComponent("endc.maessentials.open", targetPlayer.getDisplayName().getString(), endc);
        }
        NetworkHooks.openGui(player, new ContainerProvider(
                endc.getFormattedText(), (i, inv, playerEntity) -> new EndcContainer(i, inv, targetPlayer)
        ), buf -> buf.writeUniqueId(targetPlayer.getUniqueID()));
    }

}
