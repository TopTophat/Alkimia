package net.falling_pan.alkimia.fluid;

import net.falling_pan.alkimia.Alkimia;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModFluids {
    public static final FlowableFluid FLOWING_DIAMOND_DUST = register("flowing_diamond_dust", new DiamondDustFluid.Flowing());
    public static final FlowableFluid DIAMOND_DUST = register("diamond_dust", new DiamondDustFluid.Still());
    public static final FlowableFluid FLOWING_SOUL = register("flowing_soul", new SoulFluid.Flowing());
    public static final FlowableFluid SOUL = register("soul", new SoulFluid.Still());
    public static final FlowableFluid FLOWING_LIQUID_SOUL = register("flowing_liquid_soul", new LiquidSoulFluid.Flowing());
    public static final FlowableFluid LIQUID_SOUL = register("liquid_soul", new LiquidSoulFluid.Still());

    private static FlowableFluid register(String name, FlowableFluid fluid) {
        return Registry.register(Registries.FLUID, Identifier.of(Alkimia.MOD_ID, name), fluid);
    }
}
