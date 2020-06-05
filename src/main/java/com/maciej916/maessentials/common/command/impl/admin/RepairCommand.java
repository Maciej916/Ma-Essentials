package com.maciej916.maessentials.common.command.impl.admin;

import com.maciej916.maessentials.common.command.BaseCommand;
import com.maciej916.maessentials.common.lib.Location;
import com.maciej916.maessentials.common.util.TeleportUtils;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.block.material.Material;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.server.ServerWorld;

public class RepairCommand extends BaseCommand {

    public RepairCommand(String command, int permissionLevel, boolean enabled) {
        super(command, permissionLevel, enabled);
    }

    @Override
    public LiteralArgumentBuilder<CommandSource> setExecution() {
        return builder.executes((context) -> execute(context.getSource()));
    }

    private int execute(CommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.asPlayer();

        ItemStack heldItem = player.getHeldItem(Hand.MAIN_HAND);
        int damage = heldItem.getDamage();
        int maxDamage = heldItem.getMaxDamage();
        if (damage == maxDamage || !heldItem.isDamaged()) {
            sendMessage(player, "repair.maessentials.error");
        } else {
            heldItem.setDamage(0);
            sendMessage(player, "repair.maessentials.done");
        }

        return Command.SINGLE_SUCCESS;
    }

}
