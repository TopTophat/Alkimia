package net.toptophat.alkimia;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.toptophat.alkimia.block.ModBlocks;
import net.toptophat.alkimia.block.entity.ModBlockEntities;
import net.toptophat.alkimia.block.entity.renderer.*;
import net.toptophat.alkimia.fluid.ModFluids;
import net.toptophat.alkimia.util.ModModelPredicates;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.util.Identifier;

public class AlkimiaModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockEntityRendererFactories.register(ModBlockEntities.MORTAR_BE, MortarBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntities.ALCHEMICAL_CRUCIBLE_BE, AlchemicalCrucibleBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntities.JAR_BE, JarBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntities.CALCIFYING_CRUCIBLE_BE, CalcifyingCrucibleBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntities.TRAY_BE, TrayBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntities.DISTILLER_INPUT_BE, DistillerInputBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntities.DISTILLER_OUTPUT_BE, DistillerOutputBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntities.CONDENSER_INPUT_BE, CondenserInputBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntities.CONDENSER_OUTPUT_BE, CondenserOutputBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntities.INFUSER_INPUT_BE, InfuserInputBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntities.INFUSER_OUTPUT_BE, InfuserOutputBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntities.CENTRIFUGE_INPUT_BE, CentrifugeInputBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntities.CENTRIFUGE_OUT_BE, CentrifugeOutBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntities.COMPRESSOR_BE, CompressorBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntities.ITEM_PEDESTAL_BE, ItemPedestalBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntities.FLUID_PEDESTAL_BE, FluidPedestalBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntities.TRANSMUTING_PEDESTAL_BE, TransmutingPedestalBlockEntityRenderer::new);
        ModBlocks.registerBlockRenderLayers();

        FluidRenderHandlerRegistry.INSTANCE.register(
                ModFluids.DIAMOND_DUST, ModFluids.FLOWING_DIAMOND_DUST,
                new SimpleFluidRenderHandler(
                        Identifier.of("alkimia:block/diamond_dust_still"),
                        Identifier.of("alkimia:block/diamond_dust_flow"),
                        0x00FFFFFF // tint color
                )
        );
        FluidRenderHandlerRegistry.INSTANCE.register(
                ModFluids.SOUL, ModFluids.FLOWING_SOUL,
                new SimpleFluidRenderHandler(
                        Identifier.of("alkimia:block/soul_still"),
                        Identifier.of("alkimia:block/soul_flow"),
                        0x00FFFFFF // tint color
                )
        );
        FluidRenderHandlerRegistry.INSTANCE.register(
                ModFluids.LIQUID_SOUL, ModFluids.FLOWING_LIQUID_SOUL,
                new SimpleFluidRenderHandler(
                        Identifier.of("alkimia:block/liquid_soul_still"),
                        Identifier.of("alkimia:block/liquid_soul_flow"),
                        0x00FFFFFF // tint color
                )
        );

        BlockRenderLayerMap.INSTANCE.putFluids(
                RenderLayer.getTranslucent(),
                ModFluids.DIAMOND_DUST, ModFluids.FLOWING_DIAMOND_DUST,
                ModFluids.LIQUID_SOUL, ModFluids.FLOWING_LIQUID_SOUL,
                ModFluids.SOUL, ModFluids.FLOWING_SOUL
        );

        ModModelPredicates.registerModelPredicates();
    }
}
