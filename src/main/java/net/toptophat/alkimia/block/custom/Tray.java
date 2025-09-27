package net.toptophat.alkimia.block.custom;

import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.toptophat.alkimia.block.entity.custom.TrayBlockEntity;
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
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class Tray extends BlockWithEntity implements BlockEntityProvider {
    public static final VoxelShape SHAPE = Block.createCuboidShape(0, 0, 0, 16, 2, 16);
    public static final MapCodec<Tray> CODEC = Tray.createCodec(Tray::new);
    public static List<CalcifyingRecipe> calcifying_recipe_book;

    public static void InitRecipes() {
        calcifying_recipe_book = List.of(
                new CalcifyingRecipe(Items.SOUL_SAND, Items.SOUL_SAND, Items.SOUL_SAND, Items.SAND, Items.SAND, Items.SAND, FluidVariant.blank(), FluidVariant.of(ModFluids.SOUL), 0, 300, 200),
                new CalcifyingRecipe(Items.CHARCOAL, Items.CHARCOAL, Items.CHARCOAL, Items.AIR, Items.AIR, Items.AIR, FluidVariant.of(Fluids.LAVA), FluidVariant.of(ModFluids.DIAMOND_DUST), 100, 500, 600)
        );
    }

    public Tray(Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType options) {
        tooltip.add(Text.translatable("tooltip.alkimia.tray.tooltip"));
        super.appendTooltip(stack, context, tooltip, options);
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new TrayBlockEntity(pos, state);
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    protected ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        System.out.println("Clicked with an item");
        if (world.getBlockEntity(pos) instanceof TrayBlockEntity tray && !world.isClient) {
            System.out.println("Detected block entity");
            tray.markDirty();
            world.updateListeners(pos, state, state, Block.NOTIFY_ALL);
            if (FluidMaps.SIMPLE_ITEM_TO_FLUID.containsKey(stack.getItem())) {
                if (FluidMaps.SIMPLE_ITEM_TO_FLUID.get(stack.getItem()) == tray.storedFluid || tray.storedFluid.isBlank()) {
                    if (tray.amount + FluidMaps.SIMPLE_ITEM_TO_AMOUNT.get(stack.getItem()) <= tray.capacity) {
                        tray.storedFluid = FluidMaps.SIMPLE_ITEM_TO_FLUID.get(stack.getItem());
                        tray.amount = FluidMaps.SIMPLE_ITEM_TO_AMOUNT.get(stack.getItem()) + tray.amount;
                        if (FluidMaps.SIMPLE_ITEM_TO_REMAINDER.containsKey(stack.getItem())) {
                            player.giveItemStack(new ItemStack(FluidMaps.SIMPLE_ITEM_TO_REMAINDER.get(stack.getItem()), 1));
                        }
                        stack.decrement(1);
                        tray.markDirty();
                        world.updateListeners(pos, state, state, Block.NOTIFY_ALL);
                    }
                }
            } else if (stack.get(ModDataComponentTypes.AMOUNT) != null) {
                System.out.println("Detected container");
                Integer amount = stack.get(ModDataComponentTypes.AMOUNT);
                Integer capacity = stack.get(ModDataComponentTypes.CAPACITY);
                FluidVariant fluid = stack.get(ModDataComponentTypes.STORED_FLUID);
                System.out.println("Amount " + amount);
                System.out.println("Capacity" + capacity);

                if (amount <= 0 && tray.amount > 0) {
                    System.out.println("Container empty, jar not");
                    Integer transferFluid = tray.amount > capacity ? capacity : tray.amount;
                    stack.set(ModDataComponentTypes.STORED_FLUID, tray.storedFluid);
                    stack.set(ModDataComponentTypes.AMOUNT, transferFluid);
                    tray.amount -= transferFluid;
                    if (tray.amount <= 0) {
                        tray.storedFluid = FluidVariant.blank();
                    }
                    tray.markDirty();
                    world.updateListeners(pos, state, state, Block.NOTIFY_ALL);
                } else if (amount > 0 && tray.amount <= 0) {
                    System.out.println("Jar empty, container not");
                    Integer transferFluid = tray.capacity < amount ? tray.capacity : amount;
                    stack.set(ModDataComponentTypes.AMOUNT, amount - transferFluid);
                    tray.amount += transferFluid;
                    tray.storedFluid = fluid;
                    if (stack.get(ModDataComponentTypes.AMOUNT) <= 0) {
                        stack.set(ModDataComponentTypes.STORED_FLUID, FluidVariant.blank());
                    }
                    tray.markDirty();
                    world.updateListeners(pos, state, state, Block.NOTIFY_ALL);
                } else if (amount > 0 && fluid == tray.storedFluid) {
                    System.out.println("Both non-empty");
                    if (tray.amount == tray.capacity && amount < capacity) {
                        //from jar to item
                        System.out.println("Jar full, container not");
                        Integer transferFluid = tray.amount > capacity - amount ? capacity - amount : tray.amount;
                        stack.set(ModDataComponentTypes.AMOUNT, amount + transferFluid);
                        stack.set(ModDataComponentTypes.STORED_FLUID, tray.storedFluid);
                        tray.amount -= transferFluid;
                        if (tray.amount <= 0) {
                            tray.storedFluid = FluidVariant.blank();
                        }
                        tray.markDirty();
                        world.updateListeners(pos, state, state, Block.NOTIFY_ALL);

                    } else if (Objects.equals(capacity, amount) && tray.amount < tray.capacity) {
                        //from item to jar
                        System.out.println("Container full, jar not");
                        Integer transferFluid = amount > tray.capacity - tray.amount ? tray.capacity - tray.amount : amount;
                        stack.set(ModDataComponentTypes.AMOUNT, amount - transferFluid);
                        tray.amount += transferFluid;
                        tray.storedFluid = fluid;
                        if (stack.get(ModDataComponentTypes.AMOUNT) <= 0) {
                            stack.set(ModDataComponentTypes.STORED_FLUID, FluidVariant.blank());
                        }
                        tray.markDirty();
                        world.updateListeners(pos, state, state, Block.NOTIFY_ALL);
                    } else if (amount < capacity && tray.amount < tray.capacity) {
                        //to jar from item
                        System.out.println("Both partially full");
                        Integer transferFluid = amount > tray.capacity - tray.amount ? tray.capacity - tray.amount : amount;
                        stack.set(ModDataComponentTypes.AMOUNT, amount - transferFluid);
                        tray.amount += transferFluid;
                        tray.storedFluid = fluid;
                        if (stack.get(ModDataComponentTypes.AMOUNT) <= 0) {
                            stack.set(ModDataComponentTypes.STORED_FLUID, FluidVariant.blank());
                        }
                        tray.markDirty();
                        world.updateListeners(pos, state, state, Block.NOTIFY_ALL);
                    }
                }
            } else if (!player.getMainHandStack().isEmpty()) {
                System.out.println("Input item");
                if (tray.getStack(0).isEmpty()) {
                    tray.setStack(0, stack.copyWithCount(1));
                    stack.decrement(1);
                    tray.markDirty();
                    world.updateListeners(pos, state, state, Block.NOTIFY_ALL);
                } else if (tray.getStack(1).isEmpty()) {
                    tray.setStack(1, stack.copyWithCount(1));
                    stack.decrement(1);
                    tray.markDirty();
                    world.updateListeners(pos, state, state, Block.NOTIFY_ALL);
                } else if (tray.getStack(2).isEmpty()) {
                    tray.setStack(2, stack.copyWithCount(1));
                    stack.decrement(1);
                    tray.markDirty();
                    world.updateListeners(pos, state, state, Block.NOTIFY_ALL);
                }
            } else if (player.getMainHandStack().isEmpty()) {
                System.out.println("Output items");
                player.giveItemStack(tray.getStack(0));
                player.giveItemStack(tray.getStack(1));
                player.giveItemStack(tray.getStack(2));
                tray.clear();
                tray.markDirty();
                world.updateListeners(pos, state, state, Block.NOTIFY_ALL);
            }
            tray.markDirty();
            world.updateListeners(pos, state, state, Block.NOTIFY_ALL);
        }
        return ItemActionResult.SUCCESS;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return TickableBlockEntity.getTicker(world);
    }
}
