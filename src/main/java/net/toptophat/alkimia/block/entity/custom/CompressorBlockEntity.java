package net.toptophat.alkimia.block.entity.custom;

import com.mojang.serialization.DataResult;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.toptophat.alkimia.block.ModBlocks;
import net.toptophat.alkimia.block.custom.*;
import net.toptophat.alkimia.block.entity.ImplementedInventory;
import net.toptophat.alkimia.block.entity.ModBlockEntities;
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
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CompressorBlockEntity extends BlockEntity implements ImplementedInventory, TickableBlockEntity {
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
    public CompressingRecipe lastRecipe = null;

    public CompressorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.COMPRESSOR_BE, pos, state);
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

    public CompressingRecipe findMatchingRecipe()
    {
        boolean isGood = true;
        assert world != null;
        for (int i = 0; i < Compressor.compressor_recipe_book.size(); i++) {
            isGood = true;
            CompressingRecipe recipe = Compressor.compressor_recipe_book.get(i);
            for (int j = 0; j < recipe.inputFluids.size(); j++) {
                if (getFluidAmount(recipe.inputFluids.get(j)) < recipe.fluidInputAmounts.get(j)) {
                    isGood = false;
                }
            }
            if (isGood) {
                if (isThisFluidInTheRecipe(storedFluid1, recipe) && isThisFluidInTheRecipe(storedFluid2, recipe) && isThisFluidInTheRecipe(storedFluid3, recipe) && isThisFluidInTheRecipe(storedFluid4, recipe)) {

                } else {
                    isGood = false;
                }
                if (isGood) {
                    System.out.println("Starting to process items");
                    List<Integer> exclude = new java.util.ArrayList<>(List.of());
                    for (int j = 0; j < inventory.size(); j++) {
                        if (isThisItemInTheRecipe(inventory.get(j).getItem(), recipe, exclude)) {
                            if (whereIsThisItemInTheRecipe(inventory.get(j).getItem(), recipe, exclude) != 99) {
                                exclude.add(whereIsThisItemInTheRecipe(inventory.get(j).getItem(), recipe, exclude));
                            }
                        } else {
                            isGood = false;
                            break;
                        }
                    }
                    System.out.println("Processed items in the recipe");

                    System.out.println("Starting to process items");
                    List<Integer> exclude2 = new java.util.ArrayList<>(List.of());
                    for (int j = 0; j < recipe.inputItems.size(); j++) {
                        if (isThisItemInTheInventory(recipe.inputItems.get(j), exclude2)) {
                            if (whereIsThisItemInTheInventory(recipe.inputItems.get(j), exclude2) != 99) {
                                exclude2.add(whereIsThisItemInTheInventory(recipe.inputItems.get(j), exclude2));
                            }
                        } else {
                            isGood = false;
                            break;
                        }
                    }
                    System.out.println("Processed items in the inventory");

                    if (isGood) {
                        return recipe;
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

    public boolean isThisFluidInTheRecipe(FluidVariant fluid, CompressingRecipe recipe)
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

    public boolean isThisItemInTheRecipe(Item item, CompressingRecipe recipe, List<Integer> exclude)
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

    public int whereIsThisItemInTheRecipe(Item item, CompressingRecipe recipe, List<Integer> exclude)
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

    private void finishRecipe(CompressingRecipe recipe) {
        if (world != null && !world.isClient)
        {
            clear();
            inventory.set(0, recipe.outputItems.get(0).getDefaultStack().copyWithCount(1));
            inventory.set(1, recipe.outputItems.get(1).getDefaultStack().copyWithCount(1));
            inventory.set(2, recipe.outputItems.get(2).getDefaultStack().copyWithCount(1));
            inventory.set(3, recipe.outputItems.get(3).getDefaultStack().copyWithCount(1));
            inventory.set(4, recipe.outputItems.get(4).getDefaultStack().copyWithCount(1));
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
            world.setBlockState(pos, world.getBlockState(pos).with(Compressor.IS_WORKING, false));
            world.playSound(null, this.pos, SoundEvents.BLOCK_IRON_TRAPDOOR_OPEN, SoundCategory.BLOCKS, 1, 1);
            markDirty();
            world.updateListeners(pos, world.getBlockState(this.pos), world.getBlockState(this.pos), Block.NOTIFY_ALL);
        }
    }

    @Override
    public void tick() {
        if (world != null && !world.isClient)
        {
            if (world.getBlockState(pos.down(1)).getBlock() == ModBlocks.STOVE && world.getBlockState(pos.down(1)).get(Stove.IS_LIT)) {
                if (findMatchingRecipe() != null) {
                    System.out.println("Found matching recipe");
                    if (lastRecipe == null) {
                        world.playSound(null, this.pos, SoundEvents.BLOCK_IRON_TRAPDOOR_CLOSE, SoundCategory.BLOCKS, 1, 1);
                        world.setBlockState(pos, world.getBlockState(pos).with(Compressor.IS_WORKING, true));
                        progress = 0;
                        progress++;
                        lastRecipe = findMatchingRecipe();
                        if (progress >= findMatchingRecipe().cooktime) {
                            finishRecipe(findMatchingRecipe());
                            lastRecipe = null;
                            markDirty();
                            world.updateListeners(pos, world.getBlockState(this.pos), world.getBlockState(this.pos), Block.NOTIFY_ALL);
                        }
                    } else if (lastRecipe != findMatchingRecipe()) {
                        world.playSound(null, this.pos, SoundEvents.BLOCK_IRON_TRAPDOOR_CLOSE, SoundCategory.BLOCKS, 1, 1);
                        world.setBlockState(pos, world.getBlockState(pos).with(Compressor.IS_WORKING, true));
                        progress = 0;
                        progress++;
                        lastRecipe = findMatchingRecipe();
                        if (progress >= findMatchingRecipe().cooktime) {
                            finishRecipe(findMatchingRecipe());
                            lastRecipe = null;
                            markDirty();
                            world.updateListeners(pos, world.getBlockState(this.pos), world.getBlockState(this.pos), Block.NOTIFY_ALL);
                        }
                    } else if (lastRecipe == findMatchingRecipe()) {
                        progress++;
                        lastRecipe = findMatchingRecipe();
                        if (progress >= findMatchingRecipe().cooktime) {
                            finishRecipe(findMatchingRecipe());
                            lastRecipe = null;
                            markDirty();
                            world.updateListeners(pos, world.getBlockState(this.pos), world.getBlockState(this.pos), Block.NOTIFY_ALL);
                        }
                    }
                }
            }
        }
    }
}
