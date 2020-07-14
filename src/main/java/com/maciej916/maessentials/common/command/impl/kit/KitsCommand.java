package com.maciej916.maessentials.common.command.impl.kit;

import com.maciej916.maessentials.common.command.BaseCommand;
import com.maciej916.maessentials.common.data.DataManager;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

import java.util.Set;

public class KitsCommand extends BaseCommand {

    public KitsCommand(String command, int permissionLevel, boolean enabled) {
        super(command, permissionLevel, enabled);
    }

    @Override
    public LiteralArgumentBuilder<CommandSource> setExecution() {
        return builder.executes((context) -> execute(context.getSource()));
    }

    private int execute(CommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.asPlayer();
        Set<String> kits = DataManager.getKit().getKits().keySet();

        TextComponent kitList = new TranslationTextComponent("kit.maessentials.list");
        if (kits.size() != 0) {
            int i = 1;
            for (String name : kits) {
                ClickEvent clickEvent = new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/kit " + name);
                HoverEvent eventHover = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslationTextComponent("kit.maessentials.list.kit", "/kit " + name));

                TextComponent kit = new TranslationTextComponent("kit.maessentials.list.kit", name);
                kit.getStyle().setClickEvent(clickEvent);
                kit.getStyle().setHoverEvent(eventHover);

                kitList.func_230529_a_(kit);
                if (kits.size() != i) {
                    kitList.func_230529_a_(new StringTextComponent(", "));
                    i++;
                }
            }
        } else {
            kitList.func_230529_a_(new StringTextComponent("-"));
        }

        sendMessage(player, kitList);
        return Command.SINGLE_SUCCESS;
    }

}
