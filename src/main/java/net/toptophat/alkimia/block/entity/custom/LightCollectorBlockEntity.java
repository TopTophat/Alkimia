package net.toptophat.alkimia.block.entity.custom;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.toptophat.alkimia.block.entity.ImplementedInventory;
import net.toptophat.alkimia.block.entity.ModBlockEntities;
import net.toptophat.alkimia.component.ModDataComponentTypes;
import net.toptophat.alkimia.item.ModItems;
import net.toptophat.alkimia.util.TickableBlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class LightCollectorBlockEntity extends BlockEntity implements ImplementedInventory, TickableBlockEntity {
    public final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(1, ItemStack.EMPTY);
    public static Map<Item, Block> CRYSTAL_TO_BLOCK;
    public int progress;
    public int recipeIndex; //0 nothing, 1 sun, 2 moon, 3 stars

    public static void initMaps()
    {
        CRYSTAL_TO_BLOCK = Map.of(
                ModItems.SUNLIGHT_COLLECTOR, Blocks.QUARTZ_BLOCK,
                ModItems.MOONLIGHT_COLLECTOR, Blocks.PURPUR_BLOCK,
                ModItems.STARLIGHT_COLLECTOR, Blocks.CRYING_OBSIDIAN
        );
    }

    public LightCollectorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.LIGHT_COLLECTOR_BE, pos, state);
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        Inventories.writeNbt(nbt, inventory, registryLookup);
        nbt.putInt("progress", this.progress);
        nbt.putInt("recipeIndex", this.recipeIndex);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        for (int i = 0; i < inventory.size(); i++) {
            inventory.set(i, ItemStack.EMPTY);
        }
        Inventories.readNbt(nbt, inventory, registryLookup);
        this.recipeIndex = nbt.getInt("recipeIndex");
        this.progress = nbt.getInt("progress");
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

    public boolean isAssembled()
    {
        if (CRYSTAL_TO_BLOCK.containsValue(world.getBlockState(pos.down()).getBlock()))
        {
            for (int i = 0; i < 7; i++) {
                for (int j = 0; j < 7; j++) {
                    if (world.getBlockState(pos.down()).getBlock() != world.getBlockState(pos.down(2).north(j - 3).east(i - 3)).getBlock())
                    {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public void tick() {
        if (isAssembled() && CRYSTAL_TO_BLOCK.containsKey(inventory.getFirst().getItem()) && CRYSTAL_TO_BLOCK.get(inventory.getFirst().getItem()) == world.getBlockState(pos.down()).getBlock())
        {
            if (inventory.getFirst().get(ModDataComponentTypes.LIGHT_AMOUNT) < inventory.getFirst().get(ModDataComponentTypes.LIGHT_CAPACITY))
            {
                if (inventory.getFirst().getItem() == ModItems.SUNLIGHT_COLLECTOR)
                {
                    if (world.isDay())
                    {
                        if (recipeIndex != 1)
                        {
                            recipeIndex = 1;
                            progress = 0;
                        }
                        progress++;
                    }
                }

                if (inventory.getFirst().getItem() == ModItems.MOONLIGHT_COLLECTOR)
                {
                    if (world.isNight() && world.getMoonPhase() != 4)
                    {
                        if (recipeIndex != 2)
                        {
                            recipeIndex = 2;
                            progress = 0;
                        }
                        progress++;
                    }
                }

                if (inventory.getFirst().getItem() == ModItems.STARLIGHT_COLLECTOR)
                {
                    if (world.isNight() && world.getMoonPhase() == 4)
                    {
                        if (recipeIndex != 3)
                        {
                            recipeIndex = 3;
                            progress = 0;
                        }
                        progress++;
                    }
                }
            }
        }
        else {
            recipeIndex = 0;
            progress = 0;
        }
    }

    public int getNeededTime()
    {
        return 10;
    }

    public int getConnectedLenses()
    {
        return 0;
    }

    public float calculateLensBonus()
    {
        float bonus = 1;
        float bonusTotal = 1;
        float lower = 0.6f;
        for (int i = 0; i < getConnectedLenses(); i++) {
            bonusTotal += bonus;
            bonus *= lower;
        }
        return bonusTotal;
    }
}
