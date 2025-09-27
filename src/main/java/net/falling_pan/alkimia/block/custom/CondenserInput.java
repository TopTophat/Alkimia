package net.falling_pan.alkimia.block.custom;

import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.falling_pan.alkimia.block.entity.custom.CondenserInputBlockEntity;
import net.falling_pan.alkimia.component.ModDataComponentTypes;
import net.falling_pan.alkimia.fluid.ModFluids;
import net.falling_pan.alkimia.util.TickableBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CondenserInput extends BlockWithEntity implements BlockEntityProvider {
    public static final MapCodec<CondenserInput> CODEC = CondenserInput.createCodec(CondenserInput::new);
    public static List<CondenserRecipe> condenser_recipe_book;

    public static void InitRecipes() {
        condenser_recipe_book = List.of(
            new CondenserRecipe(List.of(FluidVariant.of(ModFluids.SOUL)), List.of(1), List.of(FluidVariant.of(ModFluids.LIQUID_SOUL)), List.of(1), 1)
        );
    }

    public CondenserInput(Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CondenserInputBlockEntity(pos, state);
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType
            options) {
        tooltip.add(Text.translatable("tooltip.alkimia.condenser_input.tooltip"));
        super.appendTooltip(stack, context, tooltip, options);
    }

    @Override
    protected ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient)
        {
            if (world.getBlockEntity(pos) instanceof CondenserInputBlockEntity condenser)
            {
                if (stack.get(ModDataComponentTypes.AMOUNT) != null)
                {
                    int amount = stack.get(ModDataComponentTypes.AMOUNT);
                    int capacity = stack.get(ModDataComponentTypes.CAPACITY);
                    FluidVariant fluid = stack.get(ModDataComponentTypes.STORED_FLUID);

                    if (fluid == FluidVariant.blank() && (condenser.amount1 > 0 || condenser.amount2 > 0 || condenser.amount3 > 0 || condenser.amount4 > 0))
                    {
                        if (condenser.amount1 > 0)
                        {
                            int transferFluid = Math.min(capacity, condenser.amount1);
                            condenser.amount1 -= transferFluid;
                            stack.set(ModDataComponentTypes.AMOUNT, transferFluid);
                            stack.set(ModDataComponentTypes.STORED_FLUID, condenser.storedFluid1);
                            if (condenser.amount1 <= 0)
                            {
                                condenser.storedFluid1 = FluidVariant.blank();
                            }
                        }
                        else if(condenser.amount2 > 0)
                        {
                            int transferFluid = Math.min(capacity, condenser.amount2);
                            condenser.amount2 -= transferFluid;
                            stack.set(ModDataComponentTypes.AMOUNT, transferFluid);
                            stack.set(ModDataComponentTypes.STORED_FLUID, condenser.storedFluid2);
                            if (condenser.amount2 <= 0)
                            {
                                condenser.storedFluid2 = FluidVariant.blank();
                            }
                        }
                        else if(condenser.amount3 > 0)
                        {
                            int transferFluid = Math.min(capacity, condenser.amount3);
                            condenser.amount3 -= transferFluid;
                            stack.set(ModDataComponentTypes.AMOUNT, transferFluid);
                            stack.set(ModDataComponentTypes.STORED_FLUID, condenser.storedFluid3);
                            if (condenser.amount3 <= 0)
                            {
                                condenser.storedFluid3 = FluidVariant.blank();
                            }
                        }
                        else {
                            int transferFluid = Math.min(capacity, condenser.amount4);
                            condenser.amount4 -= transferFluid;
                            stack.set(ModDataComponentTypes.AMOUNT, transferFluid);
                            stack.set(ModDataComponentTypes.STORED_FLUID, condenser.storedFluid4);
                            if (condenser.amount4 <= 0)
                            {
                                condenser.storedFluid4 = FluidVariant.blank();
                            }
                        }
                        condenser.markDirty();
                        world.updateListeners(pos, state, state, Block.NOTIFY_ALL);
                    }
                    else if((fluid == condenser.storedFluid1 || fluid == condenser.storedFluid2 || fluid == condenser.storedFluid3 || fluid == condenser.storedFluid4) && condenser.amount1 + condenser.amount2 + condenser.amount3 + condenser.amount4 < condenser.capacity)
                    {
                        if (fluid == condenser.storedFluid1)
                        {
                            int transferFluid = Math.min(amount, condenser.capacity - (condenser.amount1 + condenser.amount2 + condenser.amount3 + condenser.amount4));
                            condenser.amount1 += transferFluid;
                            stack.set(ModDataComponentTypes.AMOUNT, amount - transferFluid);
                            if (amount - transferFluid == 0)
                            {
                                stack.set(ModDataComponentTypes.STORED_FLUID, FluidVariant.blank());
                            }
                        }
                        else if(fluid == condenser.storedFluid2)
                        {
                            int transferFluid = Math.min(amount, condenser.capacity - (condenser.amount1 + condenser.amount2 + condenser.amount3 + condenser.amount4));
                            condenser.amount2 += transferFluid;
                            stack.set(ModDataComponentTypes.AMOUNT, amount - transferFluid);
                            if (amount - transferFluid == 0)
                            {
                                stack.set(ModDataComponentTypes.STORED_FLUID, FluidVariant.blank());
                            }
                        }
                        else if(fluid == condenser.storedFluid3)
                        {
                            int transferFluid = Math.min(amount, condenser.capacity - (condenser.amount1 + condenser.amount2 + condenser.amount3 + condenser.amount4));
                            condenser.amount3 += transferFluid;
                            stack.set(ModDataComponentTypes.AMOUNT, amount - transferFluid);
                            if (amount - transferFluid == 0)
                            {
                                stack.set(ModDataComponentTypes.STORED_FLUID, FluidVariant.blank());
                            }
                        }
                        else {
                            int transferFluid = Math.min(amount, condenser.capacity - (condenser.amount1 + condenser.amount2 + condenser.amount3 + condenser.amount4));
                            condenser.amount4 += transferFluid;
                            stack.set(ModDataComponentTypes.AMOUNT, amount - transferFluid);
                            if (amount - transferFluid == 0)
                            {
                                stack.set(ModDataComponentTypes.STORED_FLUID, FluidVariant.blank());
                            }
                        }
                        condenser.markDirty();
                        world.updateListeners(pos, state, state, Block.NOTIFY_ALL);
                    }
                    else if(FluidVariant.blank() == condenser.storedFluid1 || FluidVariant.blank() == condenser.storedFluid2 || FluidVariant.blank() == condenser.storedFluid3 || FluidVariant.blank() == condenser.storedFluid4)
                    {
                        if (FluidVariant.blank() == condenser.storedFluid1)
                        {
                            int transferFluid = Math.min(amount, condenser.capacity - (condenser.amount1 + condenser.amount2 + condenser.amount3 + condenser.amount4));
                            condenser.amount1 += transferFluid;
                            condenser.storedFluid1 = fluid;
                            stack.set(ModDataComponentTypes.AMOUNT, amount - transferFluid);
                            if (amount - transferFluid == 0)
                            {
                                stack.set(ModDataComponentTypes.STORED_FLUID, FluidVariant.blank());
                            }
                        }
                        else if(FluidVariant.blank() == condenser.storedFluid2)
                        {
                            int transferFluid = Math.min(amount, condenser.capacity - (condenser.amount1 + condenser.amount2 + condenser.amount3 + condenser.amount4));
                            condenser.amount2 += transferFluid;
                            condenser.storedFluid2 = fluid;
                            stack.set(ModDataComponentTypes.AMOUNT, amount - transferFluid);
                            if (amount - transferFluid == 0)
                            {
                                stack.set(ModDataComponentTypes.STORED_FLUID, FluidVariant.blank());
                            }
                        }
                        else if(FluidVariant.blank() == condenser.storedFluid3)
                        {
                            int transferFluid = Math.min(amount, condenser.capacity - (condenser.amount1 + condenser.amount2 + condenser.amount3 + condenser.amount4));
                            condenser.amount3 += transferFluid;
                            condenser.storedFluid3 = fluid;
                            stack.set(ModDataComponentTypes.AMOUNT, amount - transferFluid);
                            if (amount - transferFluid == 0)
                            {
                                stack.set(ModDataComponentTypes.STORED_FLUID, FluidVariant.blank());
                            }
                        }
                        else {
                            int transferFluid = Math.min(amount, condenser.capacity - (condenser.amount1 + condenser.amount2 + condenser.amount3 + condenser.amount4));
                            condenser.amount4 += transferFluid;
                            condenser.storedFluid4 = fluid;
                            stack.set(ModDataComponentTypes.AMOUNT, amount - transferFluid);
                            if (amount - transferFluid == 0)
                            {
                                stack.set(ModDataComponentTypes.STORED_FLUID, FluidVariant.blank());
                            }
                        }
                        condenser.markDirty();
                        world.updateListeners(pos, state, state, Block.NOTIFY_ALL);
                    }
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
