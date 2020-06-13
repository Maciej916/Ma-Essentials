package com.maciej916.maessentials.common.subscribe.events;

import com.maciej916.maessentials.MaEssentials;
import com.maciej916.maessentials.common.config.ModConfig;
import com.maciej916.maessentials.common.data.DataManager;
import com.maciej916.maessentials.common.lib.Location;
import com.maciej916.maessentials.common.util.TeleportUtils;
import net.minecraft.block.MovingPistonBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MaEssentials.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EventRightClick {

    @SubscribeEvent
    public static void onRightClick(PlayerInteractEvent.RightClickItem event) {
        PlayerEntity player = event.getPlayer();
        Item heldItem = event.getItemStack().getItem();
        if (player.world.isRemote) {
            Vec3d vec3 = player.getPositionVector();
            Vec3d vec3a = player.getLook(1.0F);
            Vec3d vec3b = vec3.add(vec3a.getX() * 64, vec3a.getY() * 64, vec3a.getZ() * 64);

            if (heldItem == Items.COMPASS) {
                // TODO
            }

//            System.out.println(event.getItemStack());
//            System.out.println(vec3b.getX());
//            System.out.println(vec3b.getY());
//            System.out.println(vec3b.getZ());

        }
    }
}
