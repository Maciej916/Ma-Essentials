package com.maciej916.maessentials.common.command.impl.admin;

import com.maciej916.maessentials.common.command.BaseCommand;
import com.maciej916.maessentials.common.lib.Location;
import com.maciej916.maessentials.common.util.TeleportUtils;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.server.ServerWorld;

public class HatCommand extends BaseCommand {

    public HatCommand(String command, int permissionLevel, boolean enabled) {
        super(command, permissionLevel, enabled);
    }

    @Override
    public LiteralArgumentBuilder<CommandSource> setExecution() {
        return builder.executes((context) -> execute(context.getSource()));
    }

    private int execute(CommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.asPlayer();

        ItemStack helmetItem = player.inventory.armorInventory.get(3).copy();
        ItemStack handItem = player.getHeldItem(Hand.MAIN_HAND);

        if (handItem.getItem() == Items.AIR) {
            sendMessage(player, "hat.maessentials.error");
            return Command.SINGLE_SUCCESS;
        }

        player.inventory.setInventorySlotContents(39, handItem);
        for (int i = 0; i < 9; ++i) {
           if (handItem.equals(player.inventory.getStackInSlot(i))) {
               player.inventory.setInventorySlotContents(i, helmetItem);
               break;
           }
        }

        sendMessage(player, "hat.maessentials.success");

        return Command.SINGLE_SUCCESS;
    }

}
