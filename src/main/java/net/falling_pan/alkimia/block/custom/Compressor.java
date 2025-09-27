package net.falling_pan.alkimia.block.custom;

import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.falling_pan.alkimia.block.entity.custom.CompressorBlockEntity;
import net.falling_pan.alkimia.component.ModDataComponentTypes;
import net.falling_pan.alkimia.fluid.ModFluids;
import net.falling_pan.alkimia.util.TickableBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Compressor extends BlockWithEntity implements BlockEntityProvider {
    public static final BooleanProperty IS_WORKING = BooleanProperty.of("is_working");
    public static final MapCodec<Compressor> CODEC = Compressor.createCodec(Compressor::new);
    public static List<CompressingRecipe> compressor_recipe_book;

    public static void InitRecipes() {
        compressor_recipe_book = List.of(
            new CompressingRecipe(List.of(FluidVariant.of(ModFluids.DIAMOND_DUST), FluidVariant.of(Fluids.LAVA), FluidVariant.of(ModFluids.SOUL), FluidVariant.of(ModFluids.LIQUID_SOUL)), List.of(250, 250, 250, 250), List.of(Items.QUARTZ, Items.QUARTZ, Items.QUARTZ, Items.QUARTZ, Items.QUARTZ), List.of(Items.DIAMOND, Items.AIR, Items.AIR, Items.AIR, Items.AIR), 200)
        );
    }

    public Compressor(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(IS_WORKING, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(IS_WORKING);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CompressorBlockEntity(pos, state);
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    protected void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof CompressorBlockEntity) {
                ItemScatterer.spawn(world, pos, ((CompressorBlockEntity) blockEntity));
                world.updateComparators(pos, this);
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType
            options) {
        tooltip.add(Text.translatable("tooltip.alkimia.compressor.tooltip"));
        super.appendTooltip(stack, context, tooltip, options);
    }

    @Override
    protected ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient)
        {
            if (world.getBlockEntity(pos) instanceof CompressorBlockEntity compressor && !state.get(IS_WORKING))
            {
                if (stack.get(ModDataComponentTypes.AMOUNT) != null)
                {
                    int amount = stack.get(ModDataComponentTypes.AMOUNT);
                    int capacity = stack.get(ModDataComponentTypes.CAPACITY);
                    FluidVariant fluid = stack.get(ModDataComponentTypes.STORED_FLUID);

                    if (fluid == FluidVariant.blank() && (compressor.amount1 > 0 || compressor.amount2 > 0 || compressor.amount3 > 0 || compressor.amount4 > 0))
                    {
                        if (compressor.amount1 > 0)
                        {
                            int transferFluid = Math.min(capacity, compressor.amount1);
                            compressor.amount1 -= transferFluid;
                            stack.set(ModDataComponentTypes.AMOUNT, transferFluid);
                            stack.set(ModDataComponentTypes.STORED_FLUID, compressor.storedFluid1);
                            if (compressor.amount1 <= 0)
                            {
                                compressor.storedFluid1 = FluidVariant.blank();
                            }
                        }
                        else if(compressor.amount2 > 0)
                        {
                            int transferFluid = Math.min(capacity, compressor.amount2);
                            compressor.amount2 -= transferFluid;
                            stack.set(ModDataComponentTypes.AMOUNT, transferFluid);
                            stack.set(ModDataComponentTypes.STORED_FLUID, compressor.storedFluid2);
                            if (compressor.amount2 <= 0)
                            {
                                compressor.storedFluid2 = FluidVariant.blank();
                            }
                        }
                        else if(compressor.amount3 > 0)
                        {
                            int transferFluid = Math.min(capacity, compressor.amount3);
                            compressor.amount3 -= transferFluid;
                            stack.set(ModDataComponentTypes.AMOUNT, transferFluid);
                            stack.set(ModDataComponentTypes.STORED_FLUID, compressor.storedFluid3);
                            if (compressor.amount3 <= 0)
                            {
                                compressor.storedFluid3 = FluidVariant.blank();
                            }
                        }
                        else {
                            int transferFluid = Math.min(capacity, compressor.amount4);
                            compressor.amount4 -= transferFluid;
                            stack.set(ModDataComponentTypes.AMOUNT, transferFluid);
                            stack.set(ModDataComponentTypes.STORED_FLUID, compressor.storedFluid4);
                            if (compressor.amount4 <= 0)
                            {
                                compressor.storedFluid4 = FluidVariant.blank();
                            }
                        }
                        compressor.markDirty();
                        world.updateListeners(pos, state, state, Block.NOTIFY_ALL);
                    }
                    else if((fluid == compressor.storedFluid1 || fluid == compressor.storedFluid2 || fluid == compressor.storedFluid3 || fluid == compressor.storedFluid4) && compressor.amount1 + compressor.amount2 + compressor.amount3 + compressor.amount4 < compressor.capacity)
                    {
                        if (fluid == compressor.storedFluid1)
                        {
                            int transferFluid = Math.min(amount, compressor.capacity - (compressor.amount1 + compressor.amount2 + compressor.amount3 + compressor.amount4));
                            compressor.amount1 += transferFluid;
                            stack.set(ModDataComponentTypes.AMOUNT, amount - transferFluid);
                            if (amount - transferFluid == 0)
                            {
                                stack.set(ModDataComponentTypes.STORED_FLUID, FluidVariant.blank());
                            }
                        }
                        else if(fluid == compressor.storedFluid2)
                        {
                            int transferFluid = Math.min(amount, compressor.capacity - (compressor.amount1 + compressor.amount2 + compressor.amount3 + compressor.amount4));
                            compressor.amount2 += transferFluid;
                            stack.set(ModDataComponentTypes.AMOUNT, amount - transferFluid);
                            if (amount - transferFluid == 0)
                            {
                                stack.set(ModDataComponentTypes.STORED_FLUID, FluidVariant.blank());
                            }
                        }
                        else if(fluid == compressor.storedFluid3)
                        {
                            int transferFluid = Math.min(amount, compressor.capacity - (compressor.amount1 + compressor.amount2 + compressor.amount3 + compressor.amount4));
                            compressor.amount3 += transferFluid;
                            stack.set(ModDataComponentTypes.AMOUNT, amount - transferFluid);
                            if (amount - transferFluid == 0)
                            {
                                stack.set(ModDataComponentTypes.STORED_FLUID, FluidVariant.blank());
                            }
                        }
                        else {
                            int transferFluid = Math.min(amount, compressor.capacity - (compressor.amount1 + compressor.amount2 + compressor.amount3 + compressor.amount4));
                            compressor.amount4 += transferFluid;
                            stack.set(ModDataComponentTypes.AMOUNT, amount - transferFluid);
                            if (amount - transferFluid == 0)
                            {
                                stack.set(ModDataComponentTypes.STORED_FLUID, FluidVariant.blank());
                            }
                        }
                        compressor.markDirty();
                        world.updateListeners(pos, state, state, Block.NOTIFY_ALL);
                    }
                    else if(FluidVariant.blank() == compressor.storedFluid1 || FluidVariant.blank() == compressor.storedFluid2 || FluidVariant.blank() == compressor.storedFluid3 || FluidVariant.blank() == compressor.storedFluid4)
                    {
                        if (FluidVariant.blank() == compressor.storedFluid1)
                        {
                            int transferFluid = Math.min(amount, compressor.capacity - (compressor.amount1 + compressor.amount2 + compressor.amount3 + compressor.amount4));
                            compressor.amount1 += transferFluid;
                            compressor.storedFluid1 = fluid;
                            stack.set(ModDataComponentTypes.AMOUNT, amount - transferFluid);
                            if (amount - transferFluid == 0)
                            {
                                stack.set(ModDataComponentTypes.STORED_FLUID, FluidVariant.blank());
                            }
                        }
                        else if(FluidVariant.blank() == compressor.storedFluid2)
                        {
                            int transferFluid = Math.min(amount, compressor.capacity - (compressor.amount1 + compressor.amount2 + compressor.amount3 + compressor.amount4));
                            compressor.amount2 += transferFluid;
                            compressor.storedFluid2 = fluid;
                            stack.set(ModDataComponentTypes.AMOUNT, amount - transferFluid);
                            if (amount - transferFluid == 0)
                            {
                                stack.set(ModDataComponentTypes.STORED_FLUID, FluidVariant.blank());
                            }
                        }
                        else if(FluidVariant.blank() == compressor.storedFluid3)
                        {
                            int transferFluid = Math.min(amount, compressor.capacity - (compressor.amount1 + compressor.amount2 + compressor.amount3 + compressor.amount4));
                            compressor.amount3 += transferFluid;
                            compressor.storedFluid3 = fluid;
                            stack.set(ModDataComponentTypes.AMOUNT, amount - transferFluid);
                            if (amount - transferFluid == 0)
                            {
                                stack.set(ModDataComponentTypes.STORED_FLUID, FluidVariant.blank());
                            }
                        }
                        else {
                            int transferFluid = Math.min(amount, compressor.capacity - (compressor.amount1 + compressor.amount2 + compressor.amount3 + compressor.amount4));
                            compressor.amount4 += transferFluid;
                            compressor.storedFluid4 = fluid;
                            stack.set(ModDataComponentTypes.AMOUNT, amount - transferFluid);
                            if (amount - transferFluid == 0)
                            {
                                stack.set(ModDataComponentTypes.STORED_FLUID, FluidVariant.blank());
                            }
                        }
                        compressor.markDirty();
                        world.updateListeners(pos, state, state, Block.NOTIFY_ALL);
                    }
                }
                else if((compressor.getStack(0).isEmpty() || compressor.getStack(1).isEmpty() || compressor.getStack(2).isEmpty() || compressor.getStack(3).isEmpty() || compressor.getStack(4).isEmpty()) && !player.getMainHandStack().isEmpty())
                {
                    if (compressor.getStack(0).isEmpty())
                    {
                        compressor.setStack(0, stack.copyWithCount(1));
                        stack.decrement(1);
                    }
                    else if (compressor.getStack(1).isEmpty())
                    {
                        compressor.setStack(1, stack.copyWithCount(1));
                        stack.decrement(1);
                    }
                    else if (compressor.getStack(2).isEmpty())
                    {
                        compressor.setStack(2, stack.copyWithCount(1));
                        stack.decrement(1);
                    }
                    else if (compressor.getStack(3).isEmpty())
                    {
                        compressor.setStack(3, stack.copyWithCount(1));
                        stack.decrement(1);
                    }
                    else if (compressor.getStack(4).isEmpty())
                    {
                        compressor.setStack(4, stack.copyWithCount(1));
                        stack.decrement(1);
                    }
                    compressor.markDirty();
                    world.updateListeners(pos, state, state, Block.NOTIFY_ALL);
                }
                else if (player.getMainHandStack().isEmpty())
                {
                    player.giveItemStack(compressor.getStack(0));
                    player.giveItemStack(compressor.getStack(1));
                    player.giveItemStack(compressor.getStack(2));
                    player.giveItemStack(compressor.getStack(3));
                    player.giveItemStack(compressor.getStack(4));
                    compressor.clear();
                    compressor.markDirty();
                    world.updateListeners(pos, state, state, Block.NOTIFY_ALL);
                }
            }
        }
        return ItemActionResult.SUCCESS;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return TickableBlockEntity.getTicker(world);
    }
}
