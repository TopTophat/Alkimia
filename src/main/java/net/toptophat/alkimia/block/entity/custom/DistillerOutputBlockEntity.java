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

public class DistillerOutputBlockEntity extends BlockEntity{
    public int amount1 = 0; // mB
    public FluidVariant storedFluid1 = FluidVariant.blank();
    public int amount2 = 0; // mB
    public FluidVariant storedFluid2 = FluidVariant.blank();
    public int amount3 = 0; // mB
    public FluidVariant storedFluid3 = FluidVariant.blank();
    public int amount4 = 0; // mB
    public FluidVariant storedFluid4 = FluidVariant.blank();
    public int capacity = 1000; // mB

    public DistillerOutputBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.DISTILLER_OUTPUT_BE, pos, state);
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
}
