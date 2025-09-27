package net.falling_pan.alkimia.block.custom;

import com.mojang.serialization.MapCodec;
import net.falling_pan.alkimia.block.entity.custom.InfuserOutputBlockEntity;
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

public class InfuserOutput extends BlockWithEntity implements BlockEntityProvider {
    public static final MapCodec<InfuserOutput> CODEC = InfuserOutput.createCodec(InfuserOutput::new);

    public InfuserOutput(Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new InfuserOutputBlockEntity(pos, state);
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType
            options) {
        tooltip.add(Text.translatable("tooltip.alkimia.infuser_output.tooltip"));
        super.appendTooltip(stack, context, tooltip, options);
    }

    @Override
    protected ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient)
        {
            if (world.getBlockEntity(pos) instanceof InfuserOutputBlockEntity infuser)
            {
                if((infuser.getStack(0).isEmpty()))
                {
                    infuser.setStack(0, stack.copyWithCount(1));
                    stack.decrement(1);
                    infuser.markDirty();
                    world.updateListeners(pos, state, state, Block.NOTIFY_ALL);
                }
                else if(player.getMainHandStack().isEmpty())
                {
                    player.giveItemStack(infuser.getStack(0));
                    infuser.clear();
                    infuser.markDirty();
                    world.updateListeners(pos, state, state, Block.NOTIFY_ALL);
                }
            }
        }
        return ItemActionResult.SUCCESS;
    }
}
