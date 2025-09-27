package net.falling_pan.alkimia.item.custom;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.falling_pan.alkimia.block.ModBlocks;
import net.falling_pan.alkimia.component.ModDataComponentTypes;
import net.falling_pan.alkimia.fluid.ModFluids;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

import java.util.List;
import java.util.Map;

public class TinyVialItem extends Item {
    private static Map<Block, Integer> SIMPLE_BLOCK_TO_AMOUNT = Map.of();

    private static Map<Block, FluidVariant> SIMPLE_BLOCK_TO_FLUID = Map.of();

    private static Map<FluidVariant, String> FLUID_TO_STRING = Map.of();

    public static void InitMaps() {
        SIMPLE_BLOCK_TO_AMOUNT =
                Map.of(
                        Blocks.WATER, 1000,
                        Blocks.LAVA, 1000,
                        ModBlocks.DIAMOND_DUST, 1000,
                        ModBlocks.SOUL, 1000,
                        ModBlocks.LIQUID_SOUL, 1000
                );
        SIMPLE_BLOCK_TO_FLUID =
                Map.of(
                        Blocks.WATER, FluidVariant.of(Fluids.WATER),
                        Blocks.LAVA, FluidVariant.of(Fluids.LAVA),
                        ModBlocks.DIAMOND_DUST, FluidVariant.of(ModFluids.DIAMOND_DUST),
                        ModBlocks.SOUL, FluidVariant.of(ModFluids.SOUL),
                        ModBlocks.LIQUID_SOUL, FluidVariant.of(ModFluids.LIQUID_SOUL)
                );
        FLUID_TO_STRING =
                Map.of(
                        FluidVariant.of(Fluids.WATER), "Water",
                        FluidVariant.of(Fluids.LAVA), "Lava",
                        FluidVariant.of(ModFluids.DIAMOND_DUST), "Diamond Dust",
                        FluidVariant.of(ModFluids.SOUL), "Souls",
                        FluidVariant.of(ModFluids.LIQUID_SOUL), "Liquid Souls"
                );
    }

    public TinyVialItem(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        if (stack.get(ModDataComponentTypes.AMOUNT) == null) {
            stack.set(ModDataComponentTypes.AMOUNT, 0);
        }
        if (stack.get(ModDataComponentTypes.CAPACITY) == null) {
            stack.set(ModDataComponentTypes.CAPACITY, 50);
        }
        if (stack.get(ModDataComponentTypes.STORED_FLUID) == null) {
            stack.set(ModDataComponentTypes.STORED_FLUID, FluidVariant.blank());
        }
        Integer capacity = stack.get(ModDataComponentTypes.CAPACITY);
        Integer amount = stack.get(ModDataComponentTypes.AMOUNT);
        FluidVariant fluid = stack.get(ModDataComponentTypes.STORED_FLUID);
        tooltip.add(Text.translatable("tooltip.alkimia.vial.tooltip1", amount, FLUID_TO_STRING.get(fluid)));
        tooltip.add(Text.translatable("tooltip.alkimia.vial.tooltip2", capacity));
        super.appendTooltip(stack, context, tooltip, type);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (!world.isClient) {
            BlockHitResult blockHitResult = raycast(
                    world, user, RaycastContext.FluidHandling.SOURCE_ONLY
            );
            if (blockHitResult.getType() == HitResult.Type.MISS) {
                return TypedActionResult.pass(itemStack);
            } else if (blockHitResult.getType() != HitResult.Type.BLOCK) {
                return TypedActionResult.pass(itemStack);
            } else {
                BlockPos blockPos = blockHitResult.getBlockPos();
                if (itemStack.get(ModDataComponentTypes.AMOUNT) == null) {
                    itemStack.set(ModDataComponentTypes.AMOUNT, 0);
                }
                if (itemStack.get(ModDataComponentTypes.CAPACITY) == null) {
                    itemStack.set(ModDataComponentTypes.CAPACITY, 100);
                }
                if (itemStack.get(ModDataComponentTypes.STORED_FLUID) == null || itemStack.get(ModDataComponentTypes.AMOUNT) == 0) {
                    itemStack.set(ModDataComponentTypes.STORED_FLUID, FluidVariant.blank());
                }
                Integer capacity = itemStack.get(ModDataComponentTypes.CAPACITY);
                Integer amount = itemStack.get(ModDataComponentTypes.AMOUNT);
                FluidVariant fluid = itemStack.get(ModDataComponentTypes.STORED_FLUID);
                Block block = world.getBlockState(blockPos).getBlock();

                if (SIMPLE_BLOCK_TO_FLUID.containsKey(block)) {
                    if (fluid == SIMPLE_BLOCK_TO_FLUID.get(block) || fluid == FluidVariant.blank()) {
                        itemStack.set(ModDataComponentTypes.STORED_FLUID, SIMPLE_BLOCK_TO_FLUID.get(block));
                        if (amount + SIMPLE_BLOCK_TO_AMOUNT.get(block) <= capacity) {
                            itemStack.set(ModDataComponentTypes.AMOUNT, amount + SIMPLE_BLOCK_TO_AMOUNT.get(block));
                            itemStack.set(ModDataComponentTypes.STORED_FLUID, SIMPLE_BLOCK_TO_FLUID.get(block));
                            world.setBlockState(blockPos, Blocks.AIR.getDefaultState());
                        }
                    }
                }
            }
        }
        return TypedActionResult.pass(itemStack);
    }
}
