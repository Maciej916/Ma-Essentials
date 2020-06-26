package com.maciej916.maessentials.common.command.impl.teleport_request;

import com.maciej916.maessentials.common.command.BaseCommand;
import com.maciej916.maessentials.common.config.ModConfig;
import com.maciej916.maessentials.common.data.DataManager;
import com.maciej916.maessentials.common.lib.player.EssentialPlayer;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

import static com.maciej916.maessentials.common.util.TeleportUtils.requestTeleport;

public class TpaCommand extends BaseCommand {

    public TpaCommand(String command, int permissionLevel, boolean enabled) {
        super(command, permissionLevel, enabled);
    }

    @Override
    public LiteralArgumentBuilder<CommandSource> setExecution() {
        return builder.then(Commands.argument("targetPlayer", EntityArgument.players()).executes(context -> execute(context.getSource(), EntityArgument.getPlayer(context, "targetPlayer"))));
    }

    private int execute(CommandSource source, ServerPlayerEntity targetPlayer) throws CommandSyntaxException {
        ServerPlayerEntity player = source.asPlayer();
        EssentialPlayer eslPlayer = DataManager.getPlayer(player);

        if (player == targetPlayer) {
            sendMessage(player, "tpa.maessentials.self");
            return Command.SINGLE_SUCCESS;
        }

        long cooldown = eslPlayer.getUsage().getTeleportCooldown("tpa", ModConfig.tpa_cooldown);
        if (cooldown != 0) {
            sendMessage(player, "maessentials.cooldown.teleport", cooldown);
            return Command.SINGLE_SUCCESS;
        }

        eslPlayer.getUsage().setCommandUsage("tpa");
        eslPlayer.saveData();

        if (requestTeleport(player, player, targetPlayer, ModConfig.tpa_timeout)) {
            sendMessage(player, "tpa.maessentials.request", targetPlayer.getDisplayName());
            sendMessage(targetPlayer, "tpa.maessentials.request.target", player.getDisplayName());

            ClickEvent clickEventAccept = new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tpaccept " + player.getDisplayName().getString());
            HoverEvent eventHoverAccept = new HoverEvent(HoverEvent.Action.field_230550_a_, new TranslationTextComponent("tpa.maessentials.request.target.accept.hover", "/tpaccept " + player.getDisplayName().getString()));
            TextComponent textAccept = new StringTextComponent("/tpaccept");
            textAccept.getStyle().func_240715_a_(clickEventAccept);
            textAccept.getStyle().func_240716_a_(eventHoverAccept);
            sendMessage(targetPlayer, "tpa.maessentials.request.target.accept", textAccept);

            ClickEvent clickEventDeny = new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tpdeny " + player.getDisplayName().getString());
            HoverEvent eventHoverDeny = new HoverEvent(HoverEvent.Action.field_230550_a_, new TranslationTextComponent("tpa.maessentials.request.target.deny.hover", "/tpdeny " + player.getDisplayName().getString()));
            TextComponent textDeny = new StringTextComponent("/tpdeny");
            textDeny.getStyle().func_240715_a_(clickEventDeny);
            textDeny.getStyle().func_240716_a_(eventHoverDeny);
            sendMessage(targetPlayer, "tpa.maessentials.request.target.deny", textDeny);
        }

        return Command.SINGLE_SUCCESS;
    }

}
