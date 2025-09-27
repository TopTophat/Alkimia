package net.toptophat.alkimia.block.custom;

import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.toptophat.alkimia.block.entity.custom.JarBlockEntity;
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
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class Jar extends BlockWithEntity implements BlockEntityProvider {
    public static final VoxelShape SHAPE = Block.createCuboidShape(3, 0, 3, 13, 14, 13);
    public static final MapCodec<Jar> CODEC = Jar.createCodec(Jar::new);

    public Jar(Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType options) {
        tooltip.add(Text.translatable("tooltip.alkimia.jar.tooltip"));
        super.appendTooltip(stack, context, tooltip, options);
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new JarBlockEntity(pos, state);
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    protected ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if(world.getBlockEntity(pos) instanceof JarBlockEntity jarBlockEntity) {
            if (FluidMaps.SIMPLE_ITEM_TO_FLUID.containsKey(stack.getItem()))
            {
                if (FluidMaps.SIMPLE_ITEM_TO_FLUID.get(stack.getItem()) == jarBlockEntity.storedFluid || jarBlockEntity.storedFluid.isBlank())
                {
                    if (jarBlockEntity.amount + FluidMaps.SIMPLE_ITEM_TO_AMOUNT.get(stack.getItem()) <= jarBlockEntity.capacity)
                    {
                        jarBlockEntity.storedFluid = FluidMaps.SIMPLE_ITEM_TO_FLUID.get(stack.getItem());
                        jarBlockEntity.amount = FluidMaps.SIMPLE_ITEM_TO_AMOUNT.get(stack.getItem()) + jarBlockEntity.amount;
                        if (FluidMaps.SIMPLE_ITEM_TO_REMAINDER.containsKey(stack.getItem()))
                        {
                            player.giveItemStack(new ItemStack(FluidMaps.SIMPLE_ITEM_TO_REMAINDER.get(stack.getItem()), 1));
                        }
                        stack.decrement(1);
                        jarBlockEntity.markDirty();
                        world.updateListeners(pos, state, state, Block.NOTIFY_ALL);
                    }
                }
            }
            else if (stack.get(ModDataComponentTypes.AMOUNT) != null)
            {
                System.out.println("Detected container");
                Integer amount = stack.get(ModDataComponentTypes.AMOUNT);
                Integer capacity = stack.get(ModDataComponentTypes.CAPACITY);
                FluidVariant fluid = stack.get(ModDataComponentTypes.STORED_FLUID);
                System.out.println("Amount " + amount);
                System.out.println("Capacity" + capacity);

                if (amount <= 0 && jarBlockEntity.amount > 0)
                {
                    System.out.println("Container empty, jar not");
                    Integer transferFluid = jarBlockEntity.amount > capacity ? capacity : jarBlockEntity.amount;
                    stack.set(ModDataComponentTypes.STORED_FLUID, jarBlockEntity.storedFluid);
                    stack.set(ModDataComponentTypes.AMOUNT, transferFluid);
                    jarBlockEntity.amount -= transferFluid;
                    if (jarBlockEntity.amount <= 0)
                    {
                        jarBlockEntity.storedFluid = FluidVariant.blank();
                    }
                    jarBlockEntity.markDirty();
                    world.updateListeners(pos, state, state, Block.NOTIFY_ALL);
                }
                else if (amount > 0 && jarBlockEntity.amount <= 0)
                {
                    System.out.println("Jar empty, container not");
                    Integer transferFluid = jarBlockEntity.capacity < amount ? jarBlockEntity.capacity : amount;
                    stack.set(ModDataComponentTypes.AMOUNT, amount - transferFluid);
                    jarBlockEntity.amount += transferFluid;
                    jarBlockEntity.storedFluid = fluid;
                    if (stack.get(ModDataComponentTypes.AMOUNT) <= 0)
                    {
                        stack.set(ModDataComponentTypes.STORED_FLUID, FluidVariant.blank());
                    }
                    jarBlockEntity.markDirty();
                    world.updateListeners(pos, state, state, Block.NOTIFY_ALL);
                }
                else if(amount > 0 && fluid == jarBlockEntity.storedFluid)
                {
                    System.out.println("Both non-empty");
                    if(jarBlockEntity.amount == jarBlockEntity.capacity && amount < capacity)
                    {
                        //from jar to item
                        System.out.println("Jar full, container not");
                        Integer transferFluid = jarBlockEntity.amount > capacity - amount ? capacity - amount : jarBlockEntity.amount;
                        stack.set(ModDataComponentTypes.AMOUNT, amount + transferFluid);
                        stack.set(ModDataComponentTypes.STORED_FLUID, jarBlockEntity.storedFluid);
                        jarBlockEntity.amount -= transferFluid;
                        if (jarBlockEntity.amount <= 0)
                        {
                            jarBlockEntity.storedFluid = FluidVariant.blank();
                        }
                        jarBlockEntity.markDirty();
                        world.updateListeners(pos, state, state, Block.NOTIFY_ALL);

                    }
                    else if (Objects.equals(capacity, amount) && jarBlockEntity.amount < jarBlockEntity.capacity)
                    {
                        //from item to jar
                        System.out.println("Container full, jar not");
                        Integer transferFluid = amount > jarBlockEntity.capacity - jarBlockEntity.amount ? jarBlockEntity.capacity - jarBlockEntity.amount : amount;
                        stack.set(ModDataComponentTypes.AMOUNT, amount - transferFluid);
                        jarBlockEntity.amount += transferFluid;
                        jarBlockEntity.storedFluid = fluid;
                        if (stack.get(ModDataComponentTypes.AMOUNT) <= 0)
                        {
                            stack.set(ModDataComponentTypes.STORED_FLUID, FluidVariant.blank());
                        }
                        jarBlockEntity.markDirty();
                        world.updateListeners(pos, state, state, Block.NOTIFY_ALL);
                    }
                    else if (amount < capacity && jarBlockEntity.amount < jarBlockEntity.capacity)
                    {
                        //to jar from item
                        System.out.println("Both partially full");
                        Integer transferFluid = amount > jarBlockEntity.capacity - jarBlockEntity.amount ? jarBlockEntity.capacity - jarBlockEntity.amount : amount;
                        stack.set(ModDataComponentTypes.AMOUNT, amount - transferFluid);
                        jarBlockEntity.amount += transferFluid;
                        jarBlockEntity.storedFluid = fluid;
                        if (stack.get(ModDataComponentTypes.AMOUNT) <= 0)
                        {
                            stack.set(ModDataComponentTypes.STORED_FLUID, FluidVariant.blank());
                        }
                        jarBlockEntity.markDirty();
                        world.updateListeners(pos, state, state, Block.NOTIFY_ALL);
                    }
                }
            }
        }
        return ItemActionResult.SUCCESS;
    }
}
