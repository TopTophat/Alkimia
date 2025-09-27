package net.falling_pan.alkimia.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.falling_pan.alkimia.block.ModBlocks;
import net.falling_pan.alkimia.item.ModItems;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generate(RecipeExporter recipeExporter) {
        List<ItemConvertible> QUICKSILVER_SMELTABLES = List.of(ModItems.CINNABAR, ModBlocks.CINNABAR_ORE);

        offerSmelting(recipeExporter, QUICKSILVER_SMELTABLES, RecipeCategory.MISC, ModItems.QUICKSILVER, 0.1f, 100, "quicksilver");
        offerBlasting(recipeExporter, QUICKSILVER_SMELTABLES, RecipeCategory.MISC, ModItems.QUICKSILVER, 0.1f, 35, "quicksilver");

        offerReversibleCompactingRecipes(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModItems.CINNABAR, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CINNABAR_BLOCK);

        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, ModBlocks.QUICKSILVER_TANK)
                .pattern("QQQ")
                .pattern("QGQ")
                .pattern("QQQ")
                .input('Q', ModItems.QUICKSILVER)
                .input('G', Items.GLASS)
                .criterion(hasItem(ModItems.CINNABAR), conditionsFromItem(ModItems.CINNABAR))
                .offerTo(recipeExporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.QUICKSILVER, 8)
                .input(ModBlocks.QUICKSILVER_TANK)
                .criterion(hasItem(ModBlocks.QUICKSILVER_TANK), conditionsFromItem(ModBlocks.QUICKSILVER_TANK))
                .offerTo(recipeExporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.FLASH_POWDER)
                .pattern("   ")
                .pattern("CCC")
                .pattern("GBC")
                .input('C', Items.CHARCOAL)
                .input('G', Items.GUNPOWDER)
                .input('B', Items.BLAZE_POWDER)
                .criterion(hasItem(Items.BLAZE_POWDER), conditionsFromItem(Items.BLAZE_POWDER))
                .offerTo(recipeExporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModBlocks.MORTAR)
                .pattern("   ")
                .pattern("SIS")
                .pattern("SSS")
                .input('I', Items.IRON_INGOT)
                .input('S', Items.STONE)
                .criterion(hasItem(Items.IRON_INGOT), conditionsFromItem(Items.IRON_INGOT))
                .offerTo(recipeExporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.PESTLE)
                .pattern(" SS")
                .pattern(" SS")
                .pattern("S  ")
                .input('S', Items.STONE)
                .criterion(hasItem(Items.STONE), conditionsFromItem(Items.STONE))
                .offerTo(recipeExporter);
    }
}
