package net.falling_pan.alkimia.block.custom;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.falling_pan.alkimia.fluid.ModFluids;
import net.falling_pan.alkimia.item.ModItems;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

import java.util.Map;

public class FluidMaps {
    public static Map<Item, Integer> SIMPLE_ITEM_TO_AMOUNT = Map.of();

    public static Map<Item, FluidVariant> SIMPLE_ITEM_TO_FLUID = Map.of();

    public static Map<Item, Item> SIMPLE_ITEM_TO_REMAINDER = Map.of();

    public static void InitMaps()
    {
        SIMPLE_ITEM_TO_AMOUNT =
                Map.of(
                        ModItems.DIAMOND_DUST_PILE, 100,
                        Items.SOUL_SAND, 50,
                        Items.GHAST_TEAR, 25,
                        ModItems.DIAMOND_DUST_BUCKET, 1000,
                        ModItems.SOUL_BUCKET, 1000,
                        ModItems.LIQUID_SOUL_BUCKET, 1000,
                        Items.LAVA_BUCKET, 1000,
                        Items.WATER_BUCKET, 1000
                );
        SIMPLE_ITEM_TO_FLUID =
                Map.of(
                        ModItems.DIAMOND_DUST_PILE, FluidVariant.of(ModFluids.DIAMOND_DUST),
                        ModItems.DIAMOND_DUST_BUCKET, FluidVariant.of(ModFluids.DIAMOND_DUST),
                        ModItems.SOUL_BUCKET, FluidVariant.of(ModFluids.SOUL),
                        ModItems.LIQUID_SOUL_BUCKET, FluidVariant.of(ModFluids.LIQUID_SOUL),
                        Items.LAVA_BUCKET, FluidVariant.of(Fluids.LAVA),
                        Items.WATER_BUCKET, FluidVariant.of(Fluids.WATER)
                );

        SIMPLE_ITEM_TO_REMAINDER =
                Map.of(
                        ModItems.DIAMOND_DUST_BUCKET, Items.BUCKET,
                        ModItems.SOUL_BUCKET, Items.BUCKET,
                        ModItems.LIQUID_SOUL_BUCKET, Items.BUCKET,
                        Items.LAVA_BUCKET, Items.BUCKET,
                        Items.WATER_BUCKET, Items.BUCKET
                );
    }
}
