
package com.maciej916.maessentials.common.util;

import com.maciej916.maessentials.common.lib.kit.Kit;
import com.maciej916.maessentials.common.data.DataManager;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.command.CommandSource;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;

import java.util.ArrayList;
import java.util.Optional;

import static com.maciej916.maessentials.MaEssentials.MODID;

public final class ModUtils {

    public static final SuggestionProvider<CommandSource> HOME_SUGGEST = (context, builder) -> ISuggestionProvider.suggest(DataManager.getPlayer(context.getSource().asPlayer()).getHomeData().getHomes().keySet().toArray(new String[0]), builder);

    public static final SuggestionProvider<CommandSource> WARP_SUGGEST = (context, builder) -> ISuggestionProvider.suggest(DataManager.getWarp().getWarps().keySet().toArray(new String[0]), builder);

    public static final SuggestionProvider<CommandSource> KIT_SUGGEST = (context, builder) -> ISuggestionProvider.suggest(DataManager.getKit().getKits().keySet().toArray(new String[0]), builder);

    private static String getVersion() {
        Optional<? extends ModContainer> o = ModList.get().getModContainerById(MODID);
        if (o.isPresent()) {
            return o.get().getModInfo().getVersion().toString();
        }
        return "NONE";
    }

    public static boolean isDev() {
        String version = getVersion();
        return version.equals("NONE");
    }

    public static boolean giveKit(ServerPlayerEntity player, Kit kit) {
        try {
            ArrayList<ItemStack> items = kit.getItems();
            for (ItemStack item : items) {
                player.inventory.addItemStackToInventory(item);
            }
            return true;
        } catch (Exception e) {
            TextUtils.sendChatMessage(player, "kit.maessentials.parse_error");
            return false;
        }
    }

    public static void kickPlayer(ServerPlayerEntity player, TranslationTextComponent op, String reason) {
        TextUtils.sendGlobalChatMessage(player.server.getPlayerList(), "kick.maessentials.done", player.getDisplayName(), op, reason);
        player.connection.disconnect(new StringTextComponent(reason));
    }
}