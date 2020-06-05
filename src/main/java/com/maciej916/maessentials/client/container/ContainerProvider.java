package com.maciej916.maessentials.client.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.IContainerProvider;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ContainerProvider implements INamedContainerProvider {

    private final ITextComponent displayName;
    private final IContainerProvider provider;

    public ContainerProvider(String displayName, IContainerProvider provider) {
        this.displayName = new TranslationTextComponent(displayName);
        this.provider = provider;
    }

    @Nullable
    @Override
    public Container createMenu(int i, @Nonnull PlayerInventory inv, @Nonnull PlayerEntity player) {
        return provider.createMenu(i, inv, player);
    }

    @Nonnull
    @Override
    public ITextComponent getDisplayName() {
        return displayName;
    }

}
