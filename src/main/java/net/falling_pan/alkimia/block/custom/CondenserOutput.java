package net.falling_pan.alkimia.block.custom;

import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.falling_pan.alkimia.block.entity.custom.CondenserOutputBlockEntity;
import net.falling_pan.alkimia.component.ModDataComponentTypes;
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

public class CondenserOutput extends BlockWithEntity implements BlockEntityProvider {
    public static final MapCodec<CondenserOutput> CODEC = CondenserOutput.createCodec(CondenserOutput::new);

    public CondenserOutput(Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CondenserOutputBlockEntity(pos, state);
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType
            options) {
        tooltip.add(Text.translatable("tooltip.alkimia.condenser_output.tooltip"));
        super.appendTooltip(stack, context, tooltip, options);
    }

    @Override
    protected ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient)
        {
            if (world.getBlockEntity(pos) instanceof CondenserOutputBlockEntity condenser)
            {
                if (stack.get(ModDataComponentTypes.AMOUNT) != null) {
                    int amount = stack.get(ModDataComponentTypes.AMOUNT);
                    int capacity = stack.get(ModDataComponentTypes.CAPACITY);
                    FluidVariant fluid = stack.get(ModDataComponentTypes.STORED_FLUID);

                    if (fluid == FluidVariant.blank() && (condenser.amount1 > 0 || condenser.amount2 > 0 || condenser.amount3 > 0 || condenser.amount4 > 0)) {
                        if (condenser.amount1 > 0) {
                            int transferFluid = Math.min(capacity, condenser.amount1);
                            condenser.amount1 -= transferFluid;
                            stack.set(ModDataComponentTypes.AMOUNT, transferFluid);
                            stack.set(ModDataComponentTypes.STORED_FLUID, condenser.storedFluid1);
                            if (condenser.amount1 <= 0) {
                                condenser.storedFluid1 = FluidVariant.blank();
                            }
                        } else if (condenser.amount2 > 0) {
                            int transferFluid = Math.min(capacity, condenser.amount2);
                            condenser.amount2 -= transferFluid;
                            stack.set(ModDataComponentTypes.AMOUNT, transferFluid);
                            stack.set(ModDataComponentTypes.STORED_FLUID, condenser.storedFluid2);
                            if (condenser.amount2 <= 0) {
                                condenser.storedFluid2 = FluidVariant.blank();
                            }
                        } else if (condenser.amount3 > 0) {
                            int transferFluid = Math.min(capacity, condenser.amount3);
                            condenser.amount3 -= transferFluid;
                            stack.set(ModDataComponentTypes.AMOUNT, transferFluid);
                            stack.set(ModDataComponentTypes.STORED_FLUID, condenser.storedFluid3);
                            if (condenser.amount3 <= 0) {
                                condenser.storedFluid3 = FluidVariant.blank();
                            }
                        } else {
                            int transferFluid = Math.min(capacity, condenser.amount4);
                            condenser.amount4 -= transferFluid;
                            stack.set(ModDataComponentTypes.AMOUNT, transferFluid);
                            stack.set(ModDataComponentTypes.STORED_FLUID, condenser.storedFluid4);
                            if (condenser.amount4 <= 0) {
                                condenser.storedFluid4 = FluidVariant.blank();
                            }
                        }
                        condenser.markDirty();
                        world.updateListeners(pos, state, state, Block.NOTIFY_ALL);
                    } else if ((fluid == condenser.storedFluid1 || fluid == condenser.storedFluid2 || fluid == condenser.storedFluid3 || fluid == condenser.storedFluid4) && condenser.amount1 + condenser.amount2 + condenser.amount3 + condenser.amount4 < condenser.capacity) {
                        if (fluid == condenser.storedFluid1) {
                            int transferFluid = Math.min(capacity - amount, condenser.amount1);
                            condenser.amount1 -= transferFluid;
                            stack.set(ModDataComponentTypes.AMOUNT, amount + transferFluid);
                            if (condenser.amount1 <= 0) {
                                condenser.storedFluid1 = FluidVariant.blank();
                            }
                        } else if (fluid == condenser.storedFluid2) {
                            int transferFluid = Math.min(capacity - amount, condenser.amount2);
                            condenser.amount2 -= transferFluid;
                            stack.set(ModDataComponentTypes.AMOUNT, amount + transferFluid);
                            if (condenser.amount2 <= 0) {
                                condenser.storedFluid2 = FluidVariant.blank();
                            }
                        } else if (fluid == condenser.storedFluid3) {
                            int transferFluid = Math.min(capacity - amount, condenser.amount3);
                            condenser.amount3 -= transferFluid;
                            stack.set(ModDataComponentTypes.AMOUNT, amount + transferFluid);
                            if (condenser.amount3 <= 0) {
                                condenser.storedFluid3 = FluidVariant.blank();
                            }
                        } else {
                            int transferFluid = Math.min(capacity - amount, condenser.amount4);
                            condenser.amount4 -= transferFluid;
                            stack.set(ModDataComponentTypes.AMOUNT, amount + transferFluid);
                            if (condenser.amount4 <= 0) {
                                condenser.storedFluid4 = FluidVariant.blank();
                            }
                        }
                        condenser.markDirty();
                        world.updateListeners(pos, state, state, Block.NOTIFY_ALL);
                    }
                }
            }
        }
        return ItemActionResult.SUCCESS;
    }
}
