/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.common.extensions;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Supplier;

import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityEventProvider;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.entity.PartEntity;
import net.minecraftforge.event.AttachCapabilitiesEvent;

public interface IForgeLevel extends ICapabilityProvider, ICapabilityEventProvider
{
    default Level self() {
        return (Level) this;
    }

    /**
     * The maximum radius to scan for entities when trying to check bounding boxes. Vanilla's default is
     * 2.0D But mods that add larger entities may increase this.
     */
    public double getMaxEntityRadius();
    /**
     * Increases the max entity radius, this is safe to call with any value.
     * The setter will verify the input value is larger then the current setting.
     *
     * @param value New max radius to set.
     * @return The new max radius
     */
    public double increaseMaxEntityRadius(double value);
    /**
     * All part entities in this world. Used when collecting entities in an AABB to fix parts being
     * ignored whose parent entity is in a chunk that does not intersect with the AABB.
     */
    public default Collection<PartEntity<?>> getPartEntities()
    {
        return Collections.emptyList();
    }

    @Override
    @SuppressWarnings("all")
    default <T> AttachCapabilitiesEvent<T> createAttachCapabilitiesEvent(T obj) {
        return new AttachCapabilitiesEvent.AttachLevelEvent<>((T) self());
    };
}
