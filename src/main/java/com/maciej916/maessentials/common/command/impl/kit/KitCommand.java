package com.maciej916.maessentials.common.command.impl.kit;

import com.maciej916.maessentials.common.command.BaseCommand;
import com.maciej916.maessentials.common.data.DataManager;
import com.maciej916.maessentials.common.lib.kit.Kit;
import com.maciej916.maessentials.common.lib.player.EssentialPlayer;
import com.maciej916.maessentials.common.util.ModUtils;
import com.maciej916.maessentials.common.util.TimeUtils;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;

public class KitCommand extends BaseCommand {

    public KitCommand(String command, int permissionLevel, boolean enabled) {
        super(command, permissionLevel, enabled);
    }

    @Override
    public LiteralArgumentBuilder<CommandSource> setExecution() {
        return builder.then(Commands.argument("kitName", StringArgumentType.string()).suggests(ModUtils.KIT_SUGGEST).executes(context -> execute(context.getSource(), StringArgumentType.getString(context, "kitName"))));
    }

    private int execute(CommandSource source, String kitName) throws CommandSyntaxException {
        ServerPlayerEntity player = source.asPlayer();
        EssentialPlayer eslPlayer = DataManager.getPlayer(player);

        Kit kit = DataManager.getKit().getKit(kitName);
        if (kit == null) {
            sendMessage(player, "kit.maessentials.not_exist", kitName);
            return Command.SINGLE_SUCCESS;
        }

        long cooldown = eslPlayer.getUsage().getKitCooldown(kitName, kit.getDuration());
        if (cooldown != 0) {
            String displayTime = TimeUtils.formatDate(cooldown);
            sendMessage(player, "kit.maessentials.wait", displayTime);
            return Command.SINGLE_SUCCESS;
        }

        if (ModUtils.giveKit(player, kit)) {
            eslPlayer.getUsage().setKitUssage(kitName);
            eslPlayer.saveData();
            player.world.playSound(player, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, ((player.getRNG().nextFloat() - player.getRNG().nextFloat()) * 0.7F + 1.0F) * 2.0F);
            sendMessage(player, "kit.maessentials.received", kitName);
        }

        return Command.SINGLE_SUCCESS;
    }

}
