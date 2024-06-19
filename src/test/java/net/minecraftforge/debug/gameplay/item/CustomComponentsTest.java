/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.debug.gameplay.item;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.test.BaseTestMod;

@Mod(CustomComponentsTest.MODID)
public class CustomComponentsTest extends BaseTestMod {
    public static final String MODID = "custom_components_test";

    public static final DeferredRegister<DataComponentType<?>> COMPONENT_TYPES = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, MODID);
    public static final RegistryObject<DataComponentType<Integer>> PROTO = COMPONENT_TYPES.register("proto", () -> {
        return DataComponentType.<Integer>builder()
            .persistent(Codec.INT)
            .networkSynchronized(ByteBufCodecs.INT)
            .build();
    });

    public CustomComponentsTest() {
        MinecraftForge.EVENT_BUS.addListener(this::onJoin);
    }

    public void onJoin(EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof Player player) {
            var stack = Items.ACACIA_BUTTON.getDefaultInstance();
            stack.set(PROTO.get(), 1);
            player.addItem(stack);
        }
    }
}