package net.toptophat.alkimia.block.custom;

import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.toptophat.alkimia.block.entity.custom.DistillerInputBlockEntity;
import net.toptophat.alkimia.component.ModDataComponentTypes;
import net.toptophat.alkimia.fluid.ModFluids;
import net.toptophat.alkimia.util.TickableBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DistillerInput extends BlockWithEntity implements BlockEntityProvider {
    public static final MapCodec<DistillerInput> CODEC = DistillerInput.createCodec(DistillerInput::new);
    public static List<DistillerRecipe> distiller_recipe_book;

    public static void InitRecipes() {
        distiller_recipe_book = List.of(
                new DistillerRecipe(List.of(FluidVariant.of(ModFluids.SOUL)), List.of(10), List.of(FluidVariant.of(ModFluids.LIQUID_SOUL)), List.of(1), List.of(), List.of(Items.AIR, Items.AIR, Items.AIR, Items.AIR, Items.AIR), 20),
                new DistillerRecipe(List.of(FluidVariant.of(ModFluids.SOUL), FluidVariant.of(ModFluids.DIAMOND_DUST)), List.of(100, 10), List.of(FluidVariant.of(ModFluids.LIQUID_SOUL)), List.of(50), List.of(Items.LAPIS_LAZULI), List.of(Items.QUARTZ, Items.AIR, Items.AIR, Items.AIR, Items.AIR), 200),
                new DistillerRecipe(List.of(), List.of(), List.of(FluidVariant.of(Fluids.LAVA)), List.of(100), List.of(Blocks.MAGMA_BLOCK.asItem()), List.of(Blocks.NETHERRACK.asItem(), Items.AIR, Items.AIR, Items.AIR, Items.AIR), 400)
        );
    }

    public DistillerInput(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH));
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new DistillerInputBlockEntity(pos, state);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(Properties.HORIZONTAL_FACING);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return super.getPlacementState(ctx).with(Properties.HORIZONTAL_FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    protected void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof DistillerInputBlockEntity) {
                ItemScatterer.spawn(world, pos, ((DistillerInputBlockEntity) blockEntity));
                world.updateComparators(pos, this);
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType
            options) {
        tooltip.add(Text.translatable("tooltip.alkimia.distiller_input.tooltip"));
        super.appendTooltip(stack, context, tooltip, options);
    }

    @Override
    protected ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient)
        {
            if (world.getBlockEntity(pos) instanceof DistillerInputBlockEntity distiller)
            {
                if (stack.get(ModDataComponentTypes.AMOUNT) != null)
                {
                    int amount = stack.get(ModDataComponentTypes.AMOUNT);
                    int capacity = stack.get(ModDataComponentTypes.CAPACITY);
                    FluidVariant fluid = stack.get(ModDataComponentTypes.STORED_FLUID);

                    if (fluid == FluidVariant.blank() && (distiller.amount1 > 0 || distiller.amount2 > 0 || distiller.amount3 > 0 || distiller.amount4 > 0))
                    {
                        if (distiller.amount1 > 0)
                        {
                            int transferFluid = Math.min(capacity, distiller.amount1);
                            distiller.amount1 -= transferFluid;
                            stack.set(ModDataComponentTypes.AMOUNT, transferFluid);
                            stack.set(ModDataComponentTypes.STORED_FLUID, distiller.storedFluid1);
                            if (distiller.amount1 <= 0)
                            {
                                distiller.storedFluid1 = FluidVariant.blank();
                            }
                        }
                        else if(distiller.amount2 > 0)
                        {
                            int transferFluid = Math.min(capacity, distiller.amount2);
                            distiller.amount2 -= transferFluid;
                            stack.set(ModDataComponentTypes.AMOUNT, transferFluid);
                            stack.set(ModDataComponentTypes.STORED_FLUID, distiller.storedFluid2);
                            if (distiller.amount2 <= 0)
                            {
                                distiller.storedFluid2 = FluidVariant.blank();
                            }
                        }
                        else if(distiller.amount3 > 0)
                        {
                            int transferFluid = Math.min(capacity, distiller.amount3);
                            distiller.amount3 -= transferFluid;
                            stack.set(ModDataComponentTypes.AMOUNT, transferFluid);
                            stack.set(ModDataComponentTypes.STORED_FLUID, distiller.storedFluid3);
                            if (distiller.amount3 <= 0)
                            {
                                distiller.storedFluid3 = FluidVariant.blank();
                            }
                        }
                        else {
                            int transferFluid = Math.min(capacity, distiller.amount4);
                            distiller.amount4 -= transferFluid;
                            stack.set(ModDataComponentTypes.AMOUNT, transferFluid);
                            stack.set(ModDataComponentTypes.STORED_FLUID, distiller.storedFluid4);
                            if (distiller.amount4 <= 0)
                            {
                                distiller.storedFluid4 = FluidVariant.blank();
                            }
                        }
                        distiller.markDirty();
                        world.updateListeners(pos, state, state, Block.NOTIFY_ALL);
                    }
                    else if((fluid == distiller.storedFluid1 || fluid == distiller.storedFluid2 || fluid == distiller.storedFluid3 || fluid == distiller.storedFluid4) && distiller.amount1 + distiller.amount2 + distiller.amount3 + distiller.amount4 < distiller.capacity)
                    {
                        if (fluid == distiller.storedFluid1)
                        {
                            int transferFluid = Math.min(amount, distiller.capacity - (distiller.amount1 + distiller.amount2 + distiller.amount3 + distiller.amount4));
                            distiller.amount1 += transferFluid;
                            stack.set(ModDataComponentTypes.AMOUNT, amount - transferFluid);
                            if (amount - transferFluid == 0)
                            {
                                stack.set(ModDataComponentTypes.STORED_FLUID, FluidVariant.blank());
                            }
                        }
                        else if(fluid == distiller.storedFluid2)
                        {
                            int transferFluid = Math.min(amount, distiller.capacity - (distiller.amount1 + distiller.amount2 + distiller.amount3 + distiller.amount4));
                            distiller.amount2 += transferFluid;
                            stack.set(ModDataComponentTypes.AMOUNT, amount - transferFluid);
                            if (amount - transferFluid == 0)
                            {
                                stack.set(ModDataComponentTypes.STORED_FLUID, FluidVariant.blank());
                            }
                        }
                        else if(fluid == distiller.storedFluid3)
                        {
                            int transferFluid = Math.min(amount, distiller.capacity - (distiller.amount1 + distiller.amount2 + distiller.amount3 + distiller.amount4));
                            distiller.amount3 += transferFluid;
                            stack.set(ModDataComponentTypes.AMOUNT, amount - transferFluid);
                            if (amount - transferFluid == 0)
                            {
                                stack.set(ModDataComponentTypes.STORED_FLUID, FluidVariant.blank());
                            }
                        }
                        else {
                            int transferFluid = Math.min(amount, distiller.capacity - (distiller.amount1 + distiller.amount2 + distiller.amount3 + distiller.amount4));
                            distiller.amount4 += transferFluid;
                            stack.set(ModDataComponentTypes.AMOUNT, amount - transferFluid);
                            if (amount - transferFluid == 0)
                            {
                                stack.set(ModDataComponentTypes.STORED_FLUID, FluidVariant.blank());
                            }
                        }
                        distiller.markDirty();
                        world.updateListeners(pos, state, state, Block.NOTIFY_ALL);
                    }
                    else if(FluidVariant.blank() == distiller.storedFluid1 || FluidVariant.blank() == distiller.storedFluid2 || FluidVariant.blank() == distiller.storedFluid3 || FluidVariant.blank() == distiller.storedFluid4)
                    {
                        if (FluidVariant.blank() == distiller.storedFluid1)
                        {
                            int transferFluid = Math.min(amount, distiller.capacity - (distiller.amount1 + distiller.amount2 + distiller.amount3 + distiller.amount4));
                            distiller.amount1 += transferFluid;
                            distiller.storedFluid1 = fluid;
                            stack.set(ModDataComponentTypes.AMOUNT, amount - transferFluid);
                            if (amount - transferFluid == 0)
                            {
                                stack.set(ModDataComponentTypes.STORED_FLUID, FluidVariant.blank());
                            }
                        }
                        else if(FluidVariant.blank() == distiller.storedFluid2)
                        {
                            int transferFluid = Math.min(amount, distiller.capacity - (distiller.amount1 + distiller.amount2 + distiller.amount3 + distiller.amount4));
                            distiller.amount2 += transferFluid;
                            distiller.storedFluid2 = fluid;
                            stack.set(ModDataComponentTypes.AMOUNT, amount - transferFluid);
                            if (amount - transferFluid == 0)
                            {
                                stack.set(ModDataComponentTypes.STORED_FLUID, FluidVariant.blank());
                            }
                        }
                        else if(FluidVariant.blank() == distiller.storedFluid3)
                        {
                            int transferFluid = Math.min(amount, distiller.capacity - (distiller.amount1 + distiller.amount2 + distiller.amount3 + distiller.amount4));
                            distiller.amount3 += transferFluid;
                            distiller.storedFluid3 = fluid;
                            stack.set(ModDataComponentTypes.AMOUNT, amount - transferFluid);
                            if (amount - transferFluid == 0)
                            {
                                stack.set(ModDataComponentTypes.STORED_FLUID, FluidVariant.blank());
                            }
                        }
                        else {
                            int transferFluid = Math.min(amount, distiller.capacity - (distiller.amount1 + distiller.amount2 + distiller.amount3 + distiller.amount4));
                            distiller.amount4 += transferFluid;
                            distiller.storedFluid4 = fluid;
                            stack.set(ModDataComponentTypes.AMOUNT, amount - transferFluid);
                            if (amount - transferFluid == 0)
                            {
                                stack.set(ModDataComponentTypes.STORED_FLUID, FluidVariant.blank());
                            }
                        }
                        distiller.markDirty();
                        world.updateListeners(pos, state, state, Block.NOTIFY_ALL);
                    }
                }
                else if((distiller.getStack(0).isEmpty() || distiller.getStack(1).isEmpty() || distiller.getStack(2).isEmpty() || distiller.getStack(3).isEmpty() || distiller.getStack(4).isEmpty()) && !player.getMainHandStack().isEmpty())
                {
                    if (distiller.getStack(0).isEmpty())
                    {
                        distiller.setStack(0, stack.copyWithCount(1));
                        stack.decrement(1);
                    }
                    else if (distiller.getStack(1).isEmpty())
                    {
                        distiller.setStack(1, stack.copyWithCount(1));
                        stack.decrement(1);
                    }
                    else if (distiller.getStack(2).isEmpty())
                    {
                        distiller.setStack(2, stack.copyWithCount(1));
                        stack.decrement(1);
                    }
                    else if (distiller.getStack(3).isEmpty())
                    {
                        distiller.setStack(3, stack.copyWithCount(1));
                        stack.decrement(1);
                    }
                    else if (distiller.getStack(4).isEmpty())
                    {
                        distiller.setStack(4, stack.copyWithCount(1));
                        stack.decrement(1);
                    }
                    distiller.markDirty();
                    world.updateListeners(pos, state, state, Block.NOTIFY_ALL);
                }
                else if (player.getMainHandStack().isEmpty())
                {
                    player.giveItemStack(distiller.getStack(0));
                    player.giveItemStack(distiller.getStack(1));
                    player.giveItemStack(distiller.getStack(2));
                    player.giveItemStack(distiller.getStack(3));
                    player.giveItemStack(distiller.getStack(4));
                    distiller.clear();
                    distiller.markDirty();
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
