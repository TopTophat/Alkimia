package net.toptophat.alkimia.block.custom;

import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.toptophat.alkimia.block.entity.custom.InfuserInputBlockEntity;
import net.toptophat.alkimia.component.ModDataComponentTypes;
import net.toptophat.alkimia.fluid.ModFluids;
import net.toptophat.alkimia.util.TickableBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class InfuserInput extends BlockWithEntity implements BlockEntityProvider {
    public static final MapCodec<InfuserInput> CODEC = InfuserInput.createCodec(InfuserInput::new);
    public static List<InfuserRecipe> infuser_recipe_book;

    public static void InitRecipes() {
        infuser_recipe_book = List.of(
            new InfuserRecipe(List.of(FluidVariant.of(ModFluids.DIAMOND_DUST)), List.of(20), Items.QUARTZ, Items.DIAMOND, 100)
        );
    }

    public InfuserInput(Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new InfuserInputBlockEntity(pos, state);
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType
            options) {
        tooltip.add(Text.translatable("tooltip.alkimia.infuser_input.tooltip"));
        super.appendTooltip(stack, context, tooltip, options);
    }

    @Override
    protected ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient)
        {
            if (world.getBlockEntity(pos) instanceof InfuserInputBlockEntity infuser)
            {
                if (stack.get(ModDataComponentTypes.AMOUNT) != null)
                {
                    int amount = stack.get(ModDataComponentTypes.AMOUNT);
                    int capacity = stack.get(ModDataComponentTypes.CAPACITY);
                    FluidVariant fluid = stack.get(ModDataComponentTypes.STORED_FLUID);

                    if (fluid == FluidVariant.blank() && (infuser.amount1 > 0 || infuser.amount2 > 0 || infuser.amount3 > 0 || infuser.amount4 > 0))
                    {
                        if (infuser.amount1 > 0)
                        {
                            int transferFluid = Math.min(capacity, infuser.amount1);
                            infuser.amount1 -= transferFluid;
                            stack.set(ModDataComponentTypes.AMOUNT, transferFluid);
                            stack.set(ModDataComponentTypes.STORED_FLUID, infuser.storedFluid1);
                            if (infuser.amount1 <= 0)
                            {
                                infuser.storedFluid1 = FluidVariant.blank();
                            }
                        }
                        else if(infuser.amount2 > 0)
                        {
                            int transferFluid = Math.min(capacity, infuser.amount2);
                            infuser.amount2 -= transferFluid;
                            stack.set(ModDataComponentTypes.AMOUNT, transferFluid);
                            stack.set(ModDataComponentTypes.STORED_FLUID, infuser.storedFluid2);
                            if (infuser.amount2 <= 0)
                            {
                                infuser.storedFluid2 = FluidVariant.blank();
                            }
                        }
                        else if(infuser.amount3 > 0)
                        {
                            int transferFluid = Math.min(capacity, infuser.amount3);
                            infuser.amount3 -= transferFluid;
                            stack.set(ModDataComponentTypes.AMOUNT, transferFluid);
                            stack.set(ModDataComponentTypes.STORED_FLUID, infuser.storedFluid3);
                            if (infuser.amount3 <= 0)
                            {
                                infuser.storedFluid3 = FluidVariant.blank();
                            }
                        }
                        else {
                            int transferFluid = Math.min(capacity, infuser.amount4);
                            infuser.amount4 -= transferFluid;
                            stack.set(ModDataComponentTypes.AMOUNT, transferFluid);
                            stack.set(ModDataComponentTypes.STORED_FLUID, infuser.storedFluid4);
                            if (infuser.amount4 <= 0)
                            {
                                infuser.storedFluid4 = FluidVariant.blank();
                            }
                        }
                        infuser.markDirty();
                        world.updateListeners(pos, state, state, Block.NOTIFY_ALL);
                    }
                    else if((fluid == infuser.storedFluid1 || fluid == infuser.storedFluid2 || fluid == infuser.storedFluid3 || fluid == infuser.storedFluid4) && infuser.amount1 + infuser.amount2 + infuser.amount3 + infuser.amount4 < infuser.capacity)
                    {
                        if (fluid == infuser.storedFluid1)
                        {
                            int transferFluid = Math.min(amount, infuser.capacity - (infuser.amount1 + infuser.amount2 + infuser.amount3 + infuser.amount4));
                            infuser.amount1 += transferFluid;
                            stack.set(ModDataComponentTypes.AMOUNT, amount - transferFluid);
                            if (amount - transferFluid == 0)
                            {
                                stack.set(ModDataComponentTypes.STORED_FLUID, FluidVariant.blank());
                            }
                        }
                        else if(fluid == infuser.storedFluid2)
                        {
                            int transferFluid = Math.min(amount, infuser.capacity - (infuser.amount1 + infuser.amount2 + infuser.amount3 + infuser.amount4));
                            infuser.amount2 += transferFluid;
                            stack.set(ModDataComponentTypes.AMOUNT, amount - transferFluid);
                            if (amount - transferFluid == 0)
                            {
                                stack.set(ModDataComponentTypes.STORED_FLUID, FluidVariant.blank());
                            }
                        }
                        else if(fluid == infuser.storedFluid3)
                        {
                            int transferFluid = Math.min(amount, infuser.capacity - (infuser.amount1 + infuser.amount2 + infuser.amount3 + infuser.amount4));
                            infuser.amount3 += transferFluid;
                            stack.set(ModDataComponentTypes.AMOUNT, amount - transferFluid);
                            if (amount - transferFluid == 0)
                            {
                                stack.set(ModDataComponentTypes.STORED_FLUID, FluidVariant.blank());
                            }
                        }
                        else {
                            int transferFluid = Math.min(amount, infuser.capacity - (infuser.amount1 + infuser.amount2 + infuser.amount3 + infuser.amount4));
                            infuser.amount4 += transferFluid;
                            stack.set(ModDataComponentTypes.AMOUNT, amount - transferFluid);
                            if (amount - transferFluid == 0)
                            {
                                stack.set(ModDataComponentTypes.STORED_FLUID, FluidVariant.blank());
                            }
                        }
                        infuser.markDirty();
                        world.updateListeners(pos, state, state, Block.NOTIFY_ALL);
                    }
                    else if(FluidVariant.blank() == infuser.storedFluid1 || FluidVariant.blank() == infuser.storedFluid2 || FluidVariant.blank() == infuser.storedFluid3 || FluidVariant.blank() == infuser.storedFluid4)
                    {
                        if (FluidVariant.blank() == infuser.storedFluid1)
                        {
                            int transferFluid = Math.min(amount, infuser.capacity - (infuser.amount1 + infuser.amount2 + infuser.amount3 + infuser.amount4));
                            infuser.amount1 += transferFluid;
                            infuser.storedFluid1 = fluid;
                            stack.set(ModDataComponentTypes.AMOUNT, amount - transferFluid);
                            if (amount - transferFluid == 0)
                            {
                                stack.set(ModDataComponentTypes.STORED_FLUID, FluidVariant.blank());
                            }
                        }
                        else if(FluidVariant.blank() == infuser.storedFluid2)
                        {
                            int transferFluid = Math.min(amount, infuser.capacity - (infuser.amount1 + infuser.amount2 + infuser.amount3 + infuser.amount4));
                            infuser.amount2 += transferFluid;
                            infuser.storedFluid2 = fluid;
                            stack.set(ModDataComponentTypes.AMOUNT, amount - transferFluid);
                            if (amount - transferFluid == 0)
                            {
                                stack.set(ModDataComponentTypes.STORED_FLUID, FluidVariant.blank());
                            }
                        }
                        else if(FluidVariant.blank() == infuser.storedFluid3)
                        {
                            int transferFluid = Math.min(amount, infuser.capacity - (infuser.amount1 + infuser.amount2 + infuser.amount3 + infuser.amount4));
                            infuser.amount3 += transferFluid;
                            infuser.storedFluid3 = fluid;
                            stack.set(ModDataComponentTypes.AMOUNT, amount - transferFluid);
                            if (amount - transferFluid == 0)
                            {
                                stack.set(ModDataComponentTypes.STORED_FLUID, FluidVariant.blank());
                            }
                        }
                        else {
                            int transferFluid = Math.min(amount, infuser.capacity - (infuser.amount1 + infuser.amount2 + infuser.amount3 + infuser.amount4));
                            infuser.amount4 += transferFluid;
                            infuser.storedFluid4 = fluid;
                            stack.set(ModDataComponentTypes.AMOUNT, amount - transferFluid);
                            if (amount - transferFluid == 0)
                            {
                                stack.set(ModDataComponentTypes.STORED_FLUID, FluidVariant.blank());
                            }
                        }
                        infuser.markDirty();
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
