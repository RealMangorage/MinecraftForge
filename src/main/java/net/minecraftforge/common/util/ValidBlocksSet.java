package net.minecraftforge.common.util;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.GatherValidBlocksEvent;

import java.util.Set;

public final class ValidBlocksSet {
    private final Set<Block> original;
    private volatile Set<Block> computed = null;

    public ValidBlocksSet(Set<Block> original) {
        this.original = original;
    }

    public boolean contains(Block block, BlockEntityType<?> type) {
        if (computed == null)
            computed = MinecraftForge.EVENT_BUS.fire(new GatherValidBlocksEvent(original, type)).compute();
        return original.contains(block) || computed.contains(block);
    }
}
