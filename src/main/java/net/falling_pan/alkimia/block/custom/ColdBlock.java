package net.falling_pan.alkimia.block.custom;

import net.falling_pan.alkimia.block.ModBlocks;
import net.falling_pan.alkimia.util.ModTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import java.util.List;

public class ColdBlock extends Block {
    public ColdBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        world.scheduleBlockTick(pos, state.getBlock(), 1);
        super.onBlockAdded(state, world, pos, oldState, notify);
    }

    @Override
    protected void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (world.getBlockState(pos.down(1)).isIn(ModTags.Blocks.EVERFROST_TARGETS) && random.nextInt() % 2 == 0)
        {
            world.setBlockState(pos.down(1), ModBlocks.COLD_BLOCK.getDefaultState());
            world.playSound(null, pos.down(1), SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME, SoundCategory.BLOCKS);
        }
        if (world.getBlockState(pos.up(1)).isIn(ModTags.Blocks.EVERFROST_TARGETS) && random.nextInt() % 2 == 0)
        {
            world.setBlockState(pos.up(1), ModBlocks.COLD_BLOCK.getDefaultState());
            world.playSound(null, pos.up(1), SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME, SoundCategory.BLOCKS);
        }
        if (world.getBlockState(pos.east(1)).isIn(ModTags.Blocks.EVERFROST_TARGETS) && random.nextInt() % 2 == 0)
        {
            world.setBlockState(pos.east(1), ModBlocks.COLD_BLOCK.getDefaultState());
            world.playSound(null, pos.east(1), SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME, SoundCategory.BLOCKS);
        }
        if (world.getBlockState(pos.west(1)).isIn(ModTags.Blocks.EVERFROST_TARGETS) && random.nextInt() % 2 == 0)
        {
            world.setBlockState(pos.west(1), ModBlocks.COLD_BLOCK.getDefaultState());
            world.playSound(null, pos.west(1), SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME, SoundCategory.BLOCKS);
        }
        if (world.getBlockState(pos.north(1)).isIn(ModTags.Blocks.EVERFROST_TARGETS) && random.nextInt() % 2 == 0)
        {
            world.setBlockState(pos.north(1), ModBlocks.COLD_BLOCK.getDefaultState());
            world.playSound(null, pos.north(1), SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME, SoundCategory.BLOCKS);
        }
        if (world.getBlockState(pos.south(1)).isIn(ModTags.Blocks.EVERFROST_TARGETS) && random.nextInt() % 2 == 0)
        {
            world.setBlockState(pos.south(1), ModBlocks.COLD_BLOCK.getDefaultState());
            world.playSound(null, pos.south(1), SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME, SoundCategory.BLOCKS);
        }
        world.scheduleBlockTick(pos, state.getBlock(), 1);
        super.scheduledTick(state, world, pos, random);
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType options) {
        tooltip.add(Text.translatable("tooltip.alkimia.cold_block.tooltip0"));
        tooltip.add(Text.translatable("tooltip.alkimia.cold_block.tooltip1"));
        super.appendTooltip(stack, context, tooltip, options);
    }
}
