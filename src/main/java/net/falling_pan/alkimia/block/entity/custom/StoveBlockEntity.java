package net.falling_pan.alkimia.block.entity.custom;

import net.falling_pan.alkimia.block.ModBlocks;
import net.falling_pan.alkimia.block.custom.Stove;
import net.falling_pan.alkimia.block.entity.ModBlockEntities;
import net.falling_pan.alkimia.util.TickableBlockEntity;
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
import org.jetbrains.annotations.Nullable;

public class StoveBlockEntity extends BlockEntity implements TickableBlockEntity {
    public long burnTime = 0;

    public StoveBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.STOVE_BE, pos, state);
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);

        nbt.putLong("burn_time", this.burnTime);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);

        this.burnTime = nbt.getLong("burn_time");
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
            if (burnTime > 0)
            {
                burnTime--;
                if (world.getBlockState(pos).get(Stove.IS_LIT) != null)
                {
                    world.setBlockState(pos, ModBlocks.STOVE.getDefaultState().with(Stove.IS_LIT, true).with(Properties.HORIZONTAL_FACING, world.getBlockState(pos).get(Properties.HORIZONTAL_FACING)));
                }
            }
            else
            {
                if (world.getBlockState(pos).get(Stove.IS_LIT) != null)
                {
                    world.setBlockState(pos, ModBlocks.STOVE.getDefaultState().with(Stove.IS_LIT, false).with(Properties.HORIZONTAL_FACING, world.getBlockState(pos).get(Properties.HORIZONTAL_FACING)));
                }
            }
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
