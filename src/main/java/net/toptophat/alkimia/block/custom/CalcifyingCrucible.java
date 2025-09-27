package net.toptophat.alkimia.block.custom;

import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.toptophat.alkimia.block.entity.custom.CalcifyingCrucibleBlockEntity;
import net.toptophat.alkimia.component.ModDataComponentTypes;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
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

public class CalcifyingCrucible extends BlockWithEntity implements BlockEntityProvider {
    public static final MapCodec<CalcifyingCrucible> CODEC = CalcifyingCrucible.createCodec(CalcifyingCrucible::new);
    public CalcifyingCrucible(Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CalcifyingCrucibleBlockEntity(pos, state);
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType options) {
        tooltip.add(Text.translatable("tooltip.alkimia.calcifying_crucible.tooltip"));
        super.appendTooltip(stack, context, tooltip, options);
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    protected ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if(world.getBlockEntity(pos) instanceof CalcifyingCrucibleBlockEntity calcifier) {
            if (stack.get(ModDataComponentTypes.AMOUNT) != null)
            {
                System.out.println("Detected container");
                Integer amount = stack.get(ModDataComponentTypes.AMOUNT);
                Integer capacity = stack.get(ModDataComponentTypes.CAPACITY);
                FluidVariant fluid = stack.get(ModDataComponentTypes.STORED_FLUID);
                System.out.println("Amount " + amount);
                System.out.println("Capacity" + capacity);

                if (amount <= 0 && calcifier.amount > 0)
                {
                    System.out.println("Retrieving fluid to empty container");
                    Integer transferFluid = calcifier.amount > capacity ? capacity : calcifier.amount;
                    stack.set(ModDataComponentTypes.STORED_FLUID, calcifier.storedFluid);
                    stack.set(ModDataComponentTypes.AMOUNT, transferFluid);
                    calcifier.amount -= transferFluid;
                    if (calcifier.amount <= 0)
                    {
                        calcifier.storedFluid = FluidVariant.blank();
                    }
                    calcifier.markDirty();
                    world.updateListeners(pos, state, state, Block.NOTIFY_ALL);
                }
                else if(amount > 0 && fluid == calcifier.storedFluid)
                {
                    System.out.println("Both non-empty");
                    if(calcifier.amount == calcifier.capacity && amount < capacity)
                    {
                        //from jar to item
                        System.out.println("Jar full, container not");
                        Integer transferFluid = calcifier.amount > capacity - amount ? capacity - amount : calcifier.amount;
                        stack.set(ModDataComponentTypes.AMOUNT, amount + transferFluid);
                        stack.set(ModDataComponentTypes.STORED_FLUID, calcifier.storedFluid);
                        calcifier.amount -= transferFluid;
                        if (calcifier.amount <= 0)
                        {
                            calcifier.storedFluid = FluidVariant.blank();
                        }
                        calcifier.markDirty();
                        world.updateListeners(pos, state, state, Block.NOTIFY_ALL);

                    }
                    else if (amount < capacity && calcifier.amount < calcifier.capacity)
                    {
                        //to jar from item
                        System.out.println("Both partially full");
                        Integer transferFluid = amount > calcifier.capacity - calcifier.amount ? calcifier.capacity - calcifier.amount : amount;
                        stack.set(ModDataComponentTypes.AMOUNT, amount - transferFluid);
                        calcifier.amount += transferFluid;
                        calcifier.storedFluid = fluid;
                        if (stack.get(ModDataComponentTypes.AMOUNT) <= 0)
                        {
                            stack.set(ModDataComponentTypes.STORED_FLUID, FluidVariant.blank());
                        }
                        calcifier.markDirty();
                        world.updateListeners(pos, state, state, Block.NOTIFY_ALL);
                    }
                }
            }
        }
        return ItemActionResult.SUCCESS;
    }
}
