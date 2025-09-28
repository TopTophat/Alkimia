package net.toptophat.alkimia.block.custom;

import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.fluid.Fluids;
import net.toptophat.alkimia.block.ModBlocks;
import net.toptophat.alkimia.block.entity.custom.TransmutingPedestalBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.toptophat.alkimia.fluid.ModFluids;
import net.toptophat.alkimia.item.ModItems;
import net.toptophat.alkimia.util.TickableBlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TransmutingPedestal extends BlockWithEntity implements BlockEntityProvider {
    public static final BooleanProperty IS_FAST = BooleanProperty.of("is_fast");
    public static final MapCodec<TransmutingPedestal> CODEC = TransmutingPedestal.createCodec(TransmutingPedestal::new);
    public static List<TransmutingRecipe> transmuting_recipe_book;

    public static void InitRecipes() {
        transmuting_recipe_book = List.of(
                new TransmutingRecipe(List.of(FluidVariant.blank(), FluidVariant.blank(), FluidVariant.blank(), FluidVariant.blank()),
                        List.of(Items.LAPIS_LAZULI, Items.LAPIS_LAZULI, Items.LAPIS_LAZULI, Items.LAPIS_LAZULI),
                        AlchemicalCrucible.aspectToInt(List.of()),
                        Items.LAPIS_LAZULI,
                        Items.DIAMOND,
                        600)
        );
    }

    public TransmutingPedestal(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(IS_FAST, false));
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new TransmutingPedestalBlockEntity(pos, state);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(IS_FAST);
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.INVISIBLE;
    }

    @Override
    protected void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof TransmutingPedestalBlockEntity) {
                ItemScatterer.spawn(world, pos, ((TransmutingPedestalBlockEntity) blockEntity));
                world.updateComparators(pos, this);
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    @Override
    protected ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.getBlockEntity(pos) instanceof TransmutingPedestalBlockEntity pedestal) {
            if (pedestal.isEmpty() && !stack.isEmpty() && stack.getItem() != Items.STICK) {
                pedestal.setStack(0, stack.copyWithCount(1));
                world.playSound(player, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 1f, 2f);
                stack.decrement(1);

                pedestal.markDirty();
                world.updateListeners(pos, state, state, 0);
            } else if (stack.isEmpty()) {
                ItemStack stackOnPedestal = pedestal.getStack(0);
                player.setStackInHand(Hand.MAIN_HAND, stackOnPedestal);
                world.playSound(player, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 1f, 1f);
                pedestal.clear();

                pedestal.markDirty();
                world.updateListeners(pos, state, state, 0);
            } else if (stack.getItem() == Items.STICK) {
                world.setBlockState(pos, ModBlocks.TRANSMUTING_PEDESTAL.getDefaultState().with(IS_FAST, !world.getBlockState(pos).get(IS_FAST)));
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
