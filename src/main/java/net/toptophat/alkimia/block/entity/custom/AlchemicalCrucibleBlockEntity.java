package net.toptophat.alkimia.block.entity.custom;

import com.mojang.serialization.DataResult;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.toptophat.alkimia.block.entity.ImplementedInventory;
import net.toptophat.alkimia.block.entity.ModBlockEntities;
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
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AlchemicalCrucibleBlockEntity extends BlockEntity implements ImplementedInventory{
    private FluidVariant base = FluidVariant.blank();
    private int fillLevel = 0;
    private List<Integer> contents = new ArrayList<Integer>(Arrays.asList(0, 0, 0, 0, 0, 0));
    private boolean isDone = false;
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(1, ItemStack.EMPTY);

    public AlchemicalCrucibleBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ALCHEMICAL_CRUCIBLE_BE, pos, state);
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        nbt.putBoolean("isDone", this.isDone);
        Inventories.writeNbt(nbt, inventory, registryLookup);
        nbt.putInt("fillLevel", this.fillLevel);

        // Save contents
        int[] contentsArray = contents.stream().mapToInt(Integer::intValue).toArray();
        nbt.putIntArray("contents", contentsArray);

        Inventories.writeNbt(nbt, inventory, registryLookup);

        // Serialize the FluidVariant using its CODEC
        DataResult<NbtElement> encoded = FluidVariant.CODEC.encodeStart(NbtOps.INSTANCE, base);
        encoded.result().ifPresent(elem -> nbt.put("storedFluid", elem));
        // If you want to clear when blank/empty, you can also:
        // else nbt.remove("Fluid");
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        this.isDone = nbt.getBoolean("isDone");
        Inventories.readNbt(nbt, inventory, registryLookup);
        this.fillLevel = nbt.getInt("fillLevel");

        // Load contents
        if (nbt.contains("contents")) {
            int[] contentsArray = nbt.getIntArray("contents");
            this.contents = new ArrayList<>();
            for (int value : contentsArray) {
                this.contents.add(value);
            }
        } else {
            // fallback in case tag is missing
            this.contents = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0));
        }

        Inventories.readNbt(nbt, inventory, registryLookup);

        if (nbt.contains("storedFluid")) {
            base = FluidVariant.CODEC
                    .parse(NbtOps.INSTANCE, nbt.get("storedFluid"))
                    .result()
                    .orElse(FluidVariant.blank());
        } else {
            base = FluidVariant.blank();
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

    public void setBase(FluidVariant fluid) {this.base = fluid;}

    public FluidVariant getBase() {return this.base;}

    public List<Integer> getContents() {
        return contents;
    }

    public void setContents(List<Integer> contents) {
        this.contents = contents;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public int getFillLevel()
    {
        return fillLevel;
    }

    public void setFillLevel(int fill)
    {
        fillLevel = fill;
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }
}
