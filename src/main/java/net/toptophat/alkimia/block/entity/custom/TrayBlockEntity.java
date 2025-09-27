package net.toptophat.alkimia.block.entity.custom;

import com.mojang.serialization.DataResult;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.toptophat.alkimia.block.ModBlocks;
import net.toptophat.alkimia.block.custom.CalcifyingRecipe;
import net.toptophat.alkimia.block.custom.Stove;
import net.toptophat.alkimia.block.custom.Tray;
import net.toptophat.alkimia.block.entity.ImplementedInventory;
import net.toptophat.alkimia.block.entity.ModBlockEntities;
import net.toptophat.alkimia.util.TickableBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class TrayBlockEntity extends BlockEntity implements ImplementedInventory, TickableBlockEntity {
    public int amount = 0; // mB
    public FluidVariant storedFluid = FluidVariant.blank();
    public int capacity = 1000; // mB
    public int progress = 0;
    public CalcifyingRecipe lastRecipe = null;
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(3, ItemStack.EMPTY);

    public TrayBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.TRAY_BE, pos, state);
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);

        // amount first (optional)
        nbt.putInt("amount", this.amount);
        nbt.putInt("capacity", this.capacity);

        // Serialize the FluidVariant using its CODEC
        DataResult<NbtElement> encoded = FluidVariant.CODEC.encodeStart(NbtOps.INSTANCE, storedFluid);
        encoded.result().ifPresent(elem -> nbt.put("storedFluid", elem));
        // If you want to clear when blank/empty, you can also:
        // else nbt.remove("Fluid");

        Inventories.writeNbt(nbt, inventory, registryLookup);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);

        amount = nbt.getInt("amount");
        capacity = nbt.getInt("capacity");

        if (nbt.contains("storedFluid")) {
            storedFluid = FluidVariant.CODEC
                    .parse(NbtOps.INSTANCE, nbt.get("storedFluid"))
                    .result()
                    .orElse(FluidVariant.blank());
        } else {
            storedFluid = FluidVariant.blank();
        }

        for (int i = 0; i < inventory.size(); i++) {
            inventory.set(i, ItemStack.EMPTY);
        }

        Inventories.readNbt(nbt, inventory, registryLookup);
        System.out.println("CLIENT received: " + nbt);
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

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    public CalcifyingRecipe findMatchingRecipe()
    {
        for (int i = 0; i < Tray.calcifying_recipe_book.size(); i++) {
            CalcifyingRecipe recipe = Tray.calcifying_recipe_book.get(i);
            if (recipe.itemInputOne == inventory.get(0).getItem() && recipe.itemInputTwo == inventory.get(1).getItem() && recipe.itemInputThree == inventory.get(2).getItem() && recipe.fluidInput == storedFluid)
            {
                if (amount >= recipe.fluidInputAmount && world.getBlockEntity(pos.down()) instanceof CalcifyingCrucibleBlockEntity crucible)
                {
                    if (crucible.storedFluid == recipe.fluidOutput || crucible.storedFluid.isBlank())
                    {
                        if (crucible.amount + recipe.fluidOutputAmount <= crucible.capacity)
                        {
                            return recipe;
                        }
                    }
                }
            }
        }
        return null;
    }

    @Override
    public void tick() {
        if (world != null && !world.isClient)
        {
            if (world.getBlockState(this.pos.down()).getBlock() == ModBlocks.CALCIFYING_CRUCIBLE)
            {
                if (world.getBlockState(pos.down(2)).getBlock() == ModBlocks.STOVE && world.getBlockState(pos.down(2)).get(Stove.IS_LIT))
                {
                    if (findMatchingRecipe() != null)
                    {
                        if (lastRecipe == null)
                        {
                            progress = 0;
                            progress++;
                            lastRecipe = findMatchingRecipe();
                            if (progress >= findMatchingRecipe().duration)
                            {
                                finishRecipe(findMatchingRecipe());
                                lastRecipe = null;
                                markDirty();
                                world.updateListeners(pos, world.getBlockState(this.pos), world.getBlockState(this.pos), Block.NOTIFY_ALL);
                                world.updateListeners(pos.down(), world.getBlockState(this.pos.down()), world.getBlockState(this.pos.down()), Block.NOTIFY_ALL);
                            }
                        }
                        else if (lastRecipe != findMatchingRecipe())
                        {
                            progress = 0;
                            progress++;
                            lastRecipe = findMatchingRecipe();
                            if (progress >= findMatchingRecipe().duration)
                            {
                                finishRecipe(findMatchingRecipe());
                                lastRecipe = null;
                                markDirty();
                                world.updateListeners(pos, world.getBlockState(this.pos), world.getBlockState(this.pos), Block.NOTIFY_ALL);
                                world.updateListeners(pos.down(), world.getBlockState(this.pos.down()), world.getBlockState(this.pos.down()), Block.NOTIFY_ALL);
                            }
                        }
                        if (lastRecipe == findMatchingRecipe())
                        {
                            progress++;
                            lastRecipe = findMatchingRecipe();
                            if (progress >= findMatchingRecipe().duration)
                            {
                                finishRecipe(findMatchingRecipe());
                                lastRecipe = null;
                                markDirty();
                                world.updateListeners(pos, world.getBlockState(this.pos), world.getBlockState(this.pos), Block.NOTIFY_ALL);
                                world.updateListeners(pos.down(), world.getBlockState(this.pos.down()), world.getBlockState(this.pos.down()), Block.NOTIFY_ALL);
                            }
                        }
                    }
                }
            }
        }
    }

    private void finishRecipe(CalcifyingRecipe recipe) {
        if (world != null && !world.isClient)
        {
            clear();
            inventory.set(0, recipe.itemRemainderOne.getDefaultStack().copyWithCount(1));
            inventory.set(1, recipe.itemRemainderTwo.getDefaultStack().copyWithCount(1));
            inventory.set(2, recipe.itemRemainderThree.getDefaultStack().copyWithCount(1));
            markDirty();
            world.updateListeners(pos, world.getBlockState(this.pos), world.getBlockState(this.pos), Block.NOTIFY_ALL);
            world.updateListeners(pos.down(), world.getBlockState(this.pos.down()), world.getBlockState(this.pos.down()), Block.NOTIFY_ALL);
            if (world.getBlockState(this.pos.down()).getBlock() == ModBlocks.CALCIFYING_CRUCIBLE && world.getBlockEntity(pos.down()) instanceof CalcifyingCrucibleBlockEntity crucible)
            {
                crucible.storedFluid = recipe.fluidOutput;
                crucible.amount += recipe.fluidOutputAmount;
            }
            amount -= recipe.fluidInputAmount;
            if (amount <= 0)
            {
                storedFluid = FluidVariant.blank();
            }
            progress = 0;
            world.playSound(null, this.pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 1, 1);
            markDirty();
            world.updateListeners(pos, world.getBlockState(this.pos), world.getBlockState(this.pos), Block.NOTIFY_ALL);
            world.updateListeners(pos.down(), world.getBlockState(this.pos.down()), world.getBlockState(this.pos.down()), Block.NOTIFY_ALL);
        }
    }

    @Override
    public void markDirty() {
        super.markDirty();
        sync();
    }

    public void sync() {
        if (world != null && !world.isClient) {
            // Send the BlockEntityUpdateS2CPacket
            ((ServerWorld) world).getChunkManager().markForUpdate(pos);

            // Force block update so renderer refreshes
            world.updateListeners(pos, getCachedState(), getCachedState(), Block.NOTIFY_ALL);

            // Still mark for saving
            super.markDirty();
        }
    }
}
