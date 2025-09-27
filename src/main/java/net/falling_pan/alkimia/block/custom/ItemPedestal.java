package net.falling_pan.alkimia.block.custom;

import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.falling_pan.alkimia.block.ModBlocks;
import net.falling_pan.alkimia.block.entity.custom.ItemPedestalBlockEntity;
import net.falling_pan.alkimia.block.entity.custom.MortarBlockEntity;
import net.falling_pan.alkimia.component.ModDataComponentTypes;
import net.falling_pan.alkimia.fluid.ModFluids;
import net.falling_pan.alkimia.item.ModItems;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class ItemPedestal extends BlockWithEntity implements BlockEntityProvider {
    public static final BooleanProperty IS_FAST = BooleanProperty.of("is_fast");
    public static final MapCodec<ItemPedestal> CODEC = ItemPedestal.createCodec(ItemPedestal::new);

    public ItemPedestal(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(IS_FAST, false));
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ItemPedestalBlockEntity(pos, state);
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
    protected void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof ItemPedestalBlockEntity) {
                ItemScatterer.spawn(world, pos, ((ItemPedestalBlockEntity) blockEntity));
                world.updateComparators(pos, this);
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    @Override
    protected ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.getBlockEntity(pos) instanceof ItemPedestalBlockEntity pedestal) {
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
                world.setBlockState(pos, ModBlocks.ITEM_PEDESTAL_BASE.getDefaultState().with(IS_FAST, !world.getBlockState(pos).get(IS_FAST)));
            }
        }

        return ItemActionResult.SUCCESS;

    }
}
