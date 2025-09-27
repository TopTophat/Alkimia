package net.falling_pan.alkimia.block.custom;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.item.Item;

import java.util.List;

public class CentrifugeRecipe {
    //rules if there are multiple fluids, and you click with a flask, if empty take the topmost fluid, else take the matching fluid, if it's there
    public List<FluidVariant> inputFluids; //max 4
    public List<Integer> fluidInputAmounts;
    public List<FluidVariant> outputFluids; //max 4
    public List<Integer> fluidOutputAmounts;
    public List<Item> inputItems; //max 5
    public List<Item> outputItems; //max 4
    public int cooktime; //ticks

    public CentrifugeRecipe(List<FluidVariant> inputFluids, List<Integer> fluidInputAmounts, List<FluidVariant> outputFluids, List<Integer> fluidOutputAmounts, List<Item> inputItems, List<Item> outputItems, int cooktime)
    {
        this.inputFluids = inputFluids;
        this.fluidInputAmounts = fluidInputAmounts;
        this.outputFluids = outputFluids;
        this.fluidOutputAmounts = fluidOutputAmounts;
        this.inputItems = inputItems;
        this.outputItems = outputItems;
        this.cooktime = cooktime;
    }

    //example recipes
        //2 orange dyes -> red dye + yellow dye
        //soul sand -> sand + souls
        //souls + lava -> diamond dust
        //5 sand -> 4 iron nuggets

}
