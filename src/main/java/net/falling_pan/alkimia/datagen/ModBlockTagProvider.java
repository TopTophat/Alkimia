package net.falling_pan.alkimia.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.falling_pan.alkimia.block.ModBlocks;
import net.falling_pan.alkimia.util.ModTags;
import net.minecraft.block.Blocks;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends FabricTagProvider.BlockTagProvider {
    public ModBlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE).add(ModBlocks.CINNABAR_BLOCK).add(ModBlocks.CINNABAR_ORE);
        getOrCreateTagBuilder(BlockTags.NEEDS_IRON_TOOL).add(ModBlocks.CINNABAR_BLOCK).add(ModBlocks.CINNABAR_ORE);
        getOrCreateTagBuilder(ModTags.Blocks.EVERFROST_TARGETS).add(Blocks.WATER).add(Blocks.SNOW).add(Blocks.LILY_PAD).add(Blocks.LAVA).add(Blocks.SNOW_BLOCK).add(Blocks.POWDER_SNOW).add(Blocks.ICE).add(Blocks.BLUE_ICE).add(Blocks.FROSTED_ICE).add(Blocks.PACKED_ICE).add(Blocks.SEAGRASS).add(Blocks.SEA_PICKLE).add(Blocks.TALL_SEAGRASS).add(Blocks.KELP);
    }
}
