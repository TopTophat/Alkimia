package net.toptophat.alkimia.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.toptophat.alkimia.Alkimia;
import net.toptophat.alkimia.entity.custom.NightVisionThrowPotionEntity;
import net.toptophat.alkimia.item.custom.NightVisionThrowPotion;

public class ModEntities {
    public static final EntityType<NightVisionThrowPotionEntity> NIGHT_VISION_THROW_POTION_ENTITY = Registry.register(Registries.ENTITY_TYPE,
            Identifier.of("alkimia", "night_vision_throw_potion_entity"),
            EntityType.Builder.<NightVisionThrowPotionEntity>create(NightVisionThrowPotionEntity::new, SpawnGroup.MISC)
                    .dimensions(0.1f, 0.1f).build());


    public static void registerModEntities() {
        Alkimia.LOGGER.info("Registering Mod Entities for " + Alkimia.MOD_ID);
    }
}
