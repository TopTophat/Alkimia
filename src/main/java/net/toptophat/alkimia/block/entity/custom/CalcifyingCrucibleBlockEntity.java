package net.toptophat.alkimia.block.entity.custom;

import com.mojang.serialization.DataResult;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.toptophat.alkimia.block.entity.ModBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class CalcifyingCrucibleBlockEntity extends BlockEntity {
    public int amount = 0; // mB
    public FluidVariant storedFluid = FluidVariant.blank();
    public int capacity = 1000; // mB

    public CalcifyingCrucibleBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CALCIFYING_CRUCIBLE_BE, pos, state);
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
}
