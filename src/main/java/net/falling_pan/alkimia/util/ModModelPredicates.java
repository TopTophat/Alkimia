package net.falling_pan.alkimia.util;

import net.falling_pan.alkimia.Alkimia;
import net.falling_pan.alkimia.component.ModDataComponentTypes;
import net.falling_pan.alkimia.item.ModItems;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.util.Identifier;

public class ModModelPredicates {
    public static void registerModelPredicates() {
        ModelPredicateProviderRegistry.register(ModItems.VIAL, Identifier.of(Alkimia.MOD_ID, "full"),
                (stack, world, entity, seed) -> stack.get(ModDataComponentTypes.AMOUNT) != null && stack.get(ModDataComponentTypes.AMOUNT) > 0 ? 1f : 0f);
        ModelPredicateProviderRegistry.register(ModItems.TINY_VIAL, Identifier.of(Alkimia.MOD_ID, "full"),
                (stack, world, entity, seed) -> stack.get(ModDataComponentTypes.AMOUNT) != null && stack.get(ModDataComponentTypes.AMOUNT) > 0 ? 1f : 0f);
        ModelPredicateProviderRegistry.register(ModItems.FLASK, Identifier.of(Alkimia.MOD_ID, "full"),
                (stack, world, entity, seed) -> stack.get(ModDataComponentTypes.AMOUNT) != null && stack.get(ModDataComponentTypes.AMOUNT) > 0 ? 1f : 0f);

    }
}
