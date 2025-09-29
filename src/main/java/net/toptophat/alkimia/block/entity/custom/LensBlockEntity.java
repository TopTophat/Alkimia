package net.toptophat.alkimia.block.entity.custom;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.toptophat.alkimia.block.ModBlocks;
import net.toptophat.alkimia.block.custom.Stove;
import net.toptophat.alkimia.block.entity.ModBlockEntities;
import net.toptophat.alkimia.util.TickableBlockEntity;
import org.jetbrains.annotations.Nullable;

public class LensBlockEntity extends BlockEntity implements TickableBlockEntity {

    public LensBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.LENS_BE, pos, state);
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
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
    public void tick() {
        if (world != null && !world.isClient)
        {

        }
    }
}
