package net.toptophat.alkimia.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.toptophat.alkimia.block.ModBlocks;
import net.toptophat.alkimia.item.ModItems;
import net.minecraft.data.client.*;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.CINNABAR_BLOCK);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.DEAD_PLAGUE);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.PLAGUE);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.COLD_BLOCK);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.CINNABAR_ORE);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.QUICKSILVER_TANK);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.DEVOURER);
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(ModItems.CINNABAR, Models.GENERATED);
        itemModelGenerator.register(ModItems.QUICKSILVER, Models.GENERATED);
        itemModelGenerator.register(ModItems.LIQUID_SUNLIGHT, Models.GENERATED);
        itemModelGenerator.register(ModItems.FLASH_POWDER, Models.GENERATED);
        itemModelGenerator.register(ModItems.PESTLE, Models.GENERATED);
        itemModelGenerator.register(ModItems.GLOW_FULL, Models.GENERATED);
        itemModelGenerator.register(ModItems.GLOW_HALF, Models.GENERATED);
        itemModelGenerator.register(ModItems.SENSES_FULL, Models.GENERATED);
        itemModelGenerator.register(ModItems.SENSES_HALF, Models.GENERATED);
        itemModelGenerator.register(ModItems.TRANSPARENCY_FULL, Models.GENERATED);
        itemModelGenerator.register(ModItems.TRANSPARENCY_HALF, Models.GENERATED);
        itemModelGenerator.register(ModItems.LIQUID_FULL, Models.GENERATED);
        itemModelGenerator.register(ModItems.LIQUID_HALF, Models.GENERATED);
        itemModelGenerator.register(ModItems.DEATH_FULL, Models.GENERATED);
        itemModelGenerator.register(ModItems.DEATH_HALF, Models.GENERATED);
        itemModelGenerator.register(ModItems.METAL_FULL, Models.GENERATED);
        itemModelGenerator.register(ModItems.METAL_HALF, Models.GENERATED);
        itemModelGenerator.register(ModItems.DIAMOND_DUST_PILE, Models.GENERATED);
        itemModelGenerator.register(ModItems.DIAMOND_DUST_BUCKET, Models.GENERATED);
        itemModelGenerator.register(ModItems.SOUL_BUCKET, Models.GENERATED);
        itemModelGenerator.register(ModItems.LIQUID_SOUL_BUCKET, Models.GENERATED);
        itemModelGenerator.register(ModItems.GUIDEBOOK, Models.GENERATED);
    }
}
