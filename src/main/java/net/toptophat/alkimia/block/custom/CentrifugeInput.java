package net.toptophat.alkimia.block.custom;

import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.toptophat.alkimia.block.entity.custom.CentrifugeInputBlockEntity;
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
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CentrifugeInput extends BlockWithEntity implements BlockEntityProvider {
    public static final MapCodec<CentrifugeInput> CODEC = CentrifugeInput.createCodec(CentrifugeInput::new);
    public static List<CentrifugeRecipe> centrifuge_recipe_book;

    public static void InitRecipes() {
        centrifuge_recipe_book = List.of(
            new CentrifugeRecipe(List.of(FluidVariant.blank(), FluidVariant.blank(), FluidVariant.blank(), FluidVariant.blank()), List.of(0, 0, 0, 0), List.of(FluidVariant.blank(), FluidVariant.blank(), FluidVariant.blank(), FluidVariant.blank()), List.of(0, 0, 0, 0), List.of(Items.ORANGE_DYE), List.of(Items.RED_DYE, Items.YELLOW_DYE, Items.AIR, Items.AIR), 100),
            new CentrifugeRecipe(List.of(FluidVariant.blank(), FluidVariant.blank(), FluidVariant.blank(), FluidVariant.blank()), List.of(0, 0, 0, 0), List.of(FluidVariant.of(ModFluids.SOUL), FluidVariant.blank(), FluidVariant.blank(), FluidVariant.blank()), List.of(100, 0, 0, 0), List.of(Items.SOUL_SAND,Items.SOUL_SAND,Items.SOUL_SAND,Items.SOUL_SAND,Items.SOUL_SAND), List.of(Items.SAND, Items.SAND, Items.SAND, Items.SAND), 100),
            new CentrifugeRecipe(List.of(FluidVariant.of(Fluids.LAVA), FluidVariant.of(ModFluids.DIAMOND_DUST), FluidVariant.blank(), FluidVariant.blank()), List.of(10, 10, 0, 0), List.of(FluidVariant.of(ModFluids.SOUL), FluidVariant.blank(), FluidVariant.blank(), FluidVariant.blank()), List.of(1, 0, 0, 0), List.of(), List.of(Items.AIR, Items.AIR, Items.AIR, Items.AIR), 10),
            new CentrifugeRecipe(List.of(FluidVariant.blank(), FluidVariant.blank(), FluidVariant.blank(), FluidVariant.blank()), List.of(0, 0, 0, 0), List.of(FluidVariant.blank(), FluidVariant.blank(), FluidVariant.blank(), FluidVariant.blank()), List.of(0, 0, 0, 0), List.of(Items.SAND, Items.SAND ,Items.SAND, Items.SAND, Items.SAND), List.of(Items.FLINT, Items.FLINT, Items.FLINT, Items.IRON_NUGGET), 100)
        );
    }

    public CentrifugeInput(Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CentrifugeInputBlockEntity(pos, state);
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    protected void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof CentrifugeInputBlockEntity) {
                ItemScatterer.spawn(world, pos, ((CentrifugeInputBlockEntity) blockEntity));
                world.updateComparators(pos, this);
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType
            options) {
        tooltip.add(Text.translatable("tooltip.alkimia.centrifuge_input.tooltip"));
        super.appendTooltip(stack, context, tooltip, options);
    }

    @Override
    protected ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient)
        {
            if (world.getBlockEntity(pos) instanceof CentrifugeInputBlockEntity centrifuge)
            {
                if (stack.get(ModDataComponentTypes.AMOUNT) != null)
                {
                    int amount = stack.get(ModDataComponentTypes.AMOUNT);
                    int capacity = stack.get(ModDataComponentTypes.CAPACITY);
                    FluidVariant fluid = stack.get(ModDataComponentTypes.STORED_FLUID);

                    if (fluid == FluidVariant.blank() && (centrifuge.amount1 > 0 || centrifuge.amount2 > 0 || centrifuge.amount3 > 0 || centrifuge.amount4 > 0))
                    {
                        if (centrifuge.amount1 > 0)
                        {
                            int transferFluid = Math.min(capacity, centrifuge.amount1);
                            centrifuge.amount1 -= transferFluid;
                            stack.set(ModDataComponentTypes.AMOUNT, transferFluid);
                            stack.set(ModDataComponentTypes.STORED_FLUID, centrifuge.storedFluid1);
                            if (centrifuge.amount1 <= 0)
                            {
                                centrifuge.storedFluid1 = FluidVariant.blank();
                            }
                        }
                        else if(centrifuge.amount2 > 0)
                        {
                            int transferFluid = Math.min(capacity, centrifuge.amount2);
                            centrifuge.amount2 -= transferFluid;
                            stack.set(ModDataComponentTypes.AMOUNT, transferFluid);
                            stack.set(ModDataComponentTypes.STORED_FLUID, centrifuge.storedFluid2);
                            if (centrifuge.amount2 <= 0)
                            {
                                centrifuge.storedFluid2 = FluidVariant.blank();
                            }
                        }
                        else if(centrifuge.amount3 > 0)
                        {
                            int transferFluid = Math.min(capacity, centrifuge.amount3);
                            centrifuge.amount3 -= transferFluid;
                            stack.set(ModDataComponentTypes.AMOUNT, transferFluid);
                            stack.set(ModDataComponentTypes.STORED_FLUID, centrifuge.storedFluid3);
                            if (centrifuge.amount3 <= 0)
                            {
                                centrifuge.storedFluid3 = FluidVariant.blank();
                            }
                        }
                        else {
                            int transferFluid = Math.min(capacity, centrifuge.amount4);
                            centrifuge.amount4 -= transferFluid;
                            stack.set(ModDataComponentTypes.AMOUNT, transferFluid);
                            stack.set(ModDataComponentTypes.STORED_FLUID, centrifuge.storedFluid4);
                            if (centrifuge.amount4 <= 0)
                            {
                                centrifuge.storedFluid4 = FluidVariant.blank();
                            }
                        }
                        centrifuge.markDirty();
                        world.updateListeners(pos, state, state, Block.NOTIFY_ALL);
                    }
                    else if((fluid == centrifuge.storedFluid1 || fluid == centrifuge.storedFluid2 || fluid == centrifuge.storedFluid3 || fluid == centrifuge.storedFluid4) && centrifuge.amount1 + centrifuge.amount2 + centrifuge.amount3 + centrifuge.amount4 < centrifuge.capacity)
                    {
                        if (fluid == centrifuge.storedFluid1)
                        {
                            int transferFluid = Math.min(amount, centrifuge.capacity - (centrifuge.amount1 + centrifuge.amount2 + centrifuge.amount3 + centrifuge.amount4));
                            centrifuge.amount1 += transferFluid;
                            stack.set(ModDataComponentTypes.AMOUNT, amount - transferFluid);
                            if (amount - transferFluid == 0)
                            {
                                stack.set(ModDataComponentTypes.STORED_FLUID, FluidVariant.blank());
                            }
                        }
                        else if(fluid == centrifuge.storedFluid2)
                        {
                            int transferFluid = Math.min(amount, centrifuge.capacity - (centrifuge.amount1 + centrifuge.amount2 + centrifuge.amount3 + centrifuge.amount4));
                            centrifuge.amount2 += transferFluid;
                            stack.set(ModDataComponentTypes.AMOUNT, amount - transferFluid);
                            if (amount - transferFluid == 0)
                            {
                                stack.set(ModDataComponentTypes.STORED_FLUID, FluidVariant.blank());
                            }
                        }
                        else if(fluid == centrifuge.storedFluid3)
                        {
                            int transferFluid = Math.min(amount, centrifuge.capacity - (centrifuge.amount1 + centrifuge.amount2 + centrifuge.amount3 + centrifuge.amount4));
                            centrifuge.amount3 += transferFluid;
                            stack.set(ModDataComponentTypes.AMOUNT, amount - transferFluid);
                            if (amount - transferFluid == 0)
                            {
                                stack.set(ModDataComponentTypes.STORED_FLUID, FluidVariant.blank());
                            }
                        }
                        else {
                            int transferFluid = Math.min(amount, centrifuge.capacity - (centrifuge.amount1 + centrifuge.amount2 + centrifuge.amount3 + centrifuge.amount4));
                            centrifuge.amount4 += transferFluid;
                            stack.set(ModDataComponentTypes.AMOUNT, amount - transferFluid);
                            if (amount - transferFluid == 0)
                            {
                                stack.set(ModDataComponentTypes.STORED_FLUID, FluidVariant.blank());
                            }
                        }
                        centrifuge.markDirty();
                        world.updateListeners(pos, state, state, Block.NOTIFY_ALL);
                    }
                    else if(FluidVariant.blank() == centrifuge.storedFluid1 || FluidVariant.blank() == centrifuge.storedFluid2 || FluidVariant.blank() == centrifuge.storedFluid3 || FluidVariant.blank() == centrifuge.storedFluid4)
                    {
                        if (FluidVariant.blank() == centrifuge.storedFluid1)
                        {
                            int transferFluid = Math.min(amount, centrifuge.capacity - (centrifuge.amount1 + centrifuge.amount2 + centrifuge.amount3 + centrifuge.amount4));
                            centrifuge.amount1 += transferFluid;
                            centrifuge.storedFluid1 = fluid;
                            stack.set(ModDataComponentTypes.AMOUNT, amount - transferFluid);
                            if (amount - transferFluid == 0)
                            {
                                stack.set(ModDataComponentTypes.STORED_FLUID, FluidVariant.blank());
                            }
                        }
                        else if(FluidVariant.blank() == centrifuge.storedFluid2)
                        {
                            int transferFluid = Math.min(amount, centrifuge.capacity - (centrifuge.amount1 + centrifuge.amount2 + centrifuge.amount3 + centrifuge.amount4));
                            centrifuge.amount2 += transferFluid;
                            centrifuge.storedFluid2 = fluid;
                            stack.set(ModDataComponentTypes.AMOUNT, amount - transferFluid);
                            if (amount - transferFluid == 0)
                            {
                                stack.set(ModDataComponentTypes.STORED_FLUID, FluidVariant.blank());
                            }
                        }
                        else if(FluidVariant.blank() == centrifuge.storedFluid3)
                        {
                            int transferFluid = Math.min(amount, centrifuge.capacity - (centrifuge.amount1 + centrifuge.amount2 + centrifuge.amount3 + centrifuge.amount4));
                            centrifuge.amount3 += transferFluid;
                            centrifuge.storedFluid3 = fluid;
                            stack.set(ModDataComponentTypes.AMOUNT, amount - transferFluid);
                            if (amount - transferFluid == 0)
                            {
                                stack.set(ModDataComponentTypes.STORED_FLUID, FluidVariant.blank());
                            }
                        }
                        else {
                            int transferFluid = Math.min(amount, centrifuge.capacity - (centrifuge.amount1 + centrifuge.amount2 + centrifuge.amount3 + centrifuge.amount4));
                            centrifuge.amount4 += transferFluid;
                            centrifuge.storedFluid4 = fluid;
                            stack.set(ModDataComponentTypes.AMOUNT, amount - transferFluid);
                            if (amount - transferFluid == 0)
                            {
                                stack.set(ModDataComponentTypes.STORED_FLUID, FluidVariant.blank());
                            }
                        }
                        centrifuge.markDirty();
                        world.updateListeners(pos, state, state, Block.NOTIFY_ALL);
                    }
                }
                else if((centrifuge.getStack(0).isEmpty() || centrifuge.getStack(1).isEmpty() || centrifuge.getStack(2).isEmpty() || centrifuge.getStack(3).isEmpty() || centrifuge.getStack(4).isEmpty()) && !player.getMainHandStack().isEmpty())
                {
                    if (centrifuge.getStack(0).isEmpty())
                    {
                        centrifuge.setStack(0, stack.copyWithCount(1));
                        stack.decrement(1);
                    }
                    else if (centrifuge.getStack(1).isEmpty())
                    {
                        centrifuge.setStack(1, stack.copyWithCount(1));
                        stack.decrement(1);
                    }
                    else if (centrifuge.getStack(2).isEmpty())
                    {
                        centrifuge.setStack(2, stack.copyWithCount(1));
                        stack.decrement(1);
                    }
                    else if (centrifuge.getStack(3).isEmpty())
                    {
                        centrifuge.setStack(3, stack.copyWithCount(1));
                        stack.decrement(1);
                    }
                    else if (centrifuge.getStack(4).isEmpty())
                    {
                        centrifuge.setStack(4, stack.copyWithCount(1));
                        stack.decrement(1);
                    }
                    centrifuge.markDirty();
                    world.updateListeners(pos, state, state, Block.NOTIFY_ALL);
                }
                else if (player.getMainHandStack().isEmpty())
                {
                    player.giveItemStack(centrifuge.getStack(0));
                    player.giveItemStack(centrifuge.getStack(1));
                    player.giveItemStack(centrifuge.getStack(2));
                    player.giveItemStack(centrifuge.getStack(3));
                    player.giveItemStack(centrifuge.getStack(4));
                    centrifuge.clear();
                    centrifuge.markDirty();
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
