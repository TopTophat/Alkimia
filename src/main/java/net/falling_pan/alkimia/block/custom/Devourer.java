package net.falling_pan.alkimia.block.custom;

import net.falling_pan.alkimia.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
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

public class Devourer extends Block {
    public static final IntProperty LIFESPAN = IntProperty.of("lifespan", 0, 7);
    public Devourer(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(LIFESPAN, 7));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(LIFESPAN);
    }

    @Override
    protected void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        world.scheduleBlockTick(pos, state.getBlock(), 3);
        super.onBlockAdded(state, world, pos, oldState, notify);
    }


    @Override
    protected void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        world.setBlockState(pos, state.with(LIFESPAN, state.get(LIFESPAN) - 1));
        if (state.get(LIFESPAN) < 2)
        {
            world.setBlockState(pos, Blocks.AIR.getDefaultState());
        }
        if (!world.getBlockState(pos.down(1)).isAir() && world.getBlockState(pos.down(1)).getBlock() != ModBlocks.DEVOURER && random.nextInt() % 2 == 0)
        {
            world.setBlockState(pos.down(1), ModBlocks.DEVOURER.getDefaultState());
            world.playSound(null, pos.down(1), SoundEvents.BLOCK_SCULK_SHRIEKER_SHRIEK, SoundCategory.BLOCKS);
        }
        if (!world.getBlockState(pos.up(1)).isAir() && world.getBlockState(pos.up(1)).getBlock() != ModBlocks.DEVOURER && random.nextInt() % 2 == 0)
        {
            world.setBlockState(pos.up(1), ModBlocks.DEVOURER.getDefaultState());
            world.playSound(null, pos.up(1), SoundEvents.BLOCK_SCULK_SHRIEKER_SHRIEK, SoundCategory.BLOCKS);
        }
        if (!world.getBlockState(pos.east(1)).isAir() && world.getBlockState(pos.east(1)).getBlock() != ModBlocks.DEVOURER && random.nextInt() % 2 == 0)
        {
            world.setBlockState(pos.east(1), ModBlocks.DEVOURER.getDefaultState());
            world.playSound(null, pos.east(1), SoundEvents.BLOCK_SCULK_SHRIEKER_SHRIEK, SoundCategory.BLOCKS);
        }
        if (!world.getBlockState(pos.west(1)).isAir() && world.getBlockState(pos.west(1)).getBlock() != ModBlocks.DEVOURER && random.nextInt() % 2 == 0)
        {
            world.setBlockState(pos.west(1), ModBlocks.DEVOURER.getDefaultState());
            world.playSound(null, pos.west(1), SoundEvents.BLOCK_SCULK_SHRIEKER_SHRIEK, SoundCategory.BLOCKS);
        }
        if (!world.getBlockState(pos.north(1)).isAir() && world.getBlockState(pos.north(1)).getBlock() != ModBlocks.DEVOURER && random.nextInt() % 2 == 0)
        {
            world.setBlockState(pos.north(1), ModBlocks.DEVOURER.getDefaultState());
            world.playSound(null, pos.north(1), SoundEvents.BLOCK_SCULK_SHRIEKER_SHRIEK, SoundCategory.BLOCKS);
        }
        if (!world.getBlockState(pos.south(1)).isAir() && world.getBlockState(pos.south(1)).getBlock() != ModBlocks.DEVOURER && random.nextInt() % 2 == 0)
        {
            world.setBlockState(pos.south(1), ModBlocks.DEVOURER.getDefaultState());
            world.playSound(null, pos.south(1), SoundEvents.BLOCK_SCULK_SHRIEKER_SHRIEK, SoundCategory.BLOCKS);
        }
        world.scheduleBlockTick(pos, state.getBlock(), 3);
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
        tooltip.add(Text.translatable("tooltip.alkimia.devourer.tooltip0"));
        tooltip.add(Text.translatable("tooltip.alkimia.devourer.tooltip1"));
        super.appendTooltip(stack, context, tooltip, options);
    }
}
