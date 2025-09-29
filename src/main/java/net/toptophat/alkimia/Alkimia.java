package net.toptophat.alkimia;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.toptophat.alkimia.block.ModBlocks;
import net.toptophat.alkimia.block.custom.*;
import net.toptophat.alkimia.block.entity.ModBlockEntities;
import net.toptophat.alkimia.component.ModDataComponentTypes;
import net.toptophat.alkimia.item.ModItemGroups;
import net.toptophat.alkimia.item.ModItems;
import net.toptophat.alkimia.item.custom.FlashPowderItem;
import net.toptophat.alkimia.item.custom.FlaskItem;
import net.toptophat.alkimia.item.custom.VialItem;
import net.toptophat.alkimia.item.custom.TinyVialItem;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Alkimia implements ModInitializer {
	public static final String MOD_ID = "alkimia";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItemGroups.registerItemGroups();

		ModItems.registerModItems();
		ModBlocks.registerModBlocks();

		ModBlockEntities.registerBlockEntities();

		ModDataComponentTypes.registerDataComponentTypes();

		AlchemicalCrucible.initMaps();
		Mortar.InitMaps();
		FluidMaps.InitMaps();
		VialItem.InitMaps();
		TinyVialItem.InitMaps();
		FlaskItem.InitMaps();
		Stove.InitMaps();
		FlashPowderItem.InitMaps();
		Tray.InitRecipes();
		DistillerInput.InitRecipes();
		CondenserInput.InitRecipes();
		InfuserInput.InitRecipes();
		CentrifugeInput.InitRecipes();
		Compressor.InitRecipes();
		TransmutingPedestal.InitRecipes();

		FuelRegistry.INSTANCE.add(ModItems.LIQUID_SUNLIGHT, 30000);

		ItemTooltipCallback.EVENT.register((stack, context, type, tooltip) -> {
			if (stack.isOf(Items.GLOW_BERRIES)) {
				tooltip.add(Text.translatable("tooltip.alkimia.glow_berries"));
			}
		});

		ItemTooltipCallback.EVENT.register((stack, context, type, tooltip) -> {
			if (stack.isOf(Items.COPPER_INGOT)) {
				tooltip.add(Text.translatable("tooltip.alkimia.copper_ingot"));
			}
		});

		ItemTooltipCallback.EVENT.register((stack, context, type, tooltip) -> {
			if (stack.isOf(Items.ENDER_EYE)) {
				tooltip.add(Text.translatable("tooltip.alkimia.ender_eye"));
			}
		});

		ItemTooltipCallback.EVENT.register((stack, context, type, tooltip) -> {
			if (stack.isOf(Items.LILY_OF_THE_VALLEY)) {
				tooltip.add(Text.translatable("tooltip.alkimia.lily_of_the_valley"));
			}
		});

		ItemTooltipCallback.EVENT.register((stack, context, type, tooltip) -> {
			if (stack.isOf(Items.SLIME_BALL)) {
				tooltip.add(Text.translatable("tooltip.alkimia.slime_ball"));
			}
		});

		ItemTooltipCallback.EVENT.register((stack, context, type, tooltip) -> {
			if (stack.isOf(Items.GLOWSTONE_DUST)) {
				tooltip.add(Text.translatable("tooltip.alkimia.glowstone_dust"));
			}
		});

		//TODO: #CODING LIGHT COLLECTOR
		//TODO: #CODING MORE ASPECTS
		//TODO: #CODING MORE BREWS
		//TODO: #CODING THROWABLE BREWS
		//TODO: #CODING BREW HURLER BAZOOKA
		//TODO: #CODING CURSES!!!
		//TODO: #CODING ANTIDOTES?
		//TODO: #PAINTING PAINT ALL THE BOOK TEXTURES
		//TODO: #PAINTING PAINT ALL TEXTURES
		//TODO: #PAINTING PAINT TINY VIAL TEXTURE
		//TODO: #PAINTING REMAKE THE COOLER
		//TODO: #CONTENT ADD ALL THE CONTENT
	}
}