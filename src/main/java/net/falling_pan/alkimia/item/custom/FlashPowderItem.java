package net.falling_pan.alkimia.item.custom;

import net.falling_pan.alkimia.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;

import java.util.List;
import java.util.Map;

public class FlashPowderItem extends Item {
    private static Map<Block, Block> FLASH_POWDER_MAP;

    public FlashPowderItem(Settings settings) {
        super(settings);
    }

    public static void InitMaps()
    {
        FLASH_POWDER_MAP =
                Map.of(
                        Blocks.RAW_IRON_BLOCK, Blocks.IRON_BLOCK,
                        Blocks.RAW_GOLD_BLOCK, Blocks.GOLD_BLOCK,
                        Blocks.RAW_COPPER_BLOCK, Blocks.COPPER_BLOCK,
                        Blocks.SAND, Blocks.GLASS,
                        ModBlocks.CINNABAR_BLOCK, ModBlocks.QUICKSILVER_TANK
                );
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        Block clickedBlock = world.getBlockState(context.getBlockPos()).getBlock();

        if(FLASH_POWDER_MAP.containsKey(clickedBlock))
        {
            if(!world.isClient())
            {
                world.setBlockState(context.getBlockPos(), FLASH_POWDER_MAP.get(clickedBlock).getDefaultState());

                context.getStack().decrement(1);

                //context.getStack().damage(1, ((ServerWorld) world), ((ServerPlayerEntity) context.getPlayer()), item -> context.getPlayer().sendEquipmentBreakStatus(item, EquipmentSlot.MAINHAND));

                world.playSound(null, context.getBlockPos(), SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS);
            }
        }

        return ActionResult.SUCCESS;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        if (Screen.hasShiftDown())
        {
            tooltip.add(Text.translatable("tooltip.alchemymod.flash_powder.shift0"));
            tooltip.add(Text.translatable("tooltip.alchemymod.flash_powder.shift1"));
        }
        else
        {
            tooltip.add(Text.translatable("tooltip.alchemymod.flash_powder.noshift"));
        }
        super.appendTooltip(stack, context, tooltip, type);
    }
}
