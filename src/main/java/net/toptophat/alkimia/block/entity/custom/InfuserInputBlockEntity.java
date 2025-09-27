package net.toptophat.alkimia.block.entity.custom;

import com.mojang.serialization.DataResult;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.toptophat.alkimia.block.ModBlocks;
import net.toptophat.alkimia.block.custom.*;
import net.toptophat.alkimia.block.entity.ModBlockEntities;
import net.toptophat.alkimia.util.TickableBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class InfuserInputBlockEntity extends BlockEntity implements TickableBlockEntity {
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
    public InfuserRecipe lastRecipe = null;

    public InfuserInputBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.INFUSER_INPUT_BE, pos, state);
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
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

    public InfuserRecipe findMatchingRecipe()
    {
        boolean isGood = true;
        assert world != null;
        if (world.getBlockEntity(pos.up()) instanceof InfuserOutputBlockEntity out)
       {
           for (int i = 0; i < InfuserInput.infuser_recipe_book.size(); i++) {
               isGood = true;
               InfuserRecipe recipe = InfuserInput.infuser_recipe_book.get(i);
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
                            if (recipe.inputItem != out.inventory.get(0).getItem())
                            {
                                isGood = false;
                            }
                           if (isGood)
                           {
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


    public boolean isThisFluidInTheRecipe(FluidVariant fluid, InfuserRecipe recipe)
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

    private void finishRecipe(InfuserRecipe recipe) {
        if (world != null && !world.isClient)
        {
            if (world.getBlockEntity(pos.up()) instanceof InfuserOutputBlockEntity out)
            {
                out.clear();
                out.inventory.set(0, recipe.outputItem.getDefaultStack().copyWithCount(1));
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
                world.playSound(null, this.pos, SoundEvents.BLOCK_AMETHYST_CLUSTER_BREAK, SoundCategory.BLOCKS, 1, 1);
                markDirty();
                out.markDirty();
                world.updateListeners(pos, world.getBlockState(this.pos), world.getBlockState(this.pos), Block.NOTIFY_ALL);
                world.updateListeners(pos.up(), world.getBlockState(pos.up()), world.getBlockState(pos.up()), Block.NOTIFY_ALL);
            }
        }
    }

    @Override
    public void tick() {
        if (world != null && !world.isClient)
        {
            if (world.getBlockState(pos.up()).getBlock() == ModBlocks.INFUSER_OUTPUT)
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
                                world.updateListeners(pos.up(), world.getBlockState(pos.up()), world.getBlockState(pos.up()), Block.NOTIFY_ALL);
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
                                world.updateListeners(pos.up(), world.getBlockState(pos.up()), world.getBlockState(pos.up()), Block.NOTIFY_ALL);
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
                                world.updateListeners(pos.up(), world.getBlockState(pos.up()), world.getBlockState(pos.up()), Block.NOTIFY_ALL);
                            }
                        }
                    }
                }
            }
        }
    }
}
