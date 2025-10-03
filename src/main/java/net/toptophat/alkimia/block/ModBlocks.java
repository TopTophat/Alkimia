package net.toptophat.alkimia.block;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.toptophat.alkimia.Alkimia;
import net.toptophat.alkimia.block.custom.*;
import net.toptophat.alkimia.fluid.ModFluids;
import net.minecraft.block.*;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class ModBlocks {

    public static final Block QUICKSILVER_TANK = registerBlock("quicksilver_tank",
            new Block(AbstractBlock.Settings.create().strength(0.5f).sounds(BlockSoundGroup.GLASS)));
    public static final Block CINNABAR_BLOCK = registerBlock("cinnabar_block",
            new Block(AbstractBlock.Settings.create().strength(2f).sounds(BlockSoundGroup.STONE).requiresTool()));
    public static final Block CINNABAR_ORE = registerBlock("cinnabar_ore",
            new Block(AbstractBlock.Settings.create().strength(3f).sounds(BlockSoundGroup.STONE).requiresTool()));
    public static final Block COLD_BLOCK = registerBlock("cold_block",
            new ColdBlock(AbstractBlock.Settings.create().strength(0.1f).sounds(BlockSoundGroup.AMETHYST_BLOCK).slipperiness(1.1f)));
    public static final Block PLAGUE = registerBlock("plague",
            new Plague(AbstractBlock.Settings.create().strength(4f).sounds(BlockSoundGroup.SCULK_VEIN)));
    public static final Block DEVOURER = registerBlock("devourer",
            new Devourer(AbstractBlock.Settings.create().strength(4f).sounds(BlockSoundGroup.SCULK_VEIN)));
    public static final Block DEAD_PLAGUE = registerBlock("dead_plague",
            new Block(AbstractBlock.Settings.create().strength(6f).sounds(BlockSoundGroup.STONE).requiresTool()));
    public static final Block MORTAR = registerBlock("mortar",
            new Mortar(AbstractBlock.Settings.create().strength(1f).sounds(BlockSoundGroup.STONE).nonOpaque()));
    public static final Block ITEM_PEDESTAL_BASE = registerBlock("item_pedestal_base",
            new ItemPedestal(AbstractBlock.Settings.create().strength(1f).sounds(BlockSoundGroup.GLASS).nonOpaque()));
    public static final Block LIGHT_COLLECTOR = registerBlock("light_collector",
            new LightCollector(AbstractBlock.Settings.create().strength(1f).sounds(BlockSoundGroup.GLASS).nonOpaque()));
    public static final Block LENS_BASE = registerBlock("lens_base",
            new Lens(AbstractBlock.Settings.create().strength(1f).sounds(BlockSoundGroup.GLASS).nonOpaque()));
    public static final Block LENS_ROTARY_STAND = registerBlock("lens_rotary_stand",
            new Block(AbstractBlock.Settings.create().strength(1f).sounds(BlockSoundGroup.GLASS).nonOpaque()));
    public static final Block LENS_LENS = registerBlock("lens_lens",
            new Block(AbstractBlock.Settings.create().strength(1f).sounds(BlockSoundGroup.GLASS).nonOpaque()));
    public static final Block TRANSMUTING_PEDESTAL = registerBlock("transmuting_pedestal",
            new TransmutingPedestal(AbstractBlock.Settings.create().strength(1f).sounds(BlockSoundGroup.GLASS).nonOpaque()));
    public static final Block ITEM_PEDESTAL_GLOBE = registerBlock("item_pedestal_globe",
            new Block(AbstractBlock.Settings.create().strength(1f).sounds(BlockSoundGroup.GLASS).nonOpaque()));
    public static final Block FLUID_PEDESTAL_BASE = registerBlock("fluid_pedestal_base",
            new FluidPedestal(AbstractBlock.Settings.create().strength(1f).sounds(BlockSoundGroup.GLASS).nonOpaque()));
    public static final Block FLUID_PEDESTAL_GLOBE = registerBlock("fluid_pedestal_globe",
            new Block(AbstractBlock.Settings.create().strength(1f).sounds(BlockSoundGroup.GLASS).nonOpaque()));
    public static final Block ALCHEMICAL_CRUCIBLE = registerBlock("alchemical_crucible",
            new AlchemicalCrucible(AbstractBlock.Settings.create().strength(1f).sounds(BlockSoundGroup.METAL).nonOpaque()));
    public static final Block JAR = registerBlock("jar",
            new Jar(AbstractBlock.Settings.create().strength(1f).sounds(BlockSoundGroup.GLASS).nonOpaque()));
    public static final Block CALCIFYING_CRUCIBLE = registerBlock("calcifying_crucible",
            new CalcifyingCrucible(AbstractBlock.Settings.create().strength(1f).sounds(BlockSoundGroup.GLASS).nonOpaque()));
    public static final Block DISTILLER_INPUT = registerBlock("distiller_input",
            new DistillerInput(AbstractBlock.Settings.create().strength(1f).sounds(BlockSoundGroup.GLASS).nonOpaque()));
    public static final Block DISTILLER_OUTPUT = registerBlock("distiller_output",
            new DistillerOutput(AbstractBlock.Settings.create().strength(1f).sounds(BlockSoundGroup.GLASS).nonOpaque()));
    public static final Block TRAY = registerBlock("tray",
            new Tray(AbstractBlock.Settings.create().strength(1f).sounds(BlockSoundGroup.METAL).nonOpaque()));
    public static final Block STOVE = registerBlock("stove",
            new Stove(AbstractBlock.Settings.create().strength(1f).sounds(BlockSoundGroup.METAL)));
    public static final Block CONDENSER_INPUT = registerBlock("condenser_input",
            new CondenserInput(AbstractBlock.Settings.create().strength(1f).sounds(BlockSoundGroup.GLASS).nonOpaque()));
    public static final Block CONDENSER_OUTPUT = registerBlock("condenser_output",
            new CondenserOutput(AbstractBlock.Settings.create().strength(1f).sounds(BlockSoundGroup.GLASS).nonOpaque()));
    public static final Block INFUSER_INPUT = registerBlock("infuser_input",
            new InfuserInput(AbstractBlock.Settings.create().strength(1f).sounds(BlockSoundGroup.METAL).nonOpaque()));
    public static final Block CENTRIFUGE_INPUT = registerBlock("centrifuge_input",
            new CentrifugeInput(AbstractBlock.Settings.create().strength(1f).sounds(BlockSoundGroup.METAL).nonOpaque()));
    public static final Block CENTRIFUGE_OUT = registerBlock("centrifuge_out",
            new CentrifugeOut(AbstractBlock.Settings.create().strength(1f).sounds(BlockSoundGroup.GLASS).nonOpaque()));
    public static final Block INFUSER_OUTPUT = registerBlock("infuser_output",
            new InfuserOutput(AbstractBlock.Settings.create().strength(1f).sounds(BlockSoundGroup.GLASS).nonOpaque()));
    public static final Block COMPRESSOR = registerBlock("compressor",
            new Compressor(AbstractBlock.Settings.create().strength(1f).sounds(BlockSoundGroup.METAL).nonOpaque()));
    public static final Block COOLER = registerBlock("cooler",
            new Block(AbstractBlock.Settings.create().strength(1f).sounds(BlockSoundGroup.METAL)));
    public static final Block DIAMOND_DUST = registerBlock(
            "diamond_dust",
            new FluidBlock(
                    ModFluids.DIAMOND_DUST,
                    AbstractBlock.Settings.create()
                            .mapColor(MapColor.WATER_BLUE)
                            .replaceable()
                            .noCollision()
                            .strength(100.0F)
                            .pistonBehavior(PistonBehavior.DESTROY)
                            .dropsNothing()
                            .liquid()
                            .sounds(BlockSoundGroup.INTENTIONALLY_EMPTY)
            )
    );
    public static final Block SOUL = registerBlock(
            "soul",
            new FluidBlock(
                    ModFluids.SOUL,
                    AbstractBlock.Settings.create()
                            .mapColor(MapColor.WATER_BLUE)
                            .replaceable()
                            .noCollision()
                            .strength(100.0F)
                            .pistonBehavior(PistonBehavior.DESTROY)
                            .dropsNothing()
                            .liquid()
                            .sounds(BlockSoundGroup.INTENTIONALLY_EMPTY)
            )
    );
    public static final Block LIQUID_SOUL = registerBlock(
            "liquid_soul",
            new FluidBlock(
                    ModFluids.LIQUID_SOUL,
                    AbstractBlock.Settings.create()
                            .mapColor(MapColor.WATER_BLUE)
                            .replaceable()
                            .noCollision()
                            .strength(100.0F)
                            .pistonBehavior(PistonBehavior.DESTROY)
                            .dropsNothing()
                            .liquid()
                            .sounds(BlockSoundGroup.INTENTIONALLY_EMPTY)
            )
    );

    private static Block registerBlock(String name, Block block)
    {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, Identifier.of(Alkimia.MOD_ID, name), block);
    }

    private static void registerBlockItem(String name, Block block) {
        Registry.register(Registries.ITEM, Identifier.of(Alkimia.MOD_ID, name),
                new BlockItem(block, new Item.Settings()));
    }

    public static void registerModBlocks() {
        Alkimia.LOGGER.info("Registering Mod Blocks for " + Alkimia.MOD_ID);
    }

    public static void registerBlockRenderLayers() {
        BlockRenderLayerMap.INSTANCE.putBlock(JAR, RenderLayer.getTextBackgroundSeeThrough());
        BlockRenderLayerMap.INSTANCE.putBlock(CALCIFYING_CRUCIBLE, RenderLayer.getTextBackgroundSeeThrough());
        BlockRenderLayerMap.INSTANCE.putBlock(DISTILLER_INPUT, RenderLayer.getTextBackgroundSeeThrough());
        BlockRenderLayerMap.INSTANCE.putBlock(DISTILLER_OUTPUT, RenderLayer.getTextBackgroundSeeThrough());
        BlockRenderLayerMap.INSTANCE.putBlock(CONDENSER_OUTPUT, RenderLayer.getTextBackgroundSeeThrough());
        BlockRenderLayerMap.INSTANCE.putBlock(CONDENSER_INPUT, RenderLayer.getTextBackgroundSeeThrough());
        BlockRenderLayerMap.INSTANCE.putBlock(INFUSER_INPUT, RenderLayer.getTextBackgroundSeeThrough());
        BlockRenderLayerMap.INSTANCE.putBlock(INFUSER_OUTPUT, RenderLayer.getTextBackgroundSeeThrough());
        BlockRenderLayerMap.INSTANCE.putBlock(CENTRIFUGE_INPUT, RenderLayer.getTextBackgroundSeeThrough());
        BlockRenderLayerMap.INSTANCE.putBlock(CENTRIFUGE_OUT, RenderLayer.getTextBackgroundSeeThrough());
        BlockRenderLayerMap.INSTANCE.putBlock(COMPRESSOR, RenderLayer.getTextBackgroundSeeThrough());
        BlockRenderLayerMap.INSTANCE.putBlock(ITEM_PEDESTAL_BASE, RenderLayer.getTextBackgroundSeeThrough());
        BlockRenderLayerMap.INSTANCE.putBlock(ITEM_PEDESTAL_GLOBE, RenderLayer.getTextBackgroundSeeThrough());
        BlockRenderLayerMap.INSTANCE.putBlock(FLUID_PEDESTAL_BASE, RenderLayer.getTextBackgroundSeeThrough());
        BlockRenderLayerMap.INSTANCE.putBlock(FLUID_PEDESTAL_GLOBE, RenderLayer.getTextBackgroundSeeThrough());
        BlockRenderLayerMap.INSTANCE.putBlock(TRANSMUTING_PEDESTAL, RenderLayer.getTextBackgroundSeeThrough());
        BlockRenderLayerMap.INSTANCE.putBlock(LENS_BASE, RenderLayer.getTextBackgroundSeeThrough());
        BlockRenderLayerMap.INSTANCE.putBlock(LENS_ROTARY_STAND, RenderLayer.getTextBackgroundSeeThrough());
        BlockRenderLayerMap.INSTANCE.putBlock(LENS_LENS, RenderLayer.getTextBackgroundSeeThrough());
        BlockRenderLayerMap.INSTANCE.putBlock(LIGHT_COLLECTOR, RenderLayer.getTextBackgroundSeeThrough());
    }
}
