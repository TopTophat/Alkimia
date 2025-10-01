package net.toptophat.alkimia.block.entity.custom;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
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
    public int recipeIndex = 0;
    public int[] boundTo = new int[3];
    public boolean isBoundValid = false;

    public LensBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.LENS_BE, pos, state);
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        nbt.putIntArray("boundTo", this.boundTo);
        nbt.putBoolean("isBoundValid", this.isBoundValid);
        nbt.putInt("recipeIndex", this.recipeIndex);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        this.boundTo = nbt.getIntArray("boundTo");
        this.isBoundValid = nbt.getBoolean("isBoundValid");
        this.recipeIndex = nbt.getInt("recipeIndex");
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
            if (world.getBlockState(pos.down()).getBlock() == Blocks.QUARTZ_BLOCK && world.getBlockState(pos.down(2)).getBlock() == Blocks.QUARTZ_BLOCK && world.getBlockState(pos.down(3)).getBlock() == Blocks.QUARTZ_BLOCK)
            {
                recipeIndex = 1;
                if (world.getBlockState(new BlockPos(boundTo[0], boundTo[1], boundTo[2])).getBlock() == ModBlocks.LIGHT_COLLECTOR)
                {
                    isBoundValid = true;
                }
            }
            else if (world.getBlockState(pos.down()).getBlock() == Blocks.PURPUR_BLOCK && world.getBlockState(pos.down(2)).getBlock() == Blocks.PURPUR_BLOCK && world.getBlockState(pos.down(3)).getBlock() == Blocks.PURPUR_BLOCK)
            {
                recipeIndex = 2;
                if (world.getBlockState(new BlockPos(boundTo[0], boundTo[1], boundTo[2])).getBlock() == ModBlocks.LIGHT_COLLECTOR)
                {
                    isBoundValid = true;
                }
            }
            else if (world.getBlockState(pos.down()).getBlock() == Blocks.CRYING_OBSIDIAN && world.getBlockState(pos.down(2)).getBlock() == Blocks.CRYING_OBSIDIAN && world.getBlockState(pos.down(3)).getBlock() == Blocks.CRYING_OBSIDIAN)
            {
                recipeIndex = 3;
                if (world.getBlockState(new BlockPos(boundTo[0], boundTo[1], boundTo[2])).getBlock() == ModBlocks.LIGHT_COLLECTOR)
                {
                    isBoundValid = true;
                }
            }
            else {
                recipeIndex = 0;
                isBoundValid = false;
            }
            world.updateListeners(pos, getCachedState(), getCachedState(), Block.NOTIFY_ALL);
        }
    }
}
