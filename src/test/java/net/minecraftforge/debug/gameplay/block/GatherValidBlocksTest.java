package net.minecraftforge.debug.gameplay.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FurnaceBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.GatherValidBlocksEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static net.minecraftforge.debug.gameplay.block.GatherValidBlocksTest.MODID;

@Mod(MODID)
public class GatherValidBlocksTest {
    public static final String MODID = "gather_valid_blocks_test";

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    public static final RegistryObject<FurnaceBlock> MY_FURNACE = BLOCKS.register("furnace", () -> new FurnaceBlock(BlockBehaviour.Properties.of()));

    public GatherValidBlocksTest(FMLJavaModLoadingContext context) {
        BLOCKS.register(context.getModEventBus());
        MinecraftForge.EVENT_BUS.addListener(this::valid);
    }

    public void valid(GatherValidBlocksEvent event) {
        if (event.isSame(BlockEntityType.FURNACE)) {
            event.addBlock(MY_FURNACE.get());
            RegistryObject
        }
    }
}
