package net.toptophat.alkimia.block.custom;

import com.mojang.serialization.MapCodec;
import net.toptophat.alkimia.block.entity.custom.StoveBlockEntity;
import net.toptophat.alkimia.util.TickableBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class Stove extends BlockWithEntity implements BlockEntityProvider {
    public static final BooleanProperty IS_LIT = BooleanProperty.of("is_lit");
    public static final MapCodec<Stove> CODEC = Stove.createCodec(Stove::new);
    public static Map<Item, Integer> FUEL_TO_SECONDS;

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(IS_LIT);
        builder.add(Properties.HORIZONTAL_FACING);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return super.getPlacementState(ctx).with(Properties.HORIZONTAL_FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    public Stove(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(IS_LIT, false).with(Properties.HORIZONTAL_FACING, Direction.NORTH));
    }

    public static void InitMaps()
    {
        FUEL_TO_SECONDS = Map.of(
                Items.CHARCOAL, 60
        );
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new StoveBlockEntity(pos, state);
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    protected ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.getBlockEntity(pos) instanceof StoveBlockEntity stove && !world.isClient)
        {
            if (FUEL_TO_SECONDS.containsKey(stack.getItem()))
            {
                stove.burnTime += FUEL_TO_SECONDS.get(stack.getItem()) * 20;
                stack.decrement(1);
                world.playSound(null, pos, SoundEvents.ENTITY_BLAZE_SHOOT, SoundCategory.BLOCKS);
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
