package net.toptophat.alkimia.item;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.toptophat.alkimia.Alkimia;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Alkimia.MOD_ID);

    public static final RegistryObject<Item> SOULIUM = ITEMS.register("soulium",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> RAW_SOULIUM = ITEMS.register("raw_soulium",
            () -> new Item(new Item.Properties()));


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}