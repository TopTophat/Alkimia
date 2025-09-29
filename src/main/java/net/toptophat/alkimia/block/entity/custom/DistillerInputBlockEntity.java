package net.toptophat.alkimia.block.entity.custom;

import com.mojang.serialization.DataResult;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.fluid.Fluids;
import net.toptophat.alkimia.block.ModBlocks;
import net.toptophat.alkimia.block.custom.*;
import net.toptophat.alkimia.block.entity.ImplementedInventory;
import net.toptophat.alkimia.block.entity.ModBlockEntities;
import net.toptophat.alkimia.component.ModDataComponentTypes;
import net.toptophat.alkimia.fluid.ModFluids;
import net.toptophat.alkimia.item.ModItems;
import net.toptophat.alkimia.util.TickableBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DistillerInputBlockEntity extends BlockEntity implements ImplementedInventory, TickableBlockEntity {
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(5, ItemStack.EMPTY);
    public int amount1 = 0; // mB
    public FluidVariant storedFluid1 = FluidVariant.blank();
    public int amount2 = 0; // mB
    public FluidVariant storedFluid2 = FluidVariant.blank();
    public int amount3 = 0; // mB
    public FluidVariant storedFluid3 = FluidVariant.blank();
    public int amount4 = 0; // mB
    public FluidVariant storedFluid4 = FluidVariant.blank();
    public int capacity = 1000; // mB
    public int progress = 0;
    public DistillerRecipe lastRecipe = null;

    public DistillerInputBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.DISTILLER_INPUT_BE, pos, state);
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        Inventories.writeNbt(nbt, inventory, registryLookup);
        // amount first (optional)
        nbt.putInt("amount1", this.amount1);
        nbt.putInt("amount2", this.amount2);
        nbt.putInt("amount3", this.amount3);
        nbt.putInt("amount4", this.amount4);
        nbt.putInt("capacity", this.capacity);

        // Serialize the FluidVariant using its CODEC
        DataResult<NbtElement> encoded1 = FluidVariant.CODEC.encodeStart(NbtOps.INSTANCE, storedFluid1);
        encoded1.result().ifPresent(elem -> nbt.put("storedFluid1", elem));
        // If you want to clear when blank/empty, you can also:
        // else nbt.remove("Fluid");
        // Serialize the FluidVariant using its CODEC
        DataResult<NbtElement> encoded2 = FluidVariant.CODEC.encodeStart(NbtOps.INSTANCE, storedFluid2);
        encoded2.result().ifPresent(elem -> nbt.put("storedFluid2", elem));
        // If you want to clear when blank/empty, you can also:
        // else nbt.remove("Fluid");
        // Serialize the FluidVariant using its CODEC
        DataResult<NbtElement> encoded3 = FluidVariant.CODEC.encodeStart(NbtOps.INSTANCE, storedFluid3);
        encoded3.result().ifPresent(elem -> nbt.put("storedFluid3", elem));
        // If you want to clear when blank/empty, you can also:
        // else nbt.remove("Fluid");
        // Serialize the FluidVariant using its CODEC
        DataResult<NbtElement> encoded4 = FluidVariant.CODEC.encodeStart(NbtOps.INSTANCE, storedFluid4);
        encoded4.result().ifPresent(elem -> nbt.put("storedFluid4", elem));
        // If you want to clear when blank/empty, you can also:
        // else nbt.remove("Fluid");
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        for (int i = 0; i < inventory.size(); i++) {
            inventory.set(i, ItemStack.EMPTY);
        }
        Inventories.readNbt(nbt, inventory, registryLookup);
        amount1 = nbt.getInt("amount1");
        amount2 = nbt.getInt("amount2");
        amount3 = nbt.getInt("amount3");
        amount4 = nbt.getInt("amount4");
        capacity = nbt.getInt("capacity");

        if (nbt.contains("storedFluid1")) {
            storedFluid1 = FluidVariant.CODEC
                    .parse(NbtOps.INSTANCE, nbt.get("storedFluid1"))
                    .result()
                    .orElse(FluidVariant.blank());
        } else {
            storedFluid1 = FluidVariant.blank();
        }

        if (nbt.contains("storedFluid2")) {
            storedFluid2 = FluidVariant.CODEC
                    .parse(NbtOps.INSTANCE, nbt.get("storedFluid2"))
                    .result()
                    .orElse(FluidVariant.blank());
        } else {
            storedFluid2 = FluidVariant.blank();
        }

        if (nbt.contains("storedFluid3")) {
            storedFluid3 = FluidVariant.CODEC
                    .parse(NbtOps.INSTANCE, nbt.get("storedFluid3"))
                    .result()
                    .orElse(FluidVariant.blank());
        } else {
            storedFluid3 = FluidVariant.blank();
        }

        if (nbt.contains("storedFluid4")) {
            storedFluid4 = FluidVariant.CODEC
                    .parse(NbtOps.INSTANCE, nbt.get("storedFluid4"))
                    .result()
                    .orElse(FluidVariant.blank());
        } else {
            storedFluid4 = FluidVariant.blank();
        }
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        return createNbt(registryLookup);
    }

    public DistillerRecipe findMatchingRecipe()
    {
        boolean isGood = true;
        assert world != null;
        if (world.getBlockEntity(getWest(world.getBlockState(pos).get(Properties.HORIZONTAL_FACING), pos)) instanceof DistillerOutputBlockEntity out && world.getBlockState(getWest(world.getBlockState(pos).get(Properties.HORIZONTAL_FACING), pos)).get(Properties.HORIZONTAL_FACING) == world.getBlockState(pos).get(Properties.HORIZONTAL_FACING))
       {
           for (int i = 0; i < DistillerInput.distiller_recipe_book.size(); i++) {
               isGood = true;
               DistillerRecipe recipe = DistillerInput.distiller_recipe_book.get(i);
               for (int j = 0; j < recipe.inputFluids.size(); j++) {
                   if (getFluidAmount(recipe.inputFluids.get(j)) < recipe.fluidInputAmounts.get(j))
                   {
                       isGood = false;
                   }
               }
               if (isGood)
               {
                   if (isGood)
                   {
                       if (isThisFluidInTheRecipe(storedFluid1, recipe) && isThisFluidInTheRecipe(storedFluid2, recipe) && isThisFluidInTheRecipe(storedFluid3, recipe) && isThisFluidInTheRecipe(storedFluid4, recipe))
                       {

                       }
                       else
                       {
                           isGood = false;
                       }
                       if (isGood)
                       {
                           System.out.println("Starting to process items");
                           List<Integer> exclude = new java.util.ArrayList<>(List.of());
                           for (int j = 0; j < inventory.size(); j++) {
                                if (isThisItemInTheRecipe(inventory.get(j).getItem(), recipe, exclude))
                                {
                                    if (whereIsThisItemInTheRecipe(inventory.get(j).getItem(), recipe, exclude) != 99)
                                    {
                                        exclude.add(whereIsThisItemInTheRecipe(inventory.get(j).getItem(), recipe, exclude));
                                    }
                                }
                                else
                                {
                                    isGood = false;
                                    break;
                                }
                           }
                           System.out.println("Processed items in the recipe");

                           System.out.println("Starting to process items");
                           List<Integer> exclude2 = new java.util.ArrayList<>(List.of());
                           for (int j = 0; j < recipe.inputItems.size(); j++) {
                               if (isThisItemInTheInventory(recipe.inputItems.get(j), exclude2))
                               {
                                   if (whereIsThisItemInTheInventory(recipe.inputItems.get(j), exclude2) != 99)
                                   {
                                       exclude2.add(whereIsThisItemInTheInventory(recipe.inputItems.get(j), exclude2));
                                   }
                               }
                               else
                               {
                                   isGood = false;
                                   break;
                               }
                           }
                           System.out.println("Processed items in the inventory");

                           if (isGood)
                           {
                               if (out.amount1 + out.amount2 + out.amount3 + out.amount4 + recipeOutputSum(recipe) > out.capacity)
                               {
                                   isGood = false;
                               }
                               if (isGood)
                               {
                                   return recipe;
                               }
                           }
                       }
                   }
               }
           }
       }
        return null;
    }

    public int getFluidAmount(FluidVariant fluid)
    {
        if (storedFluid1 == fluid)
        {
            return amount1;
        }
        else if (storedFluid2 == fluid)
        {
            return amount2;
        }
        else if (storedFluid3 == fluid)
        {
            return amount3;
        }
        else if (storedFluid4 == fluid)
        {
            return amount4;
        }
        return 0;
    }

    public int getItemAmount(Item item)
    {
        int i = 0;
        if (inventory.get(0).getItem() == item)
        {
            i++;
        }
        if (inventory.get(1).getItem() == item)
        {
            i++;
        }
        if (inventory.get(2).getItem() == item)
        {
            i++;
        }
        if (inventory.get(3).getItem() == item)
        {
            i++;
        }
        if (inventory.get(4).getItem() == item)
        {
            i++;
        }
        return i;
    }

    public boolean isThisFluidInTheRecipe(FluidVariant fluid, DistillerRecipe recipe)
    {
        boolean isDetected = false;
        if (fluid == FluidVariant.blank())
        {
            return true;
        }
        for (int i = 0; i < recipe.inputFluids.size(); i++) {
            if (fluid == recipe.inputFluids.get(i))
            {
                isDetected = true;
            }
        }
        return isDetected;
    }

    public BlockPos getWest(Direction dir, BlockPos pos)
    {
        return switch (dir)
        {
            case NORTH -> pos.south();
            case WEST -> pos.east();
            case EAST -> pos.west();
            case SOUTH -> pos.north();
            default -> pos;
        };
    }

    public boolean isThisItemInTheRecipe(Item item, DistillerRecipe recipe, List<Integer> exclude)
    {
        boolean isDetected = false;
        if (item == Items.AIR)
        {
            return true;
        }
        for (int i = 0; i < recipe.inputItems.size(); i++) {
            if (item == recipe.inputItems.get(i) && !exclude.contains(i)) {
                isDetected = true;
                System.out.println("Detected item in the recipe");
            }
        }
        return isDetected;
    }

    public int whereIsThisItemInTheRecipe(Item item, DistillerRecipe recipe, List<Integer> exclude)
    {
        if (item == Items.AIR)
        {
            return 99;
        }
        for (int i = 0; i < recipe.inputItems.size(); i++) {
            if (item == recipe.inputItems.get(i) && !exclude.contains(i)) {
                System.out.println("Where in the recipe, here " + i);
                return i;
            }
        }
        return 99;
    }

    public boolean isThisItemInTheInventory(Item item, List<Integer> exclude)
    {
        boolean isDetected = false;
        if (item == Items.AIR)
        {
            return true;
        }
        for (int i = 0; i < inventory.size(); i++) {
            if (item == inventory.get(i).getItem() && !exclude.contains(i)) {
                isDetected = true;
                System.out.println("Detected item in the inventory");
            }
        }
        return isDetected;
    }

    public int whereIsThisItemInTheInventory(Item item, List<Integer> exclude)
    {
        if (item == Items.AIR)
        {
            return 99;
        }
        for (int i = 0; i < inventory.size(); i++) {
            if (item == inventory.get(i).getItem() && !exclude.contains(i)) {
                System.out.println("Where in the inventory, here " + i);
                return i;
            }
        }
        return 99;
    }

    public int recipeOutputSum(DistillerRecipe recipe)
    {
        int j = 0;
        for (int i = 0; i < recipe.fluidOutputAmounts.size(); i++) {
            i += recipe.fluidOutputAmounts.get(i);
        }
        return j;
    }

    public int whereIsThisFluid(FluidVariant fluid, List<FluidVariant> fluids)
    {
        for (int i = 0; i < fluids.size(); i++) {
            if (fluid == fluids.get(i))
            {
                return i;
            }
        }
        for (int i = 0; i < fluids.size(); i++) {
            if (FluidVariant.blank() == fluids.get(i))
            {
                return i;
            }
        }
        return 4;
    }

    private void finishRecipe(DistillerRecipe recipe) {
        if (world != null && !world.isClient)
        {
            if (world.getBlockEntity(getWest(world.getBlockState(pos).get(Properties.HORIZONTAL_FACING), pos)) instanceof DistillerOutputBlockEntity out && world.getBlockState(getWest(world.getBlockState(pos).get(Properties.HORIZONTAL_FACING), pos)).get(Properties.HORIZONTAL_FACING) == world.getBlockState(pos).get(Properties.HORIZONTAL_FACING))
            {
                if (recipe.isCrystalCollector && inventory.getFirst().get(ModDataComponentTypes.LIGHT_AMOUNT) > 0)
                {
                    System.out.println(inventory.getFirst().get(ModDataComponentTypes.LIGHT_AMOUNT) + " is bigger than 0");
                    inventory.getFirst().set(ModDataComponentTypes.LIGHT_AMOUNT, inventory.getFirst().get(ModDataComponentTypes.LIGHT_AMOUNT) - 1);
                    System.out.println(inventory.getFirst().get(ModDataComponentTypes.LIGHT_AMOUNT) + " new amount 1 lower");
                    for (int i = 0; i < recipe.fluidOutputAmounts.size(); i++) {
                        switch (whereIsThisFluid(recipe.outputFluids.get(i), List.of(out.storedFluid1, out.storedFluid2, out.storedFluid3, out.storedFluid4)))
                        {
                            case 0 -> out.amount1 += recipe.fluidOutputAmounts.get(i);
                            case 1 -> out.amount2 += recipe.fluidOutputAmounts.get(i);
                            case 2 -> out.amount3 += recipe.fluidOutputAmounts.get(i);
                            case 3 -> out.amount4 += recipe.fluidOutputAmounts.get(i);
                        }
                        switch (whereIsThisFluid(recipe.outputFluids.get(i), List.of(out.storedFluid1, out.storedFluid2, out.storedFluid3, out.storedFluid4)))
                        {
                            case 0 -> out.storedFluid1 = recipe.outputFluids.get(i);
                            case 1 -> out.storedFluid2 = recipe.outputFluids.get(i);
                            case 2 -> out.storedFluid3 = recipe.outputFluids.get(i);
                            case 3 -> out.storedFluid4 = recipe.outputFluids.get(i);
                        }
                    }
                    world.playSound(null, this.pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1, 1);
                    markDirty();
                    out.markDirty();
                    world.updateListeners(pos, world.getBlockState(this.pos), world.getBlockState(this.pos), Block.NOTIFY_ALL);
                    world.updateListeners(getWest(world.getBlockState(pos).get(Properties.HORIZONTAL_FACING), pos), world.getBlockState(getWest(world.getBlockState(pos).get(Properties.HORIZONTAL_FACING), pos)), world.getBlockState(getWest(world.getBlockState(pos).get(Properties.HORIZONTAL_FACING), pos)), Block.NOTIFY_ALL);
                    return;
                } else if (recipe.isCrystalCollector && inventory.getFirst().get(ModDataComponentTypes.LIGHT_AMOUNT) == 0) {
                    return;
                }
                clear();
                inventory.set(0, recipe.remainders.get(0).getDefaultStack().copyWithCount(1));
                inventory.set(1, recipe.remainders.get(1).getDefaultStack().copyWithCount(1));
                inventory.set(2, recipe.remainders.get(2).getDefaultStack().copyWithCount(1));
                inventory.set(3, recipe.remainders.get(3).getDefaultStack().copyWithCount(1));
                inventory.set(4, recipe.remainders.get(4).getDefaultStack().copyWithCount(1));
                for (int i = 0; i < recipe.fluidInputAmounts.size(); i++) {
                    switch (whereIsThisFluid(recipe.inputFluids.get(i), List.of(storedFluid1, storedFluid2, storedFluid3, storedFluid4)))
                    {
                        case 0 -> amount1 -= recipe.fluidInputAmounts.get(i);
                        case 1 -> amount2 -= recipe.fluidInputAmounts.get(i);
                        case 2 -> amount3 -= recipe.fluidInputAmounts.get(i);
                        case 3 -> amount4 -= recipe.fluidInputAmounts.get(i);
                    }
                    if (amount1 <= 0)
                    {
                        storedFluid1 = FluidVariant.blank();
                    }
                    if (amount2 <= 0)
                    {
                        storedFluid2 = FluidVariant.blank();
                    }
                    if (amount3 <= 0)
                    {
                        storedFluid3 = FluidVariant.blank();
                    }
                    if (amount4 <= 0)
                    {
                        storedFluid4 = FluidVariant.blank();
                    }
                }
                for (int i = 0; i < recipe.fluidOutputAmounts.size(); i++) {
                    switch (whereIsThisFluid(recipe.outputFluids.get(i), List.of(out.storedFluid1, out.storedFluid2, out.storedFluid3, out.storedFluid4)))
                    {
                        case 0 -> out.amount1 += recipe.fluidOutputAmounts.get(i);
                        case 1 -> out.amount2 += recipe.fluidOutputAmounts.get(i);
                        case 2 -> out.amount3 += recipe.fluidOutputAmounts.get(i);
                        case 3 -> out.amount4 += recipe.fluidOutputAmounts.get(i);
                    }
                    switch (whereIsThisFluid(recipe.outputFluids.get(i), List.of(out.storedFluid1, out.storedFluid2, out.storedFluid3, out.storedFluid4)))
                    {
                        case 0 -> out.storedFluid1 = recipe.outputFluids.get(i);
                        case 1 -> out.storedFluid2 = recipe.outputFluids.get(i);
                        case 2 -> out.storedFluid3 = recipe.outputFluids.get(i);
                        case 3 -> out.storedFluid4 = recipe.outputFluids.get(i);
                    }
                }
                world.playSound(null, this.pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1, 1);
                markDirty();
                out.markDirty();
                world.updateListeners(pos, world.getBlockState(this.pos), world.getBlockState(this.pos), Block.NOTIFY_ALL);
                world.updateListeners(getWest(world.getBlockState(pos).get(Properties.HORIZONTAL_FACING), pos), world.getBlockState(getWest(world.getBlockState(pos).get(Properties.HORIZONTAL_FACING), pos)), world.getBlockState(getWest(world.getBlockState(pos).get(Properties.HORIZONTAL_FACING), pos)), Block.NOTIFY_ALL);
            }
        }
    }

    @Override
    public void tick() {
        if (world != null && !world.isClient)
        {
            if (world.getBlockState(getWest(world.getBlockState(pos).get(Properties.HORIZONTAL_FACING), pos)).getBlock() == ModBlocks.DISTILLER_OUTPUT)
            {
                if (world.getBlockState(pos.down(1)).getBlock() == ModBlocks.STOVE && world.getBlockState(pos.down(1)).get(Stove.IS_LIT))
                {
                    if (findMatchingRecipe() != null)
                    {
                        System.out.println("Found matching recipe");
                        if (lastRecipe == null)
                        {
                            progress = 0;
                            progress++;
                            lastRecipe = findMatchingRecipe();
                            if (progress >= findMatchingRecipe().cooktime)
                            {
                                finishRecipe(findMatchingRecipe());
                                lastRecipe = null;
                                markDirty();
                                world.updateListeners(pos, world.getBlockState(this.pos), world.getBlockState(this.pos), Block.NOTIFY_ALL);
                                world.updateListeners(getWest(world.getBlockState(pos).get(Properties.HORIZONTAL_FACING), pos), world.getBlockState(getWest(world.getBlockState(pos).get(Properties.HORIZONTAL_FACING), pos)), world.getBlockState(getWest(world.getBlockState(pos).get(Properties.HORIZONTAL_FACING), pos)), Block.NOTIFY_ALL);
                            }
                        }
                        else if (lastRecipe != findMatchingRecipe())
                        {
                            progress = 0;
                            progress++;
                            lastRecipe = findMatchingRecipe();
                            if (progress >= findMatchingRecipe().cooktime)
                            {
                                finishRecipe(findMatchingRecipe());
                                lastRecipe = null;
                                markDirty();
                                world.updateListeners(pos, world.getBlockState(this.pos), world.getBlockState(this.pos), Block.NOTIFY_ALL);
                                world.updateListeners(getWest(world.getBlockState(pos).get(Properties.HORIZONTAL_FACING), pos), world.getBlockState(getWest(world.getBlockState(pos).get(Properties.HORIZONTAL_FACING), pos)), world.getBlockState(getWest(world.getBlockState(pos).get(Properties.HORIZONTAL_FACING), pos)), Block.NOTIFY_ALL);
                            }
                        }
                        else if (lastRecipe == findMatchingRecipe())
                        {
                            progress++;
                            lastRecipe = findMatchingRecipe();
                            if (progress >= findMatchingRecipe().cooktime)
                            {
                                finishRecipe(findMatchingRecipe());
                                lastRecipe = null;
                                markDirty();
                                world.updateListeners(pos, world.getBlockState(this.pos), world.getBlockState(this.pos), Block.NOTIFY_ALL);
                                world.updateListeners(getWest(world.getBlockState(pos).get(Properties.HORIZONTAL_FACING), pos), world.getBlockState(getWest(world.getBlockState(pos).get(Properties.HORIZONTAL_FACING), pos)), world.getBlockState(getWest(world.getBlockState(pos).get(Properties.HORIZONTAL_FACING), pos)), Block.NOTIFY_ALL);
                            }
                        }
                    }
                }
            }
        }
    }
}
