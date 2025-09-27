package net.toptophat.alkimia.block.custom;

import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.toptophat.alkimia.block.entity.custom.DistillerOutputBlockEntity;
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

public class DistillerOutput extends BlockWithEntity implements BlockEntityProvider {
    public static final MapCodec<DistillerOutput> CODEC = DistillerOutput.createCodec(DistillerOutput::new);

    public DistillerOutput(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH));
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new DistillerOutputBlockEntity(pos, state);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(Properties.HORIZONTAL_FACING);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return super.getPlacementState(ctx).with(Properties.HORIZONTAL_FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType
            options) {
        tooltip.add(Text.translatable("tooltip.alkimia.distiller_output.tooltip"));
        super.appendTooltip(stack, context, tooltip, options);
    }

    @Override
    protected ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient)
        {
            if (world.getBlockEntity(pos) instanceof DistillerOutputBlockEntity distiller)
            {
                if (stack.get(ModDataComponentTypes.AMOUNT) != null) {
                    int amount = stack.get(ModDataComponentTypes.AMOUNT);
                    int capacity = stack.get(ModDataComponentTypes.CAPACITY);
                    FluidVariant fluid = stack.get(ModDataComponentTypes.STORED_FLUID);

                    if (fluid == FluidVariant.blank() && (distiller.amount1 > 0 || distiller.amount2 > 0 || distiller.amount3 > 0 || distiller.amount4 > 0)) {
                        if (distiller.amount1 > 0) {
                            int transferFluid = Math.min(capacity, distiller.amount1);
                            distiller.amount1 -= transferFluid;
                            stack.set(ModDataComponentTypes.AMOUNT, transferFluid);
                            stack.set(ModDataComponentTypes.STORED_FLUID, distiller.storedFluid1);
                            if (distiller.amount1 <= 0) {
                                distiller.storedFluid1 = FluidVariant.blank();
                            }
                        } else if (distiller.amount2 > 0) {
                            int transferFluid = Math.min(capacity, distiller.amount2);
                            distiller.amount2 -= transferFluid;
                            stack.set(ModDataComponentTypes.AMOUNT, transferFluid);
                            stack.set(ModDataComponentTypes.STORED_FLUID, distiller.storedFluid2);
                            if (distiller.amount2 <= 0) {
                                distiller.storedFluid2 = FluidVariant.blank();
                            }
                        } else if (distiller.amount3 > 0) {
                            int transferFluid = Math.min(capacity, distiller.amount3);
                            distiller.amount3 -= transferFluid;
                            stack.set(ModDataComponentTypes.AMOUNT, transferFluid);
                            stack.set(ModDataComponentTypes.STORED_FLUID, distiller.storedFluid3);
                            if (distiller.amount3 <= 0) {
                                distiller.storedFluid3 = FluidVariant.blank();
                            }
                        } else {
                            int transferFluid = Math.min(capacity, distiller.amount4);
                            distiller.amount4 -= transferFluid;
                            stack.set(ModDataComponentTypes.AMOUNT, transferFluid);
                            stack.set(ModDataComponentTypes.STORED_FLUID, distiller.storedFluid4);
                            if (distiller.amount4 <= 0) {
                                distiller.storedFluid4 = FluidVariant.blank();
                            }
                        }
                        distiller.markDirty();
                        world.updateListeners(pos, state, state, Block.NOTIFY_ALL);
                    } else if ((fluid == distiller.storedFluid1 || fluid == distiller.storedFluid2 || fluid == distiller.storedFluid3 || fluid == distiller.storedFluid4) && distiller.amount1 + distiller.amount2 + distiller.amount3 + distiller.amount4 < distiller.capacity) {
                        if (fluid == distiller.storedFluid1) {
                            int transferFluid = Math.min(capacity - amount, distiller.amount1);
                            distiller.amount1 -= transferFluid;
                            stack.set(ModDataComponentTypes.AMOUNT, amount + transferFluid);
                            if (distiller.amount1 <= 0) {
                                distiller.storedFluid1 = FluidVariant.blank();
                            }
                        } else if (fluid == distiller.storedFluid2) {
                            int transferFluid = Math.min(capacity - amount, distiller.amount2);
                            distiller.amount2 -= transferFluid;
                            stack.set(ModDataComponentTypes.AMOUNT, amount + transferFluid);
                            if (distiller.amount2 <= 0) {
                                distiller.storedFluid2 = FluidVariant.blank();
                            }
                        } else if (fluid == distiller.storedFluid3) {
                            int transferFluid = Math.min(capacity - amount, distiller.amount3);
                            distiller.amount3 -= transferFluid;
                            stack.set(ModDataComponentTypes.AMOUNT, amount + transferFluid);
                            if (distiller.amount3 <= 0) {
                                distiller.storedFluid3 = FluidVariant.blank();
                            }
                        } else {
                            int transferFluid = Math.min(capacity - amount, distiller.amount4);
                            distiller.amount4 -= transferFluid;
                            stack.set(ModDataComponentTypes.AMOUNT, amount + transferFluid);
                            if (distiller.amount4 <= 0) {
                                distiller.storedFluid4 = FluidVariant.blank();
                            }
                        }
                        distiller.markDirty();
                        world.updateListeners(pos, state, state, Block.NOTIFY_ALL);
                    }
                }
            }
        }
        return ItemActionResult.SUCCESS;
    }
}
