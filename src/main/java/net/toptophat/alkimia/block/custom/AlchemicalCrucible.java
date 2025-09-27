package net.toptophat.alkimia.block.custom;

import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.toptophat.alkimia.Alkimia;
import net.toptophat.alkimia.block.ModBlocks;
import net.toptophat.alkimia.block.entity.custom.AlchemicalCrucibleBlockEntity;
import net.toptophat.alkimia.component.ModDataComponentTypes;
import net.toptophat.alkimia.fluid.ModFluids;
import net.toptophat.alkimia.item.ModItems;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class AlchemicalCrucible extends BlockWithEntity implements BlockEntityProvider {
    public static final MapCodec<AlchemicalCrucible> CODEC = AlchemicalCrucible.createCodec(AlchemicalCrucible::new);

    public enum aspect {
        Glow,
        Senses,
        Transparency,
        Liquid,
        Death,
        Metal
    }

    public static List<AlchemicalRecipe> recipe_book;

    private static Map<Item, List<Integer>> ITEM_TO_ASPECT;

    public static void initMaps() {
        ITEM_TO_ASPECT = Map.of(
                Items.GLOW_BERRIES, aspectToInt(new ArrayList<>(List.of(aspect.Glow, aspect.Senses))),
                Items.GLOWSTONE_DUST, aspectToInt(new ArrayList<>(List.of(aspect.Glow))),
                Items.ENDER_EYE, aspectToInt(new ArrayList<>(List.of(aspect.Senses))),
                Items.SLIME_BALL, aspectToInt(new ArrayList<>(List.of(aspect.Liquid))),
                Items.COPPER_INGOT, aspectToInt(new ArrayList<>(List.of(aspect.Metal))),
                Items.LILY_OF_THE_VALLEY, aspectToInt(new ArrayList<>(List.of(aspect.Death)))
        );

        recipe_book = List.of(
                new AlchemicalRecipe(List.of(aspect.Glow, aspect.Senses), Items.SPIDER_EYE, true, ModItems.NIGHT_VISION_POTION, 1, FluidVariant.of(ModFluids.DIAMOND_DUST)),
                new AlchemicalRecipe(List.of(aspect.Death, aspect.Liquid, aspect.Metal), Items.IRON_INGOT, false, ModItems.QUICKSILVER, 2, FluidVariant.of(Fluids.WATER)),
                new AlchemicalRecipe(List.of(aspect.Death, aspect.Liquid, aspect.Metal), Items.GOLD_INGOT, false, ModItems.QUICKSILVER, 5, FluidVariant.of(Fluids.WATER))
        );
    }

    public AlchemicalCrucible(Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType options) {
        tooltip.add(Text.translatable("tooltip.alkimia.alchemical_crucible.tooltip"));
        super.appendTooltip(stack, context, tooltip, options);
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new AlchemicalCrucibleBlockEntity(pos, state);
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    protected void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof AlchemicalCrucibleBlockEntity alchemicalCrucibleBlockEntity) {
                ItemScatterer.spawn(world, pos, alchemicalCrucibleBlockEntity);
                world.updateComparators(pos, this);
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    @Override
    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
        if (world.isClient) return;

        if (!(world.getBlockEntity(pos) instanceof AlchemicalCrucibleBlockEntity crucible)) return;
        if (!isHeated(world, pos)) return;

        if (!(entity instanceof ItemEntity itemEntity)) return;

        if (crucible.isDone()) return;

        if (crucible.getFillLevel() < 5) return;

        ItemStack stack = itemEntity.getStack();
        Item item = stack.getItem();

        for (AlchemicalRecipe alchemicalRecipe : recipe_book) {
            Alkimia.LOGGER.info("Recipe {}", aspectToInt(alchemicalRecipe.contents));
            Alkimia.LOGGER.info("Catalyst {}", alchemicalRecipe.catalyst);
        }

        // 1️⃣ Check if item is a catalyst for current contents
        if (isThereMatchingRecipe(world, pos, item.asItem())) {

            if (findMatchingRecipe(world, pos, item.asItem()).isPotion) {
                crucible.setDone(true);
                crucible.setStack(0, findMatchingRecipe(world, pos, item.asItem()).result.getDefaultStack().copyWithCount(1));
                crucible.setContents(new ArrayList<>(List.of(0, 0, 0, 0, 0, 0))); // reset contents
                System.out.println("This is a potion");
            } else {
                int amount = findMatchingRecipe(world, pos, item.asItem()).amount;
                ItemStack resultStack = findMatchingRecipe(world, pos, item.asItem()).result.getDefaultStack().copyWithCount(amount);

                // Give to nearest player within 5 blocks
                PlayerEntity nearest = world.getClosestPlayer(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 5, false);
                if (nearest != null) {
                    nearest.giveItemStack(resultStack);
                } else {
                    // fallback: drop it in the world if no player nearby
                    world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY() + 1, pos.getZ(), resultStack));
                }

                crucible.setContents(new ArrayList<>(List.of(0, 0, 0, 0, 0, 0))); // reset contents
                System.out.println("This isn't a potion");
            }

            stack.decrement(1);
            System.out.println("Accepted catalyst");
            crucible.markDirty();
            world.updateListeners(pos, state, state, Block.NOTIFY_ALL);
            return;
        }

        // 2️⃣ Check if item contributes aspects
        if (ITEM_TO_ASPECT.containsKey(item)) {
            stack.decrement(1);
            List<Integer> newContents = sumLists(crucible.getContents(), ITEM_TO_ASPECT.get(item));
            crucible.setContents(newContents);
            crucible.markDirty();
            world.updateListeners(pos, state, state, Block.NOTIFY_ALL);
            System.out.println("This accepted an ingredient");
            System.out.println("Contents " + crucible.getContents());
        }
        crucible.markDirty();
        world.updateListeners(pos, state, state, Block.NOTIFY_ALL);
        super.onSteppedOn(world, pos, state, entity);
    }

    @Override
    protected ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.getBlockEntity(pos) instanceof AlchemicalCrucibleBlockEntity alchemicalCrucibleBlockEntity && !world.isClient) {
            if (stack.get(ModDataComponentTypes.AMOUNT) != null && alchemicalCrucibleBlockEntity.getFillLevel() < 5)
            {
                if (stack.get(ModDataComponentTypes.STORED_FLUID) != FluidVariant.blank() && (alchemicalCrucibleBlockEntity.getBase() == FluidVariant.blank() || alchemicalCrucibleBlockEntity.getBase() == stack.get(ModDataComponentTypes.STORED_FLUID)))
                {
                    int transferFluid = ((int)Math.floor((float)stack.get(ModDataComponentTypes.AMOUNT) / 100f)) * 100;
                    stack.set(ModDataComponentTypes.AMOUNT, stack.get(ModDataComponentTypes.AMOUNT) - Math.min(transferFluid, 500 - alchemicalCrucibleBlockEntity.getFillLevel() * 100));
                    alchemicalCrucibleBlockEntity.setFillLevel(alchemicalCrucibleBlockEntity.getFillLevel() + Math.min(transferFluid / 100, (500 - alchemicalCrucibleBlockEntity.getFillLevel() * 100) / 100));
                    alchemicalCrucibleBlockEntity.setBase(stack.get(ModDataComponentTypes.STORED_FLUID));
                    if (stack.get(ModDataComponentTypes.AMOUNT) < 1)
                    {
                        stack.set(ModDataComponentTypes.STORED_FLUID, FluidVariant.blank());
                    }
                    alchemicalCrucibleBlockEntity.markDirty();
                    world.updateListeners(pos, state, state, Block.NOTIFY_ALL);
                }
            }
            if (stack.getItem() == Items.GLASS_BOTTLE && alchemicalCrucibleBlockEntity.isDone())
            {
                stack.decrement(1);
                player.giveItemStack(alchemicalCrucibleBlockEntity.getStack(0).copyWithCount(1));
                alchemicalCrucibleBlockEntity.setFillLevel(alchemicalCrucibleBlockEntity.getFillLevel() - 1);
                if (alchemicalCrucibleBlockEntity.getFillLevel() < 1)
                {
                    alchemicalCrucibleBlockEntity.setDone(false);
                    alchemicalCrucibleBlockEntity.setBase(FluidVariant.blank());
                    alchemicalCrucibleBlockEntity.setStack(0, ItemStack.EMPTY);
                    alchemicalCrucibleBlockEntity.markDirty();
                    world.updateListeners(pos, state, state, Block.NOTIFY_ALL);
                }
                System.out.println("Bottled the potion");
                alchemicalCrucibleBlockEntity.markDirty();
                world.updateListeners(pos, state, state, Block.NOTIFY_ALL);
            }
            alchemicalCrucibleBlockEntity.markDirty();
            world.updateListeners(pos, state, state, Block.NOTIFY_ALL);
        }
        return super.onUseWithItem(stack, state, world, pos, player, hand, hit);
    }

    public boolean isHeated(World world, BlockPos pos) {
        if (world.getBlockEntity(pos) instanceof AlchemicalCrucibleBlockEntity alchemicalCrucibleBlockEntity) {
            if (world.getBlockState(pos.down(1)).getBlock() == ModBlocks.STOVE && world.getBlockState(pos.down(1)).get(Stove.IS_LIT)) {
                return true;
            }
            return false;
        }
        return false;
    }

    public List<Integer> sumLists(List<Integer> list_one, List<Integer> list_two) {
        List<Integer> returnList = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0));
        for (int i = 0; i < returnList.size(); i++) { // ✅ iterate through all 6 aspects
            int val = 0;
            if (list_one != null && i < list_one.size()) {
                val += list_one.get(i);
            }
            if (list_two != null && i < list_two.size()) {
                val += list_two.get(i);
            }
            returnList.set(i, val); // ✅ replace instead of adding
        }
        return returnList;
    }

    public List<Integer> getHalf(List<Integer> list) {
        List<Integer> returnList = new ArrayList<>();
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                returnList.add(list.get(i) / 2);
            }
        }
        return returnList;
    }

    public  static List<Integer> aspectToInt(List<aspect> aspects)
    {
        List<Integer> returnList = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0));
        for (AlchemicalCrucible.aspect aspect : aspects) {
            returnList.set(aspect.ordinal(), returnList.get(aspect.ordinal()) + 1);
        }
        return returnList;
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (world.getBlockEntity(pos) instanceof AlchemicalCrucibleBlockEntity crucible) {
            System.out.println("Done " + crucible.isDone());
            System.out.println("Fill level " + crucible.getFillLevel());
            System.out.println("Contents " + crucible.getContents());
            System.out.println("Inventory " + crucible.getStack(0));
        }
        return ActionResult.SUCCESS;
    }

    public boolean isThereMatchingRecipe(World world, BlockPos pos, Item catalyst)
    {
        if (world.getBlockEntity(pos) instanceof AlchemicalCrucibleBlockEntity crucible)
        {
            for (AlchemicalRecipe alchemicalRecipe : recipe_book) {
                if (Objects.equals(aspectToInt(alchemicalRecipe.contents), getHalf(crucible.getContents())) && alchemicalRecipe.catalyst == catalyst && alchemicalRecipe.base == crucible.getBase()) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    public AlchemicalRecipe findMatchingRecipe(World world, BlockPos pos, Item catalyst)
    {
        if (world.getBlockEntity(pos) instanceof AlchemicalCrucibleBlockEntity crucible)
        {
            for (AlchemicalRecipe alchemicalRecipe : recipe_book) {
                if (Objects.equals(aspectToInt(alchemicalRecipe.contents), getHalf(crucible.getContents())) && alchemicalRecipe.catalyst == catalyst && alchemicalRecipe.base == crucible.getBase()) {
                    return alchemicalRecipe;
                }
            }
            return null;
        }
        return null;
    }
}
