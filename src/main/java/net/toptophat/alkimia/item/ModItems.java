package net.toptophat.alkimia.item;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.toptophat.alkimia.Alkimia;
import net.toptophat.alkimia.component.ModDataComponentTypes;
import net.toptophat.alkimia.fluid.ModFluids;
import net.toptophat.alkimia.item.custom.*;
import net.minecraft.item.*;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;

public class ModItems {
    public static final Item CINNABAR = registerItem("cinnabar", new Item(new Item.Settings())
    {
        @Override
        public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
            tooltip.add(Text.translatable("tooltip.alkimia.cinnabar"));
            super.appendTooltip(stack, context, tooltip, type);
        }
    });
    public static final Item QUICKSILVER = registerItem("quicksilver", new Item(new Item.Settings()));

    public static final Item PESTLE = registerItem("pestle", new Item(new Item.Settings().maxCount(1)));

    public static final Item FLASH_POWDER = registerItem("flash_powder", new FlashPowderItem(new Item.Settings().fireproof()));
    public static final Item GUIDEBOOK = registerItem("guidebook", new GuidebookItem(new Item.Settings().maxCount(1).component(ModDataComponentTypes.PAGE, 0)));

    public static final Item NIGHT_VISION_POTION = registerItem("night_vision_potion", new NightVisionPotionItem(new Item.Settings().food(ModFoodComponents.NIGHT_VISION_POTION).maxCount(12)));

    public static final Item LIQUID_SUNLIGHT = registerItem("liquid_sunlight", new Item(new Item.Settings().recipeRemainder(Items.GLASS_BOTTLE))
    {
        @Override
        public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
            tooltip.add(Text.translatable("tooltip.alkimia.liquid_sunlight"));
            super.appendTooltip(stack, context, tooltip, type);
        }
    });
    public static final Item GLOW_FULL = registerItem("glow_full", new Item(new Item.Settings()));
    public static final Item GLOW_HALF = registerItem("glow_half", new Item(new Item.Settings()));
    public static final Item SENSES_FULL = registerItem("senses_full", new Item(new Item.Settings()));
    public static final Item SENSES_HALF = registerItem("senses_half", new Item(new Item.Settings()));
    public static final Item TRANSPARENCY_FULL = registerItem("transparency_full", new Item(new Item.Settings()));
    public static final Item TRANSPARENCY_HALF = registerItem("transparency_half", new Item(new Item.Settings()));
    public static final Item LIQUID_FULL = registerItem("liquid_full", new Item(new Item.Settings()));
    public static final Item LIQUID_HALF = registerItem("liquid_half", new Item(new Item.Settings()));
    public static final Item DEATH_FULL = registerItem("death_full", new Item(new Item.Settings()));
    public static final Item DEATH_HALF = registerItem("death_half", new Item(new Item.Settings()));
    public static final Item METAL_FULL = registerItem("metal_full", new Item(new Item.Settings()));
    public static final Item METAL_HALF = registerItem("metal_half", new Item(new Item.Settings()));
    public static final Item DIAMOND_DUST_PILE = registerItem("diamond_dust_pile", new Item(new Item.Settings()));
    public static final Item DIAMOND_DUST_BUCKET = registerItem("diamond_dust_bucket", new BucketItem(ModFluids.DIAMOND_DUST, new Item.Settings().recipeRemainder(Items.BUCKET).maxCount(1)));
    public static final Item SOUL_BUCKET = registerItem("soul_bucket", new BucketItem(ModFluids.SOUL, new Item.Settings().recipeRemainder(Items.BUCKET).maxCount(1)));
    public static final Item LIQUID_SOUL_BUCKET = registerItem("liquid_soul_bucket", new BucketItem(ModFluids.LIQUID_SOUL, new Item.Settings().recipeRemainder(Items.BUCKET).maxCount(1)));
    public static final Item VIAL = registerItem("vial", new VialItem(new Item.Settings().maxCount(1).component(ModDataComponentTypes.AMOUNT, 0).component(ModDataComponentTypes.CAPACITY, 100).component(ModDataComponentTypes.STORED_FLUID, FluidVariant.blank())));
    public static final Item TINY_VIAL = registerItem("tiny_vial", new TinyVialItem(new Item.Settings().maxCount(1).component(ModDataComponentTypes.AMOUNT, 0).component(ModDataComponentTypes.CAPACITY, 50).component(ModDataComponentTypes.STORED_FLUID, FluidVariant.blank())));
    public static final Item FLASK = registerItem("flask", new FlaskItem(new Item.Settings().maxCount(1).component(ModDataComponentTypes.AMOUNT, 0).component(ModDataComponentTypes.CAPACITY, 1000).component(ModDataComponentTypes.STORED_FLUID, FluidVariant.blank())));
    public static final Item SUNLIGHT_COLLECTOR = registerItem("sunlight_collector", new Item(new Item.Settings().maxCount(1).component(ModDataComponentTypes.LIGHT_AMOUNT, 10).component(ModDataComponentTypes.LIGHT_CAPACITY, 1000))
    {
        @Override
        public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
            tooltip.add(Text.translatable("tooltip.alkimia.light_collector_crystal", stack.get(ModDataComponentTypes.LIGHT_AMOUNT), stack.get(ModDataComponentTypes.LIGHT_CAPACITY)));
            super.appendTooltip(stack, context, tooltip, type);
        }
    });
    public static final Item MOONLIGHT_COLLECTOR = registerItem("moonlight_collector", new Item(new Item.Settings().maxCount(1).component(ModDataComponentTypes.LIGHT_AMOUNT, 0).component(ModDataComponentTypes.LIGHT_CAPACITY, 1000)) {
        @Override
        public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
            tooltip.add(Text.translatable("tooltip.alkimia.light_collector_crystal", stack.get(ModDataComponentTypes.LIGHT_AMOUNT), stack.get(ModDataComponentTypes.LIGHT_CAPACITY)));
            super.appendTooltip(stack, context, tooltip, type);
        }
    });
    public static final Item STARLIGHT_COLLECTOR = registerItem("starlight_collector", new Item(new Item.Settings().maxCount(1).component(ModDataComponentTypes.LIGHT_AMOUNT, 0).component(ModDataComponentTypes.LIGHT_CAPACITY, 1000)) {
        @Override
        public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
            tooltip.add(Text.translatable("tooltip.alkimia.light_collector_crystal", stack.get(ModDataComponentTypes.LIGHT_AMOUNT), stack.get(ModDataComponentTypes.LIGHT_CAPACITY)));
            super.appendTooltip(stack, context, tooltip, type);
        }
    });

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(Alkimia.MOD_ID, name), item);
    }

    public static void registerModItems() {
        Alkimia.LOGGER.info("Registering Mod Items for " + Alkimia.MOD_ID);
    }
}
