package com.maciej916.maessentials.common.util;

import com.maciej916.maessentials.common.data.DataManager;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.world.GameType;
import net.minecraft.world.server.ServerWorld;

import java.util.Set;

public final class PlayerUtils {

    public static void doSuicide(ServerPlayerEntity player, ServerPlayerEntity target) {
        if (target.interactionManager.getGameType() == GameType.CREATIVE || target.interactionManager.getGameType() == GameType.SPECTATOR) {
            if (player == target) {
                TextUtils.sendMessage(target, "maessentials.invaild_gamemode");
            } else {
                TextUtils.sendMessage(target, "maessentials.invaild_gamemode.player", target.getDisplayName());
            }
            return;
        }

        target.attackEntityFrom(DamageSource.LIGHTNING_BOLT, target.getHealth());

        ServerWorld world = (ServerWorld) player.world;

//        LightningBoltEntity bolt = new LightningBoltEntity(EntityType.LIGHTNING_BOLT, world);
//        world.addEntity(bolt);

//        LightningBoltEntity entity = new LightningBoltEntity(world, player.getPosX(), player.getPosY(), player.getPosZ(), true);
//        world.addLightningBolt(entity);

        if (player != target) {
            TextUtils.sendMessage(player, "suicide.maessentials.player", target.getDisplayName());
            TextUtils.sendMessage(target, "suicide.maessentials.player.target", player.getDisplayName());
        }
    }

    public static void warpList(ServerPlayerEntity player) {
        Set<String> warps = DataManager.getWarp().getWarps().keySet();
        TextComponent warpList = new TranslationTextComponent("warp.maessentials.list", warps.size());

        if (warps.size() != 0) {
            int i = 1;
            for (String name : warps) {
                ClickEvent clickEvent = new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/warp " + name);
                HoverEvent eventHover = new HoverEvent(HoverEvent.Action.field_230550_a_, new TranslationTextComponent("warp.maessentials.list.warp", "/warp " + name));

                TextComponent kit = new TranslationTextComponent("warp.maessentials.list.warp", name);
                kit.getStyle().func_240715_a_(clickEvent);
                kit.getStyle().func_240716_a_(eventHover);

                warpList.func_230529_a_(kit);
                if (warps.size() != i) {
                    warpList.func_230529_a_(new StringTextComponent(", "));
                    i++;
                }
            }
        } else {
            warpList.func_230529_a_(new StringTextComponent("-"));
        }

        TextUtils.sendMessage(player, warpList);
    }



}
