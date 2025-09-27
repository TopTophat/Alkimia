package net.toptophat.alkimia.block.custom;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.item.Item;

import java.util.List;

public class InfuserRecipe {
    //rules if there are multiple fluids, and you click with a flask, if empty take the topmost fluid, else take the matching fluid, if it's there
    public List<FluidVariant> inputFluids; //max 4
    public List<Integer> fluidInputAmounts;
    public Item inputItem;
    public Item outputItem;
    public int cooktime; //ticks

    public InfuserRecipe(List<FluidVariant> inputFluids, List<Integer> fluidInputAmounts, Item inputItem, Item outputItem, int cooktime)
    {
        this.inputFluids = inputFluids;
        this.fluidInputAmounts = fluidInputAmounts;
        this.inputItem = inputItem;
        this.outputItem = outputItem;
        this.cooktime = cooktime;
    }

    //example recipe item + fluid -> item
        //50mb of souls + sand -> soul sand
}
