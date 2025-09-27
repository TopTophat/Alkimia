package net.falling_pan.alkimia.block.entity;

import net.falling_pan.alkimia.Alkimia;
import net.falling_pan.alkimia.block.ModBlocks;
import net.falling_pan.alkimia.block.entity.custom.*;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlockEntities {
    public static final BlockEntityType<MortarBlockEntity> MORTAR_BE =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(Alkimia.MOD_ID, "mortar_be"),
                    BlockEntityType.Builder.create(MortarBlockEntity::new, ModBlocks.MORTAR).build(null));
    public static final BlockEntityType<AlchemicalCrucibleBlockEntity> ALCHEMICAL_CRUCIBLE_BE =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(Alkimia.MOD_ID, "alchemical_crucible_be"),
                    BlockEntityType.Builder.create(AlchemicalCrucibleBlockEntity::new, ModBlocks.ALCHEMICAL_CRUCIBLE).build(null));
    public static final BlockEntityType<JarBlockEntity> JAR_BE =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(Alkimia.MOD_ID, "jar_be"),
                    BlockEntityType.Builder.create(JarBlockEntity::new, ModBlocks.JAR).build(null));
    public static final BlockEntityType<CalcifyingCrucibleBlockEntity> CALCIFYING_CRUCIBLE_BE =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(Alkimia.MOD_ID, "calcifying_crucible_be"),
                    BlockEntityType.Builder.create(CalcifyingCrucibleBlockEntity::new, ModBlocks.CALCIFYING_CRUCIBLE).build(null));
    public static final BlockEntityType<TrayBlockEntity> TRAY_BE =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(Alkimia.MOD_ID, "tray_be"),
                    BlockEntityType.Builder.create(TrayBlockEntity::new, ModBlocks.TRAY).build(null));
    public static final BlockEntityType<DistillerInputBlockEntity> DISTILLER_INPUT_BE =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(Alkimia.MOD_ID, "distiller_input_be"),
                    BlockEntityType.Builder.create(DistillerInputBlockEntity::new, ModBlocks.DISTILLER_INPUT).build(null));
    public static final BlockEntityType<DistillerOutputBlockEntity> DISTILLER_OUTPUT_BE =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(Alkimia.MOD_ID, "distiller_output_be"),
                    BlockEntityType.Builder.create(DistillerOutputBlockEntity::new, ModBlocks.DISTILLER_OUTPUT).build(null));
    public static final BlockEntityType<StoveBlockEntity> STOVE_BE =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(Alkimia.MOD_ID, "stove_be"),
                    BlockEntityType.Builder.create(StoveBlockEntity::new, ModBlocks.STOVE).build(null));
    public static final BlockEntityType<CondenserInputBlockEntity> CONDENSER_INPUT_BE =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(Alkimia.MOD_ID, "condenser_input_be"),
                    BlockEntityType.Builder.create(CondenserInputBlockEntity::new, ModBlocks.CONDENSER_INPUT).build(null));
    public static final BlockEntityType<CondenserOutputBlockEntity> CONDENSER_OUTPUT_BE =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(Alkimia.MOD_ID, "condenser_output_be"),
                    BlockEntityType.Builder.create(CondenserOutputBlockEntity::new, ModBlocks.CONDENSER_OUTPUT).build(null));
    public static final BlockEntityType<InfuserInputBlockEntity> INFUSER_INPUT_BE =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(Alkimia.MOD_ID, "infuser_input_be"),
                    BlockEntityType.Builder.create(InfuserInputBlockEntity::new, ModBlocks.INFUSER_INPUT).build(null));
    public static final BlockEntityType<InfuserOutputBlockEntity> INFUSER_OUTPUT_BE =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(Alkimia.MOD_ID, "infuser_output_be"),
                    BlockEntityType.Builder.create(InfuserOutputBlockEntity::new, ModBlocks.INFUSER_OUTPUT).build(null));
    public static final BlockEntityType<CentrifugeInputBlockEntity> CENTRIFUGE_INPUT_BE =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(Alkimia.MOD_ID, "centrifuge_input_be"),
                    BlockEntityType.Builder.create(CentrifugeInputBlockEntity::new, ModBlocks.CENTRIFUGE_INPUT).build(null));
    public static final BlockEntityType<CentrifugeOutBlockEntity> CENTRIFUGE_OUT_BE =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(Alkimia.MOD_ID, "centrifuge_out_be"),
                    BlockEntityType.Builder.create(CentrifugeOutBlockEntity::new, ModBlocks.CENTRIFUGE_OUT).build(null));
    public static final BlockEntityType<CompressorBlockEntity> COMPRESSOR_BE =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(Alkimia.MOD_ID, "compressor_be"),
                    BlockEntityType.Builder.create(CompressorBlockEntity::new, ModBlocks.COMPRESSOR).build(null));
    public static final BlockEntityType<ItemPedestalBlockEntity> ITEM_PEDESTAL_BE =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(Alkimia.MOD_ID, "item_pedestal_be"),
                    BlockEntityType.Builder.create(ItemPedestalBlockEntity::new, ModBlocks.ITEM_PEDESTAL_BASE).build(null));
    public static final BlockEntityType<TransmutingPedestalBlockEntity> TRANSMUTING_PEDESTAL_BE =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(Alkimia.MOD_ID, "transmuting_pedestal_be"),
                    BlockEntityType.Builder.create(TransmutingPedestalBlockEntity::new, ModBlocks.TRANSMUTING_PEDESTAL).build(null));
    public static final BlockEntityType<FluidPedestalBlockEntity> FLUID_PEDESTAL_BE =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(Alkimia.MOD_ID, "fluid_pedestal_be"),
                    BlockEntityType.Builder.create(FluidPedestalBlockEntity::new, ModBlocks.FLUID_PEDESTAL_BASE).build(null));

    public static void registerBlockEntities()
    {
        Alkimia.LOGGER.info("Registering Block Entities for " + Alkimia.MOD_ID);
    }
}
