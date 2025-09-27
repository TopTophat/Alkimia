package net.falling_pan.alkimia.item;

import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Items;

public class ModFoodComponents {
    public static final FoodComponent NIGHT_VISION_POTION = new FoodComponent.Builder().alwaysEdible().usingConvertsTo(Items.GLASS_BOTTLE).statusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 10000), 100).build();
}
