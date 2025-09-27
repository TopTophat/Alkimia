package net.falling_pan.alkimia.block.custom;

import net.falling_pan.alkimia.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import java.util.List;

public class Plague extends Block {
    public static final IntProperty CHUNKSPREADS_LEFT = IntProperty.of("chunkspreads_left", 0, 1);
    public Plague(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(CHUNKSPREADS_LEFT, 1));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(CHUNKSPREADS_LEFT);
    }

    @Override
    protected void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        world.scheduleBlockTick(pos, state.getBlock(), 4);
        super.onBlockAdded(state, world, pos, oldState, notify);
    }


    @Override
    protected void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        boolean isAllPlague;
        isAllPlague = true;
        if (!world.getBlockState(pos.down(1)).isAir() && world.getBlockState(pos.down(1)).getBlock() != ModBlocks.PLAGUE && world.getBlockState(pos.down(1)).getBlock() != ModBlocks.DEAD_PLAGUE)
        {
            isAllPlague = false;
            if (random.nextInt() % 2 == 0)
            {
                world.setBlockState(pos.down(1), ModBlocks.PLAGUE.getDefaultState().with(CHUNKSPREADS_LEFT, state.get(CHUNKSPREADS_LEFT)));
                world.playSound(null, pos.down(1), SoundEvents.BLOCK_SCULK_SHRIEKER_SHRIEK, SoundCategory.BLOCKS);
            }
        }
        if (!world.getBlockState(pos.up(1)).isAir() && world.getBlockState(pos.up(1)).getBlock() != ModBlocks.PLAGUE && world.getBlockState(pos.up(1)).getBlock() != ModBlocks.DEAD_PLAGUE)
        {
            isAllPlague = false;
            if (random.nextInt() % 2 == 0)
            {
                world.setBlockState(pos.up(1), ModBlocks.PLAGUE.getDefaultState().with(CHUNKSPREADS_LEFT, state.get(CHUNKSPREADS_LEFT)));
                world.playSound(null, pos.up(1), SoundEvents.BLOCK_SCULK_SHRIEKER_SHRIEK, SoundCategory.BLOCKS);
            }
        }
        if (!world.getBlockState(pos.east(1)).isAir() && world.getBlockState(pos.east(1)).getBlock() != ModBlocks.PLAGUE && world.getBlockState(pos.east(1)).getBlock() != ModBlocks.DEAD_PLAGUE && world.getChunk(pos.east()) == world.getChunk(pos) || state.get(CHUNKSPREADS_LEFT) > 0 && !world.getBlockState(pos.east(1)).isAir() && world.getBlockState(pos.east(1)).getBlock() != ModBlocks.PLAGUE && world.getBlockState(pos.east(1)).getBlock() != ModBlocks.DEAD_PLAGUE)
        {
            isAllPlague = false;
            if (random.nextInt() % 2 == 0)
            {
                if (world.getChunk(pos.east()) != world.getChunk(pos))
                {
                    world.setBlockState(pos.east(1), ModBlocks.PLAGUE.getDefaultState().with(CHUNKSPREADS_LEFT, state.get(CHUNKSPREADS_LEFT) - 1));
                    world.playSound(null, pos.east(1), SoundEvents.BLOCK_SCULK_SHRIEKER_SHRIEK, SoundCategory.BLOCKS);
                }
                else if (world.getChunk(pos.east()) == world.getChunk(pos))
                {
                    world.setBlockState(pos.east(1), ModBlocks.PLAGUE.getDefaultState().with(CHUNKSPREADS_LEFT, state.get(CHUNKSPREADS_LEFT)));
                    world.playSound(null, pos.east(1), SoundEvents.BLOCK_SCULK_SHRIEKER_SHRIEK, SoundCategory.BLOCKS);
                }
            }
        }
        if (!world.getBlockState(pos.west(1)).isAir() && world.getBlockState(pos.west(1)).getBlock() != ModBlocks.PLAGUE && world.getBlockState(pos.west(1)).getBlock() != ModBlocks.DEAD_PLAGUE && world.getChunk(pos.west()) == world.getChunk(pos) || state.get(CHUNKSPREADS_LEFT) > 0 && !world.getBlockState(pos.west(1)).isAir() && world.getBlockState(pos.west(1)).getBlock() != ModBlocks.PLAGUE && world.getBlockState(pos.west(1)).getBlock() != ModBlocks.DEAD_PLAGUE)
        {
            isAllPlague = false;
            if (random.nextInt() % 2 == 0)
            {
                if (world.getChunk(pos.west()) != world.getChunk(pos))
                {
                    world.setBlockState(pos.west(1), ModBlocks.PLAGUE.getDefaultState().with(CHUNKSPREADS_LEFT, state.get(CHUNKSPREADS_LEFT) - 1));
                    world.playSound(null, pos.west(1), SoundEvents.BLOCK_SCULK_SHRIEKER_SHRIEK, SoundCategory.BLOCKS);
                }
                else if (world.getChunk(pos.west()) == world.getChunk(pos))
                {
                    world.setBlockState(pos.west(1), ModBlocks.PLAGUE.getDefaultState().with(CHUNKSPREADS_LEFT, state.get(CHUNKSPREADS_LEFT)));
                    world.playSound(null, pos.west(1), SoundEvents.BLOCK_SCULK_SHRIEKER_SHRIEK, SoundCategory.BLOCKS);
                }
            }
        }
        if (!world.getBlockState(pos.north(1)).isAir() && world.getBlockState(pos.north(1)).getBlock() != ModBlocks.PLAGUE && world.getBlockState(pos.north(1)).getBlock() != ModBlocks.DEAD_PLAGUE && world.getChunk(pos.north()) == world.getChunk(pos) || state.get(CHUNKSPREADS_LEFT) > 0 && !world.getBlockState(pos.north(1)).isAir() && world.getBlockState(pos.north(1)).getBlock() != ModBlocks.PLAGUE && world.getBlockState(pos.north(1)).getBlock() != ModBlocks.DEAD_PLAGUE)
        {
            isAllPlague = false;
            if (random.nextInt() % 2 == 0)
            {
                if (world.getChunk(pos.north()) != world.getChunk(pos))
                {
                    world.setBlockState(pos.north(1), ModBlocks.PLAGUE.getDefaultState().with(CHUNKSPREADS_LEFT, state.get(CHUNKSPREADS_LEFT) - 1));
                    world.playSound(null, pos.north(1), SoundEvents.BLOCK_SCULK_SHRIEKER_SHRIEK, SoundCategory.BLOCKS);
                }
                else if (world.getChunk(pos.north()) == world.getChunk(pos))
                {
                    world.setBlockState(pos.north(1), ModBlocks.PLAGUE.getDefaultState().with(CHUNKSPREADS_LEFT, state.get(CHUNKSPREADS_LEFT)));
                    world.playSound(null, pos.north(1), SoundEvents.BLOCK_SCULK_SHRIEKER_SHRIEK, SoundCategory.BLOCKS);
                }
            }
        }
        if (!world.getBlockState(pos.south(1)).isAir() && world.getBlockState(pos.south(1)).getBlock() != ModBlocks.PLAGUE && world.getBlockState(pos.south(1)).getBlock() != ModBlocks.DEAD_PLAGUE && world.getChunk(pos.south()) == world.getChunk(pos) || state.get(CHUNKSPREADS_LEFT) > 0 && !world.getBlockState(pos.south(1)).isAir() && world.getBlockState(pos.south(1)).getBlock() != ModBlocks.PLAGUE && world.getBlockState(pos.south(1)).getBlock() != ModBlocks.DEAD_PLAGUE)
        {
            isAllPlague = false;
            if (random.nextInt() % 2 == 0)
            {
                if (world.getChunk(pos.south()) != world.getChunk(pos))
                {
                    world.setBlockState(pos.south(1), ModBlocks.PLAGUE.getDefaultState().with(CHUNKSPREADS_LEFT, state.get(CHUNKSPREADS_LEFT) - 1));
                    world.playSound(null, pos.south(1), SoundEvents.BLOCK_SCULK_SHRIEKER_SHRIEK, SoundCategory.BLOCKS);
                }
                else if (world.getChunk(pos.south()) == world.getChunk(pos))
                {
                    world.setBlockState(pos.south(1), ModBlocks.PLAGUE.getDefaultState().with(CHUNKSPREADS_LEFT, state.get(CHUNKSPREADS_LEFT)));
                    world.playSound(null, pos.south(1), SoundEvents.BLOCK_SCULK_SHRIEKER_SHRIEK, SoundCategory.BLOCKS);
                }
            }
        }
        if (isAllPlague)
        {
            world.setBlockState(pos, ModBlocks.DEAD_PLAGUE.getDefaultState());
        }
        world.scheduleBlockTick(pos, state.getBlock(), 4);
        super.scheduledTick(state, world, pos, random);
    }

    @Override
    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!world.isClient)
        {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 200, 1));
        }
        return super.onBreak(world, pos, state, player);
    }

    @Override
    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
        if (entity.isLiving())
        {
            LivingEntity target = (LivingEntity)entity;
            target.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 200, 1));
        }
        super.onSteppedOn(world, pos, state, entity);
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType options) {
        tooltip.add(Text.translatable("tooltip.alkimia.plague.tooltip0"));
        tooltip.add(Text.translatable("tooltip.alkimia.plague.tooltip1"));
        super.appendTooltip(stack, context, tooltip, options);
    }
}
