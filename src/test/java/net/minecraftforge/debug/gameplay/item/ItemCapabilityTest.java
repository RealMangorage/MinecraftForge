/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.debug.gameplay.item;

import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.test.BaseTestMod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.function.Consumer;

@GameTestHolder("forge." + ItemCapabilityTest.MOD_ID)
@Mod(ItemCapabilityTest.MOD_ID)
public class ItemCapabilityTest extends BaseTestMod {
    public static final String MOD_ID = "item_caps_test";

    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENTS = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, MOD_ID);
    public static final RegistryObject<DataComponentType<Integer>> STORAGE = DATA_COMPONENTS.register("energy_storage", () ->
            DataComponentType.<Integer>builder()
                    .persistent(ExtraCodecs.POSITIVE_INT)
                    .networkSynchronized(ByteBufCodecs.VAR_INT)
                    .build()
    );

    public ItemCapabilityTest(FMLJavaModLoadingContext context) {
        super(context);

        MinecraftForge.EVENT_BUS.addGenericListener(ItemStack.class, this::onEvent);
        MinecraftForge.EVENT_BUS.addListener(this::tooltipEvent);
        MinecraftForge.EVENT_BUS.addListener(this::onTick);

    }

    public void onTick(TickEvent.PlayerTickEvent event) {
        if (event.player.level().isClientSide) return;
        event.player.getMainHandItem().getCapability(ForgeCapabilities.ENERGY).ifPresent(s -> {
            s.receiveEnergy(1, false);
        });
    }

    public void onEvent(AttachCapabilitiesEvent<ItemStack> event) {
        event.addCapability(
                ResourceLocation.fromNamespaceAndPath("forge", "test"),
                new MyProvider(event.getObject())
        );
    }

    public void tooltipEvent(ItemTooltipEvent event) {
        var item = event.getItemStack();
        item.getCapability(ForgeCapabilities.ENERGY).ifPresent(storage -> {
            event.getToolTip().add(Component.literal("Energy: " + storage.getEnergyStored()));
        });
    }

    public static final class MyEnergyStorage extends EnergyStorage {

        private final Consumer<EnergyStorage> consumer;

        public MyEnergyStorage(int capacity, Consumer<EnergyStorage> consumer) {
            super(capacity);
            this.consumer = consumer;
        }

        public MyEnergyStorage(int capacity, int maxTransfer, Consumer<EnergyStorage> consumer) {
            super(capacity, maxTransfer);
            this.consumer = consumer;
        }

        public MyEnergyStorage(int capacity, int maxReceive, int maxExtract, Consumer<EnergyStorage> consumer) {
            super(capacity, maxReceive, maxExtract);
            this.consumer = consumer;
        }

        public MyEnergyStorage(int capacity, int maxReceive, int maxExtract, int energy, Consumer<EnergyStorage> consumer) {
            super(capacity, maxReceive, maxExtract, energy);
            this.consumer = consumer;
        }

        static <T, V> T doAndReturn(T value, V value2, Consumer<V> consumer) {
            consumer.accept(value2);
            return value;
        }

        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            return doAndReturn(super.receiveEnergy(maxReceive, simulate), this, consumer);
        }

        @Override
        public int extractEnergy(int maxExtract, boolean simulate) {
            return doAndReturn(super.extractEnergy(maxExtract, simulate), this, consumer);
        }
    }

    public static final class MyProvider implements ICapabilityProvider {
        private final ItemStack stack;
        private final EnergyStorage storage;
        private final LazyOptional<EnergyStorage> storageLazyOptional;

        public MyProvider(ItemStack stack) {
            this.stack = stack;
            this.storage = new MyEnergyStorage(1000, 10, 10, stack.getOrDefault(STORAGE.get(), 10), storage -> {
                stack.set(STORAGE.get(), storage.getEnergyStored());
            });
            this.storageLazyOptional = LazyOptional.of(() -> storage);
        }

        @Override
        public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
            if (cap == ForgeCapabilities.ENERGY) {
                return storageLazyOptional.cast();
            }
            return LazyOptional.empty();
        }
    }
}
