package net.toptophat.alkimia.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.toptophat.alkimia.Alkimia;
import net.toptophat.alkimia.block.ModBlocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {
    public static final ItemGroup ALCHEMY_GROUP = Registry.register(Registries.ITEM_GROUP, Identifier.of(Alkimia.MOD_ID, "alchemy"), FabricItemGroup.builder().displayName(Text.translatable("itemgroup.alkimia.alchemy")).entries((displayContext, entries) -> {
        entries.add(ModItems.QUICKSILVER);
        entries.add(ModItems.CINNABAR);
        entries.add(ModBlocks.CINNABAR_BLOCK);
        entries.add(ModBlocks.QUICKSILVER_TANK);
        entries.add(ModBlocks.CINNABAR_ORE);
        entries.add(ModItems.FLASH_POWDER);
        entries.add(ModBlocks.COLD_BLOCK);
        entries.add(ModBlocks.PLAGUE);
        entries.add(ModBlocks.DEVOURER);
        entries.add(ModBlocks.DEAD_PLAGUE);
        entries.add(ModItems.LIQUID_SUNLIGHT);
        entries.add(ModItems.NIGHT_VISION_POTION);
        entries.add(ModBlocks.MORTAR);
        entries.add(ModItems.PESTLE);
        entries.add(ModItems.GUIDEBOOK);
        entries.add(ModBlocks.ALCHEMICAL_CRUCIBLE);
        entries.add(ModBlocks.JAR);
        entries.add(ModItems.DIAMOND_DUST_PILE);
        entries.add(ModItems.DIAMOND_DUST_BUCKET);
        entries.add(ModItems.SOUL_BUCKET);
        entries.add(ModItems.LIQUID_SOUL_BUCKET);
        entries.add(ModItems.VIAL);
        entries.add(ModItems.FLASK);
        entries.add(ModBlocks.CALCIFYING_CRUCIBLE);
        entries.add(ModBlocks.TRAY);
        entries.add(ModBlocks.DISTILLER_INPUT);
        entries.add(ModBlocks.DISTILLER_OUTPUT);
        entries.add(ModBlocks.STOVE);
        entries.add(ModBlocks.COOLER);
        entries.add(ModBlocks.CONDENSER_INPUT);
        entries.add(ModBlocks.CONDENSER_OUTPUT);
        entries.add(ModBlocks.INFUSER_INPUT);
        entries.add(ModBlocks.INFUSER_OUTPUT);
        entries.add(ModBlocks.CENTRIFUGE_INPUT);
        entries.add(ModBlocks.CENTRIFUGE_OUT);
        entries.add(ModItems.TINY_VIAL);
        entries.add(ModBlocks.COMPRESSOR);
        entries.add(ModBlocks.ITEM_PEDESTAL_BASE);
        entries.add(ModBlocks.FLUID_PEDESTAL_BASE);
        entries.add(ModBlocks.TRANSMUTING_PEDESTAL);
    }).icon(() -> new ItemStack(ModItems.QUICKSILVER)).build());


    public static void registerItemGroups() {
        Alkimia.LOGGER.info("Registering Item Groups for " + Alkimia.MOD_ID);
    }
}
