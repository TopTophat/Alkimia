package net.toptophat.alkimia.component;

import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.toptophat.alkimia.Alkimia;
import net.minecraft.component.ComponentType;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.function.UnaryOperator;

public class ModDataComponentTypes {
    public static final ComponentType<Integer> AMOUNT =
            Registry.register(
                    Registries.DATA_COMPONENT_TYPE,
                    Identifier.of(Alkimia.MOD_ID, "amount"),
                    ComponentType.<Integer>builder()
                            .codec(Codec.INT)                 // how it’s saved to NBT
                            .packetCodec(PacketCodecs.INTEGER) // how it’s synced over network
                            .build()
            );
    public static final ComponentType<Integer> CAPACITY =
            Registry.register(
                    Registries.DATA_COMPONENT_TYPE,
                    Identifier.of(Alkimia.MOD_ID, "capacity"),
                    ComponentType.<Integer>builder()
                            .codec(Codec.INT)                 // how it’s saved to NBT
                            .packetCodec(PacketCodecs.INTEGER) // how it’s synced over network
                            .build()
            );
    public static final ComponentType<Integer> LIGHT_AMOUNT =
            Registry.register(
                    Registries.DATA_COMPONENT_TYPE,
                    Identifier.of(Alkimia.MOD_ID, "light_amount"),
                    ComponentType.<Integer>builder()
                            .codec(Codec.INT)                 // how it’s saved to NBT
                            .packetCodec(PacketCodecs.INTEGER) // how it’s synced over network
                            .build()
            );
    public static final ComponentType<Integer> LIGHT_CAPACITY =
            Registry.register(
                    Registries.DATA_COMPONENT_TYPE,
                    Identifier.of(Alkimia.MOD_ID, "light_capacity"),
                    ComponentType.<Integer>builder()
                            .codec(Codec.INT)                 // how it’s saved to NBT
                            .packetCodec(PacketCodecs.INTEGER) // how it’s synced over network
                            .build()
            );
    public static final ComponentType<Integer> PAGE =
            Registry.register(
                    Registries.DATA_COMPONENT_TYPE,
                    Identifier.of(Alkimia.MOD_ID, "page"),
                    ComponentType.<Integer>builder()
                            .codec(Codec.INT)                 // how it’s saved to NBT
                            .packetCodec(PacketCodecs.INTEGER) // how it’s synced over network
                            .build()
            );
    public static final ComponentType<FluidVariant> STORED_FLUID = register("stored_fluid", builder -> builder.codec(FluidVariant.CODEC));

    private static <T>ComponentType<T> register(String name, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, Identifier.of(Alkimia.MOD_ID, name),
                builderOperator.apply(ComponentType.builder()).build());
    }

    public static void registerDataComponentTypes() {
        Alkimia.LOGGER.info("Registering Data Component Types for " + Alkimia.MOD_ID);
    }
}
