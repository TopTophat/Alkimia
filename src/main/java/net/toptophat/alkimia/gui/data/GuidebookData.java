package net.toptophat.alkimia.gui.data;

import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec2f;

import java.util.List;

public class GuidebookData {
    //20 characters per 1/2 page
    public static BookData book = new BookData(List.of(
            new EntryData(
                    List.of(new PageData(
                            List.of(
                                    new TextData(Text.translatable("guidebook.alkimia.hello_world0"), new Vec2f(100, 45))
                            ),
                            List.of(

                            ),
                            List.of(
                                    new ItemData(Identifier.of("minecraft", "textures/block/bedrock.png"), new Vec2f(100, 100), new Vec2f(16, 16))
                            ),
                            0
                    )), "Intro", "Help"
            ),
            new EntryData(
                    List.of(new PageData(
                            List.of(
                                    new TextData(Text.translatable("guidebook.alkimia.hello_world1"), new Vec2f(100, 45))
                            ),
                            List.of(

                            ),
                            List.of(
                                    new ItemData(Identifier.of("minecraft", "textures/item/redstone.png"), new Vec2f(100, 100), new Vec2f(16, 16))
                            ),
                            1
                    ), new PageData(
                            List.of(
                                    new TextData(Text.translatable("guidebook.alkimia.hello_world2"), new Vec2f(100, 45))
                            ),
                            List.of(

                            ),
                            List.of(
                                    new ItemData(Identifier.of("minecraft", "textures/item/feather.png"), new Vec2f(100, 100), new Vec2f(16, 16))
                            ),
                            2
                    )), "Continuation", "Middle"
            ),
            new EntryData(
                    List.of(new PageData(
                            List.of(
                                    new TextData(Text.translatable("guidebook.alkimia.flash_powder_facts1"), new Vec2f(100, 45)),
                                    new TextData(Text.translatable("guidebook.alkimia.flash_powder_facts2"), new Vec2f(100, 55)),
                                    new TextData(Text.translatable("guidebook.alkimia.flash_powder_facts3"), new Vec2f(100, 65)),
                                    new TextData(Text.translatable("guidebook.alkimia.flash_powder_facts4"), new Vec2f(100, 75)),
                                    new TextData(Text.translatable("guidebook.alkimia.flash_powder_facts5"), new Vec2f(100, 85)),
                                    new TextData(Text.translatable("guidebook.alkimia.flash_powder_facts6"), new Vec2f(100, 95))
                            ),
                            List.of(
                                    new ImageData(Identifier.of("alkimia", "textures/gui/crafting_table_recipe.png"), new Vec2f(100, 105), new Vec2f(112, 68)),
                                    new ImageData(Identifier.of("alkimia", "textures/gui/decorative_frame.png"), new Vec2f(250, 45), new Vec2f(38, 28))
                            ),
                            List.of(
                                    new ItemData(Identifier.of("minecraft", "textures/item/charcoal.png"), new Vec2f(104, 131), new Vec2f(16, 16)),
                                    new ItemData(Identifier.of("minecraft", "textures/item/charcoal.png"), new Vec2f(126, 131), new Vec2f(16, 16)),
                                    new ItemData(Identifier.of("minecraft", "textures/item/charcoal.png"), new Vec2f(148, 131), new Vec2f(16, 16)),
                                    new ItemData(Identifier.of("minecraft", "textures/item/charcoal.png"), new Vec2f(148, 153), new Vec2f(16, 16)),
                                    new ItemData(Identifier.of("minecraft", "textures/item/blaze_powder.png"), new Vec2f(126, 153), new Vec2f(16, 16)),
                                    new ItemData(Identifier.of("minecraft", "textures/item/gunpowder.png"), new Vec2f(104, 153), new Vec2f(16, 16)),
                                    new ItemData(Identifier.of("alkimia", "textures/item/flash_powder.png"), new Vec2f(192, 131), new Vec2f(16, 16)),
                                    new ItemData(Identifier.of("alkimia", "textures/item/flash_powder.png"), new Vec2f(261, 51), new Vec2f(16, 16))
                            ),
                            3
                    )), "Flash powder", "Smelting"
            ),
            new EntryData(
                    List.of(new PageData(
                            List.of(
                                    new TextData(Text.translatable("guidebook.alkimia.temporal_TODO"), new Vec2f(100, 45))
                            ),
                            List.of(

                            ),
                            List.of(

                            ),
                            4
                    )), "TODO", "Work"
            ),
            new EntryData(
                    List.of(new PageData(
                            List.of(
                                    new TextData(Text.translatable("guidebook.alkimia.darkvision_brew1"), new Vec2f(100, 45)),
                                    new TextData(Text.translatable("guidebook.alkimia.darkvision_brew2"), new Vec2f(100, 55)),
                                    new TextData(Text.translatable("guidebook.alkimia.darkvision_brew3"), new Vec2f(100, 65)),
                                    new TextData(Text.translatable("guidebook.alkimia.darkvision_brew4"), new Vec2f(100, 75)),
                                    new TextData(Text.translatable("guidebook.alkimia.darkvision_brew5"), new Vec2f(100, 85)),
                                    new TextData(Text.translatable("guidebook.alkimia.darkvision_brew6"), new Vec2f(100, 95))
                            ),
                            List.of(
                                    new ImageData(Identifier.of("alkimia", "textures/gui/crucible_recipe.png"), new Vec2f(220, 75), new Vec2f(120, 120))
                            ),
                            List.of(
                                    new ItemData(Identifier.of("alkimia", "textures/item/night_vision_potion.png"), new Vec2f(272, 85), new Vec2f(16, 16)),
                                    new ItemData(Identifier.of("alkimia", "textures/block/diamond_dust_still.png"), new Vec2f(272, 159), new Vec2f(16, 16)),
                                    new ItemData(Identifier.of("minecraft", "textures/item/spider_eye.png"), new Vec2f(272, 115), new Vec2f(16, 16)),
                                    new ItemData(Identifier.of("alkimia", "textures/item/glow_full.png"), new Vec2f(259, 115), new Vec2f(8, 8)),
                                    new ItemData(Identifier.of("alkimia", "textures/item/senses_full.png"), new Vec2f(293, 115), new Vec2f(8, 8))
                            ),
                            5
                    )), "Darkvision Brew", "Night"
            )
    ));
}
