package net.toptophat.alkimia.block.custom;

import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.toptophat.alkimia.block.entity.custom.MortarBlockEntity;
import net.toptophat.alkimia.component.ModDataComponentTypes;
import net.toptophat.alkimia.fluid.ModFluids;
import net.toptophat.alkimia.item.ModItems;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class Mortar extends BlockWithEntity implements BlockEntityProvider {
    public static final VoxelShape SHAPE = Block.createCuboidShape(3, 0, 3, 13, 5, 13);
    public static final MapCodec<Mortar> CODEC = Mortar.createCodec(Mortar::new);
    private static Map<Item, Item> GRINDING_RESULT_MAP = Map.of();

    private static Map<Item, Integer> GRINDING_AMOUNT_MAP = Map.of();
    private static Map<Item, Integer> GRINDING_FLUID_AMOUNT_MAP = Map.of();
    private static Map<Item, FluidVariant> GRINDING_FLUID_MAP = Map.of();

    public static void InitMaps() {
        GRINDING_RESULT_MAP =
                Map.of(
                        Items.COBBLESTONE, Items.GRAVEL,
                        Items.GRAVEL, Items.SAND,
                        Items.ICE, Items.SNOW,
                        Items.DIAMOND, Items.AIR,
                        Items.BLAZE_ROD, Items.BLAZE_POWDER
                );

        GRINDING_AMOUNT_MAP =
                Map.of(
                        Items.COBBLESTONE, 2,
                        Items.GRAVEL, 2,
                        Items.ICE, 2,
                        Items.DIAMOND, 0,
                        Items.BLAZE_ROD, 5
                );

        GRINDING_FLUID_AMOUNT_MAP =
                Map.of(
                        Items.COBBLESTONE, 0,
                        Items.GRAVEL, 0,
                        Items.ICE, 0,
                        Items.DIAMOND, 100,
                        Items.BLAZE_ROD, 0
                );

        GRINDING_FLUID_MAP =
                Map.of(
                        Items.COBBLESTONE, FluidVariant.blank(),
                        Items.GRAVEL, FluidVariant.blank(),
                        Items.ICE, FluidVariant.blank(),
                        Items.DIAMOND, FluidVariant.of(ModFluids.DIAMOND_DUST),
                        Items.BLAZE_ROD, FluidVariant.blank()
                );
    }

    public Mortar(Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new MortarBlockEntity(pos, state);
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    protected void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof MortarBlockEntity) {
                ItemScatterer.spawn(world, pos, ((MortarBlockEntity) blockEntity));
                world.updateComparators(pos, this);
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    @Override
    protected ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.getBlockEntity(pos) instanceof MortarBlockEntity mortarBlockEntity) {
            if (stack.get(ModDataComponentTypes.AMOUNT) != null) {
                System.out.println("Detected container");
                Integer amount = stack.get(ModDataComponentTypes.AMOUNT);
                Integer capacity = stack.get(ModDataComponentTypes.CAPACITY);
                FluidVariant fluid = stack.get(ModDataComponentTypes.STORED_FLUID);
                System.out.println("Amount " + amount);
                System.out.println("Capacity" + capacity);

                if (amount <= 0 && mortarBlockEntity.amount > 0) {
                    System.out.println("Retrieving fluid to empty container");
                    Integer transferFluid = mortarBlockEntity.amount > capacity ? capacity : mortarBlockEntity.amount;
                    stack.set(ModDataComponentTypes.STORED_FLUID, mortarBlockEntity.storedFluid);
                    stack.set(ModDataComponentTypes.AMOUNT, transferFluid);
                    mortarBlockEntity.amount -= transferFluid;
                    if (mortarBlockEntity.amount <= 0) {
                        mortarBlockEntity.storedFluid = FluidVariant.blank();
                    }
                    mortarBlockEntity.markDirty();
                    world.updateListeners(pos, state, state, Block.NOTIFY_ALL);
                } else if (amount > 0 && fluid == mortarBlockEntity.storedFluid) {
                    System.out.println("Both non-empty");
                    if (mortarBlockEntity.amount == mortarBlockEntity.capacity && amount < capacity) {
                        //from jar to item
                        System.out.println("Jar full, container not");
                        Integer transferFluid = mortarBlockEntity.amount > capacity - amount ? capacity - amount : mortarBlockEntity.amount;
                        stack.set(ModDataComponentTypes.AMOUNT, amount + transferFluid);
                        stack.set(ModDataComponentTypes.STORED_FLUID, mortarBlockEntity.storedFluid);
                        mortarBlockEntity.amount -= transferFluid;
                        if (mortarBlockEntity.amount <= 0) {
                            mortarBlockEntity.storedFluid = FluidVariant.blank();
                        }
                        mortarBlockEntity.markDirty();
                        world.updateListeners(pos, state, state, Block.NOTIFY_ALL);

                    } else if (amount < capacity && mortarBlockEntity.amount < mortarBlockEntity.capacity) {
                        //to jar from item
                        System.out.println("Both partially full");
                        Integer transferFluid = amount > mortarBlockEntity.capacity - mortarBlockEntity.amount ? mortarBlockEntity.capacity - mortarBlockEntity.amount : amount;
                        stack.set(ModDataComponentTypes.AMOUNT, amount - transferFluid);
                        mortarBlockEntity.amount += transferFluid;
                        mortarBlockEntity.storedFluid = fluid;
                        if (stack.get(ModDataComponentTypes.AMOUNT) <= 0) {
                            stack.set(ModDataComponentTypes.STORED_FLUID, FluidVariant.blank());
                        }
                        mortarBlockEntity.markDirty();
                        world.updateListeners(pos, state, state, Block.NOTIFY_ALL);
                    }
                }
            } else if (mortarBlockEntity.isEmpty() && !stack.isEmpty() && stack.getItem() != ModItems.PESTLE) {
                mortarBlockEntity.setStack(0, stack.copyWithCount(1));
                world.playSound(player, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 1f, 2f);
                stack.decrement(1);

                mortarBlockEntity.markDirty();
                world.updateListeners(pos, state, state, 0);
            } else if (stack.isEmpty()) {
                ItemStack stackOnPedestal = mortarBlockEntity.getStack(0);
                player.setStackInHand(Hand.MAIN_HAND, stackOnPedestal);
                world.playSound(player, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 1f, 1f);
                mortarBlockEntity.clear();

                mortarBlockEntity.markDirty();
                world.updateListeners(pos, state, state, 0);
            } else if (GRINDING_RESULT_MAP.containsKey(mortarBlockEntity.getStack(0).getItem()) && stack.getItem() == ModItems.PESTLE) {
                if (GRINDING_FLUID_MAP.get(mortarBlockEntity.getStack(0).getItem()) == FluidVariant.blank()) {
                    ItemStack groundStack = mortarBlockEntity.getStack(0);
                    mortarBlockEntity.clear();
                    player.giveItemStack(new ItemStack(GRINDING_RESULT_MAP.get(groundStack.getItem()), GRINDING_AMOUNT_MAP.get(groundStack.getItem())));
                    world.playSound(player, pos, SoundEvents.BLOCK_ROOTED_DIRT_BREAK, SoundCategory.BLOCKS, 1f, 2f);

                    mortarBlockEntity.markDirty();
                    world.updateListeners(pos, state, state, 0);
                } else if (GRINDING_FLUID_MAP.get(mortarBlockEntity.getStack(0).getItem()) == mortarBlockEntity.storedFluid && GRINDING_FLUID_AMOUNT_MAP.get(mortarBlockEntity.getStack(0).getItem()) + mortarBlockEntity.amount <= mortarBlockEntity.capacity || mortarBlockEntity.storedFluid.isBlank()) {
                    ItemStack groundStack = mortarBlockEntity.getStack(0);
                    mortarBlockEntity.clear();
                    player.giveItemStack(new ItemStack(GRINDING_RESULT_MAP.get(groundStack.getItem()), GRINDING_AMOUNT_MAP.get(groundStack.getItem())));
                    mortarBlockEntity.storedFluid = GRINDING_FLUID_MAP.get(groundStack.getItem());
                    mortarBlockEntity.amount += GRINDING_FLUID_AMOUNT_MAP.get(groundStack.getItem());
                    world.playSound(player, pos, SoundEvents.BLOCK_ROOTED_DIRT_BREAK, SoundCategory.BLOCKS, 1f, 2f);

                    mortarBlockEntity.markDirty();
                    world.updateListeners(pos, state, state, 0);
                }
            }
        }

        return ItemActionResult.SUCCESS;

    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType
            options) {
        tooltip.add(Text.translatable("tooltip.alkimia.mortar.tooltip"));
        super.appendTooltip(stack, context, tooltip, options);
    }
}
