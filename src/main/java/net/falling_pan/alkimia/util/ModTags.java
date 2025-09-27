package net.falling_pan.alkimia.util;

import net.falling_pan.alkimia.Alkimia;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class ModTags {
    public static class Blocks
    {
        public static final TagKey<Block> EVERFROST_TARGETS = createTag("everfrost_targets");

        private static TagKey<Block> createTag(String name) {
            return TagKey.of(RegistryKeys.BLOCK, Identifier.of(Alkimia.MOD_ID, name));
        }
    }

    public static class Items
    {
        private static TagKey<Item> createTag(String name) {
            return TagKey.of(RegistryKeys.ITEM, Identifier.of(Alkimia.MOD_ID, name));
        }
    }
}
