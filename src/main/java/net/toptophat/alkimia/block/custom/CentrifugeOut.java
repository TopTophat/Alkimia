package net.toptophat.alkimia.block.custom;

import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.toptophat.alkimia.block.entity.custom.CentrifugeOutBlockEntity;
import net.toptophat.alkimia.component.ModDataComponentTypes;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CentrifugeOut extends BlockWithEntity implements BlockEntityProvider {
    public static final MapCodec<CentrifugeOut> CODEC = CentrifugeOut.createCodec(CentrifugeOut::new);

    public CentrifugeOut(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH));
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CentrifugeOutBlockEntity(pos, state);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(Properties.HORIZONTAL_FACING);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return super.getPlacementState(ctx).with(Properties.HORIZONTAL_FACING, ctx.getHorizontalPlayerFacing());
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType
            options) {
        tooltip.add(Text.translatable("tooltip.alkimia.centrifuge_out.tooltip"));
        super.appendTooltip(stack, context, tooltip, options);
    }

    @Override
    protected ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            if (world.getBlockEntity(pos) instanceof CentrifugeOutBlockEntity centrifuge) {
                if (stack.get(ModDataComponentTypes.AMOUNT) != null) {
                    int amount = stack.get(ModDataComponentTypes.AMOUNT);
                    int capacity = stack.get(ModDataComponentTypes.CAPACITY);
                    FluidVariant fluid = stack.get(ModDataComponentTypes.STORED_FLUID);

                    if (fluid == FluidVariant.blank() && (centrifuge.amount > 0)) {
                        if (centrifuge.amount > 0) {
                            int transferFluid = Math.min(capacity, centrifuge.amount);
                            centrifuge.amount -= transferFluid;
                            stack.set(ModDataComponentTypes.AMOUNT, transferFluid);
                            stack.set(ModDataComponentTypes.STORED_FLUID, centrifuge.storedFluid);
                            if (centrifuge.amount <= 0) {
                                centrifuge.storedFluid = FluidVariant.blank();
                            }
                        }
                        centrifuge.markDirty();
                        world.updateListeners(pos, state, state, Block.NOTIFY_ALL);
                    } else if (fluid == centrifuge.storedFluid) {
                        int transferFluid = Math.min(capacity - amount, centrifuge.amount);
                        centrifuge.amount -= transferFluid;
                        stack.set(ModDataComponentTypes.AMOUNT, amount + transferFluid);
                        if (centrifuge.amount <= 0) {
                            centrifuge.storedFluid = FluidVariant.blank();
                        }
                        centrifuge.markDirty();
                        world.updateListeners(pos, state, state, Block.NOTIFY_ALL);
                    }
                }
                else if (player.getMainHandStack().isEmpty()) {
                    player.giveItemStack(centrifuge.getStack(0));
                    centrifuge.clear();
                    centrifuge.markDirty();
                    world.updateListeners(pos, state, state, Block.NOTIFY_ALL);
                }
            }
        }
        return ItemActionResult.SUCCESS;
    }
}
