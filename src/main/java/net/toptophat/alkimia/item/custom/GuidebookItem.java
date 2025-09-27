package net.toptophat.alkimia.item.custom;

import net.toptophat.alkimia.gui.ModGuideScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class GuidebookItem extends Item{
    public GuidebookItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient) {
            MinecraftClient.getInstance().setScreen(new ModGuideScreen(Text.translatable("guidebook.alkimia.title"), user.getStackInHand(hand)));
        }
        return TypedActionResult.success(user.getStackInHand(hand));
    }
}
