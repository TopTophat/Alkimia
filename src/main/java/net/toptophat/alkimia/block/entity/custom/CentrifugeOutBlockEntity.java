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

public class CentrifugeOutBlockEntity extends BlockEntity implements ImplementedInventory {
    public DefaultedList<ItemStack> inventory = DefaultedList.ofSize(1, ItemStack.EMPTY);
    public int amount = 0; // mB
    public FluidVariant storedFluid = FluidVariant.blank();
    public int capacity = 1000; // mB

    public CentrifugeOutBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CENTRIFUGE_OUT_BE, pos, state);
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        Inventories.writeNbt(nbt, inventory, registryLookup);
        // amount first (optional)
        nbt.putInt("amount", this.amount);
        nbt.putInt("capacity", this.capacity);

        // Serialize the FluidVariant using its CODEC
        DataResult<NbtElement> encoded1 = FluidVariant.CODEC.encodeStart(NbtOps.INSTANCE, storedFluid);
        encoded1.result().ifPresent(elem -> nbt.put("storedFluid", elem));
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
}
