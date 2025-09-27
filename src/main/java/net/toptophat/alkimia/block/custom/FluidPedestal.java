package net.toptophat.alkimia.block.custom;

import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.toptophat.alkimia.block.ModBlocks;
import net.toptophat.alkimia.block.entity.custom.FluidPedestalBlockEntity;
import net.toptophat.alkimia.component.ModDataComponentTypes;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class FluidPedestal extends BlockWithEntity implements BlockEntityProvider {
    public static final BooleanProperty IS_FAST = BooleanProperty.of("is_fast");
    public static final MapCodec<FluidPedestal> CODEC = FluidPedestal.createCodec(FluidPedestal::new);

    public FluidPedestal(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(IS_FAST, false));
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new FluidPedestalBlockEntity(pos, state);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(IS_FAST);
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    protected ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.getBlockEntity(pos) instanceof FluidPedestalBlockEntity pedestal) {
            if (stack.get(ModDataComponentTypes.AMOUNT) != null)
            {
                System.out.println("Detected container");
                Integer amount = stack.get(ModDataComponentTypes.AMOUNT);
                Integer capacity = stack.get(ModDataComponentTypes.CAPACITY);
                FluidVariant fluid = stack.get(ModDataComponentTypes.STORED_FLUID);
                System.out.println("Amount " + amount);
                System.out.println("Capacity" + capacity);

                if (amount <= 0 && pedestal.amount > 0)
                {
                    System.out.println("Container empty, jar not");
                    Integer transferFluid = pedestal.amount > capacity ? capacity : pedestal.amount;
                    stack.set(ModDataComponentTypes.STORED_FLUID, pedestal.storedFluid);
                    stack.set(ModDataComponentTypes.AMOUNT, transferFluid);
                    pedestal.amount -= transferFluid;
                    if (pedestal.amount <= 0)
                    {
                        pedestal.storedFluid = FluidVariant.blank();
                    }
                    pedestal.markDirty();
                    world.updateListeners(pos, state, state, Block.NOTIFY_ALL);
                }
                else if (amount > 0 && pedestal.amount <= 0)
                {
                    System.out.println("Jar empty, container not");
                    Integer transferFluid = pedestal.capacity < amount ? pedestal.capacity : amount;
                    stack.set(ModDataComponentTypes.AMOUNT, amount - transferFluid);
                    pedestal.amount += transferFluid;
                    pedestal.storedFluid = fluid;
                    if (stack.get(ModDataComponentTypes.AMOUNT) <= 0)
                    {
                        stack.set(ModDataComponentTypes.STORED_FLUID, FluidVariant.blank());
                    }
                    pedestal.markDirty();
                    world.updateListeners(pos, state, state, Block.NOTIFY_ALL);
                }
                else if(amount > 0 && fluid == pedestal.storedFluid)
                {
                    System.out.println("Both non-empty");
                    if(pedestal.amount == pedestal.capacity && amount < capacity)
                    {
                        //from jar to item
                        System.out.println("Jar full, container not");
                        Integer transferFluid = pedestal.amount > capacity - amount ? capacity - amount : pedestal.amount;
                        stack.set(ModDataComponentTypes.AMOUNT, amount + transferFluid);
                        stack.set(ModDataComponentTypes.STORED_FLUID, pedestal.storedFluid);
                        pedestal.amount -= transferFluid;
                        if (pedestal.amount <= 0)
                        {
                            pedestal.storedFluid = FluidVariant.blank();
                        }
                        pedestal.markDirty();
                        world.updateListeners(pos, state, state, Block.NOTIFY_ALL);

                    }
                    else if (Objects.equals(capacity, amount) && pedestal.amount < pedestal.capacity)
                    {
                        //from item to jar
                        System.out.println("Container full, jar not");
                        Integer transferFluid = amount > pedestal.capacity - pedestal.amount ? pedestal.capacity - pedestal.amount : amount;
                        stack.set(ModDataComponentTypes.AMOUNT, amount - transferFluid);
                        pedestal.amount += transferFluid;
                        pedestal.storedFluid = fluid;
                        if (stack.get(ModDataComponentTypes.AMOUNT) <= 0)
                        {
                            stack.set(ModDataComponentTypes.STORED_FLUID, FluidVariant.blank());
                        }
                        pedestal.markDirty();
                        world.updateListeners(pos, state, state, Block.NOTIFY_ALL);
                    }
                    else if (amount < capacity && pedestal.amount < pedestal.capacity)
                    {
                        //to jar from item
                        System.out.println("Both partially full");
                        Integer transferFluid = amount > pedestal.capacity - pedestal.amount ? pedestal.capacity - pedestal.amount : amount;
                        stack.set(ModDataComponentTypes.AMOUNT, amount - transferFluid);
                        pedestal.amount += transferFluid;
                        pedestal.storedFluid = fluid;
                        if (stack.get(ModDataComponentTypes.AMOUNT) <= 0)
                        {
                            stack.set(ModDataComponentTypes.STORED_FLUID, FluidVariant.blank());
                        }
                        pedestal.markDirty();
                        world.updateListeners(pos, state, state, Block.NOTIFY_ALL);
                    }
                }
            } else if (stack.getItem() == Items.STICK) {
                world.setBlockState(pos, ModBlocks.FLUID_PEDESTAL_BASE.getDefaultState().with(IS_FAST, !world.getBlockState(pos).get(IS_FAST)));
            }
        }

        return ItemActionResult.SUCCESS;

    }
}
