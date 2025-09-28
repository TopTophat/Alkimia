package net.toptophat.alkimia.block.entity.custom;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.block.Block;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.toptophat.alkimia.block.ModBlocks;
import net.toptophat.alkimia.block.custom.*;
import net.toptophat.alkimia.block.entity.ImplementedInventory;
import net.toptophat.alkimia.block.entity.ModBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.toptophat.alkimia.fluid.ModFluids;
import net.toptophat.alkimia.util.TickableBlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static net.toptophat.alkimia.block.custom.TransmutingPedestal.transmuting_recipe_book;

public class TransmutingPedestalBlockEntity extends BlockEntity implements ImplementedInventory, TickableBlockEntity {
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(1, ItemStack.EMPTY);
    public int progress = 0;
    public TransmutingRecipe currentRecipe = null;
    public int recipeIndex = -1;
    public float rotation = 0f;  // current rotation angle in degrees
    public float rotationSpeed = 0f;  // current rotation speed

    public TransmutingPedestalBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.TRANSMUTING_PEDESTAL_BE, pos, state);
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        Inventories.writeNbt(nbt, inventory, registryLookup);
        nbt.putInt("recipeIndex", this.recipeIndex);
        nbt.putInt("progress", this.progress);
        nbt.putFloat("rotation", this.rotation);
        nbt.putFloat("rotationSpeed", this.rotationSpeed);
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
        this.rotation = nbt.getFloat("rotation");
        this.rotationSpeed = nbt.getFloat("rotationSpeed");
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
        if (getBlock(pos.down(2)) == ModBlocks.ALCHEMICAL_CRUCIBLE &&
            getBlock(pos.down(2).north(4)) == ModBlocks.ITEM_PEDESTAL_BASE &&
                getBlock(pos.down(2).south(4)) == ModBlocks.ITEM_PEDESTAL_BASE &&
                getBlock(pos.down(2).west(4)) == ModBlocks.ITEM_PEDESTAL_BASE &&
                getBlock(pos.down(2).east(4)) == ModBlocks.ITEM_PEDESTAL_BASE &&
                getBlock(pos.down(2).north(3).east(3)) == ModBlocks.FLUID_PEDESTAL_BASE &&
                getBlock(pos.down(2).south(3).east(3)) == ModBlocks.FLUID_PEDESTAL_BASE &&
                getBlock(pos.down(2).north(3).west(3)) == ModBlocks.FLUID_PEDESTAL_BASE &&
                getBlock(pos.down(2).south(3).west(3)) == ModBlocks.FLUID_PEDESTAL_BASE)
        {
            System.out.println("Assembled");
            return true;
        }
        return false;
    }

    public boolean doesMainItemMatch(TransmutingRecipe recipe)
    {
        System.out.println("does main match" + (recipe.mainItem == inventory.get(0).getItem()));
        return recipe.mainItem == inventory.get(0).getItem();
    }

    public boolean doAspectsMatch(TransmutingRecipe recipe)
    {
        AlchemicalCrucibleBlockEntity crucible = (AlchemicalCrucibleBlockEntity) world.getBlockEntity(pos.down(2));
        System.out.println("do aspects match " + recipe.inputContents.equals(AlchemicalCrucible.getHalf(crucible.getContents())));
        return recipe.inputContents.equals(AlchemicalCrucible.getHalf(crucible.getContents()));
    }

    public boolean areAllFull()
    {
        List<Integer> presentFluids = List.of(getFluidPedestal(Direction.NORTH).amount, getFluidPedestal(Direction.SOUTH).amount, getFluidPedestal(Direction.WEST).amount, getFluidPedestal(Direction.EAST).amount);
        for (int i = 0; i < presentFluids.size(); i++) {
            if (presentFluids.get(i) != 1000 && presentFluids.get(i) != 0)
            {
                return false;
            }
        }
        System.out.println("all full");
        return true;
    }

    public boolean doAllFluidsMatch(TransmutingRecipe recipe)
    {
        boolean isGood = false;
        List<FluidVariant> presentFluids = List.of(getFluidPedestal(Direction.NORTH).storedFluid, getFluidPedestal(Direction.SOUTH).storedFluid, getFluidPedestal(Direction.WEST).storedFluid, getFluidPedestal(Direction.EAST).storedFluid);
        List<FluidVariant> recipeFluids = recipe.inputFluids;
        System.out.println("recipe fluids size " + recipeFluids.size());
        List<Integer> excludePresent = new java.util.ArrayList<>(List.of());
        List<Integer> excludeRecipe = new java.util.ArrayList<>(List.of());

        //are all present fluids in the recipe
        for (int i = 0; i < presentFluids.size(); i++) {
            isGood = false;
            if (presentFluids.get(i) != FluidVariant.blank())
            {
                for (int j = 0; j < recipeFluids.size(); j++) {
                    if (!excludeRecipe.contains(j))
                    {
                        if (presentFluids.get(i) == recipeFluids.get(j))
                        {
                            System.out.println(presentFluids.get(i) + " equals " + recipeFluids.get(j));
                            isGood = true;
                            excludeRecipe.add(j);
                            break;
                        }
                    }
                }
                if (!isGood)
                {
                    System.out.println("not good first fluid check");
                    return false;
                }
            }
        }

        //are all recipe fluids present
        for (int i = 0; i < recipeFluids.size(); i++) {
            isGood = false;
            if (recipeFluids.get(i) != FluidVariant.blank())
            {
                for (int j = 0; j < presentFluids.size(); j++) {
                    if (!excludePresent.contains(j))
                    {
                        if (recipeFluids.get(i) == presentFluids.get(j))
                        {
                            isGood = true;
                            excludePresent.add(j);
                            break;
                        }
                    }
                }
                if (!isGood)
                {
                    System.out.println("not good second fluid check");
                    return false;
                }
            }
        }

        System.out.println("all fluids match");
        return true;
    }

    public boolean doAllItemsMatch(TransmutingRecipe recipe)
    {
        boolean isGood = false;
        List<Item> presentItems = List.of(getItemPedestal(Direction.NORTH).inventory.get(0).getItem(), getItemPedestal(Direction.SOUTH).inventory.get(0).getItem(), getItemPedestal(Direction.WEST).inventory.get(0).getItem(), getItemPedestal(Direction.EAST).inventory.get(0).getItem());
        List<Item> recipeItems = recipe.inputItems;
        List<Integer> excludePresent = new java.util.ArrayList<>(List.of());
        List<Integer> excludeRecipe = new java.util.ArrayList<>(List.of());

        //are all present fluids in the recipe
        for (int i = 0; i < presentItems.size(); i++) {
            isGood = false;
            if (presentItems.get(i) != Items.AIR)
            {
                for (int j = 0; j < recipeItems.size(); j++) {
                    if (!excludeRecipe.contains(j))
                    {
                        if (presentItems.get(i) == recipeItems.get(j))
                        {
                            isGood = true;
                            excludeRecipe.add(j);
                            break;
                        }
                    }
                }
                if (!isGood)
                {
                    return false;
                }
            }
        }

        //are all recipe fluids present
        for (int i = 0; i < recipeItems.size(); i++) {
            isGood = false;
            if (recipeItems.get(i) != Items.AIR)
            {
                for (int j = 0; j < presentItems.size(); j++) {
                    if (!excludePresent.contains(j))
                    {
                        if (recipeItems.get(i) == presentItems.get(j))
                        {
                            isGood = true;
                            excludePresent.add(j);
                            break;
                        }
                    }
                }
                if (!isGood)
                {
                    return false;
                }
            }
        }

        System.out.println("all items match");
        return true;
    }

    public TransmutingRecipe findValidRecipe()
    {
        for (int i = 0; i < transmuting_recipe_book.size(); i++) {
            if (isAssembled() && areAllFull() && doesMainItemMatch(transmuting_recipe_book.get(i)) && doAspectsMatch(transmuting_recipe_book.get(i)) && doAllFluidsMatch(transmuting_recipe_book.get(i)) && doAllItemsMatch(transmuting_recipe_book.get(i)))
            {
                return transmuting_recipe_book.get(i);
            }
        }
        return null;
    }

    public ItemPedestalBlockEntity getItemPedestal(Direction dir)
    {
        return switch (dir)
        {
            case NORTH -> (ItemPedestalBlockEntity) world.getBlockEntity(pos.down(2).north(4));
            case SOUTH -> (ItemPedestalBlockEntity) world.getBlockEntity(pos.down(2).south(4));
            case WEST -> (ItemPedestalBlockEntity) world.getBlockEntity(pos.down(2).west(4));
            case EAST -> (ItemPedestalBlockEntity) world.getBlockEntity(pos.down(2).east(4));
            case DOWN, UP -> null;
        };
    }

    public FluidPedestalBlockEntity getFluidPedestal(Direction dir)
    {
        return switch (dir)
        {
            case NORTH -> (FluidPedestalBlockEntity) world.getBlockEntity(pos.down(2).north(3).east(3));
            case SOUTH -> (FluidPedestalBlockEntity) world.getBlockEntity(pos.down(2).south(3).west(3));
            case WEST -> (FluidPedestalBlockEntity) world.getBlockEntity(pos.down(2).west(3).north(3));
            case EAST -> (FluidPedestalBlockEntity) world.getBlockEntity(pos.down(2).east(3).south(3));
            default -> null;
        };
    }

    public Block getBlock(BlockPos pos)
    {
        return world.getBlockState(pos).getBlock();
    }

    @Override
    public void tick() {
        if (findValidRecipe() != null)
        {
            if (findValidRecipe() != currentRecipe)
            {
                progress = 0;
                setFast(false);
                if (findValidRecipe() != null)
                {
                    progress++;
                    currentRecipe = findValidRecipe();
                }
            }
            else
            {
                progress++;
                setFast(true);
                if (progress >= findValidRecipe().cooktime)
                {
                    progress = 0;
                    finishRecipe(currentRecipe);
                    currentRecipe = null;
                }
            }
        }
        else if (isAssembled())
        {
            currentRecipe = null;
            progress = 0;
            setFast(false);
        }

        recipeIndex = currentRecipe == null ? -1 : transmuting_recipe_book.indexOf(currentRecipe);

        if (this.recipeIndex != -1 && this.getWorld() != null) {
            float targetSpeed = 32f * ((float) this.progress / TransmutingPedestal.transmuting_recipe_book.get(this.recipeIndex).cooktime);
            rotationSpeed += (targetSpeed - rotationSpeed) * 0.05f; // smooth acceleration
            rotation += rotationSpeed;
        } else {
            // slow down or reset
            rotationSpeed *= 0.9f;
            rotation += rotationSpeed;
        }

        world.updateListeners(pos, getCachedState(), getCachedState(), Block.NOTIFY_ALL);
    }

    public void finishRecipe(TransmutingRecipe recipe)
    {
        getItemPedestal(Direction.NORTH).clear();
        getItemPedestal(Direction.SOUTH).clear();
        getItemPedestal(Direction.WEST).clear();
        getItemPedestal(Direction.EAST).clear();

        getFluidPedestal(Direction.NORTH).amount = 0;
        getFluidPedestal(Direction.SOUTH).amount = 0;
        getFluidPedestal(Direction.WEST).amount = 0;
        getFluidPedestal(Direction.EAST).amount = 0;

        getFluidPedestal(Direction.NORTH).storedFluid = FluidVariant.blank();
        getFluidPedestal(Direction.SOUTH).storedFluid = FluidVariant.blank();
        getFluidPedestal(Direction.WEST).storedFluid = FluidVariant.blank();
        getFluidPedestal(Direction.EAST).storedFluid = FluidVariant.blank();

        AlchemicalCrucibleBlockEntity crucible =  (AlchemicalCrucibleBlockEntity) world.getBlockEntity(pos.down(2));
        crucible.setContents(new ArrayList<>(List.of(0, 0, 0, 0, 0, 0)));
        crucible.setDone(false);
        crucible.setBase(FluidVariant.blank());
        crucible.setFillLevel(0);
        crucible.clear();

        inventory.set(0, recipe.resultItem.getDefaultStack().copyWithCount(1));

        getItemPedestal(Direction.NORTH).markDirty();
        getItemPedestal(Direction.SOUTH).markDirty();
        getItemPedestal(Direction.WEST).markDirty();
        getItemPedestal(Direction.EAST).markDirty();
        getFluidPedestal(Direction.NORTH).markDirty();
        getFluidPedestal(Direction.SOUTH).markDirty();
        getFluidPedestal(Direction.WEST).markDirty();
        getFluidPedestal(Direction.EAST).markDirty();
        crucible.markDirty();
        markDirty();

        world.updateListeners(getItemPedestal(Direction.NORTH).getPos(), getItemPedestal(Direction.NORTH).getCachedState(), getItemPedestal(Direction.NORTH).getCachedState(), Block.NOTIFY_ALL);
        world.updateListeners(getItemPedestal(Direction.SOUTH).getPos(), getItemPedestal(Direction.SOUTH).getCachedState(), getItemPedestal(Direction.SOUTH).getCachedState(), Block.NOTIFY_ALL);
        world.updateListeners(getItemPedestal(Direction.EAST).getPos(), getItemPedestal(Direction.EAST).getCachedState(), getItemPedestal(Direction.EAST).getCachedState(), Block.NOTIFY_ALL);
        world.updateListeners(getItemPedestal(Direction.WEST).getPos(), getItemPedestal(Direction.WEST).getCachedState(), getItemPedestal(Direction.WEST).getCachedState(), Block.NOTIFY_ALL);

        world.updateListeners(getFluidPedestal(Direction.NORTH).getPos(), getFluidPedestal(Direction.NORTH).getCachedState(), getFluidPedestal(Direction.NORTH).getCachedState(), Block.NOTIFY_ALL);
        world.updateListeners(getFluidPedestal(Direction.SOUTH).getPos(), getFluidPedestal(Direction.SOUTH).getCachedState(), getFluidPedestal(Direction.SOUTH).getCachedState(), Block.NOTIFY_ALL);
        world.updateListeners(getFluidPedestal(Direction.EAST).getPos(), getFluidPedestal(Direction.EAST).getCachedState(), getFluidPedestal(Direction.EAST).getCachedState(), Block.NOTIFY_ALL);
        world.updateListeners(getFluidPedestal(Direction.WEST).getPos(), getFluidPedestal(Direction.WEST).getCachedState(), getFluidPedestal(Direction.WEST).getCachedState(), Block.NOTIFY_ALL);

        world.updateListeners(crucible.getPos(), crucible.getCachedState(), crucible.getCachedState(), Block.NOTIFY_ALL);

        world.updateListeners(pos, getCachedState(), getCachedState(), Block.NOTIFY_ALL);
    }

    public void setFast(boolean set)
    {
        world.setBlockState(getItemPedestal(Direction.NORTH).getPos(), ModBlocks.ITEM_PEDESTAL_BASE.getDefaultState().with(ItemPedestal.IS_FAST, set));
        world.setBlockState(getItemPedestal(Direction.SOUTH).getPos(), ModBlocks.ITEM_PEDESTAL_BASE.getDefaultState().with(ItemPedestal.IS_FAST, set));
        world.setBlockState(getItemPedestal(Direction.WEST).getPos(), ModBlocks.ITEM_PEDESTAL_BASE.getDefaultState().with(ItemPedestal.IS_FAST, set));
        world.setBlockState(getItemPedestal(Direction.EAST).getPos(), ModBlocks.ITEM_PEDESTAL_BASE.getDefaultState().with(ItemPedestal.IS_FAST, set));

        world.setBlockState(getFluidPedestal(Direction.NORTH).getPos(), ModBlocks.FLUID_PEDESTAL_BASE.getDefaultState().with(FluidPedestal.IS_FAST, set));
        world.setBlockState(getFluidPedestal(Direction.SOUTH).getPos(), ModBlocks.FLUID_PEDESTAL_BASE.getDefaultState().with(FluidPedestal.IS_FAST, set));
        world.setBlockState(getFluidPedestal(Direction.WEST).getPos(), ModBlocks.FLUID_PEDESTAL_BASE.getDefaultState().with(FluidPedestal.IS_FAST, set));
        world.setBlockState(getFluidPedestal(Direction.EAST).getPos(), ModBlocks.FLUID_PEDESTAL_BASE.getDefaultState().with(FluidPedestal.IS_FAST, set));

        world.setBlockState(pos, ModBlocks.TRANSMUTING_PEDESTAL.getDefaultState().with(TransmutingPedestal.IS_FAST, set));
    }
}
