package net.toptophat.alkimia.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.toptophat.alkimia.Alkimia;
import net.toptophat.alkimia.block.ModBlocks;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Alkimia.MOD_ID);

    public static final RegistryObject<CreativeModeTab> ALKIMIA_TAB = CREATIVE_MODE_TABS.register("alkimia_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.SOULIUM.get()))
                    .title(Component.translatable("creativetab.alkimia_tab"))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(ModItems.SOULIUM.get());
                        pOutput.accept(ModItems.RAW_SOULIUM.get());
                        pOutput.accept(ModBlocks.RAW_SOULIUM_BLOCK.get());
                        pOutput.accept(ModBlocks.SOULIUM_BLOCK.get());
                        pOutput.accept(ModBlocks.SOULIUM_ORE.get());
                    })
                    .build());


    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
