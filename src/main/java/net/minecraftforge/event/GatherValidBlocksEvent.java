package net.minecraftforge.event;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.Event;

import java.util.HashSet;
import java.util.Set;

public class GatherValidBlocksEvent extends Event {
    private final Set<Block> original;
    private final BlockEntityType<?> blockEntityType;
    private final Set<Block> modded = new HashSet<>();

    public GatherValidBlocksEvent(Set<Block> original, BlockEntityType<?> type) {
        this.original = original;
        this.blockEntityType = type;
    }

    public void addBlock(Block block) {
        if (original.contains(block) || modded.contains(block)) return;
        modded.add(block);
    }

    public boolean isSame(BlockEntityType<?> type) {
        return type == blockEntityType;
    }

    public Set<Block> compute() {
        return Set.copyOf(modded);
    }
}
