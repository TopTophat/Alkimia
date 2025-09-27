package net.toptophat.alkimia.block.custom;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.item.Item;

import java.util.List;

public class CompressingRecipe {
    //rules if there are multiple fluids, and you click with a flask, if empty take the topmost fluid, else take the matching fluid, if it's there
    public List<FluidVariant> inputFluids; //max 4
    public List<Integer> fluidInputAmounts;
    public List<Item> inputItems; //max 5
    public List<Item> outputItems; //max 5
    public int cooktime; //ticks

    public CompressingRecipe(List<FluidVariant> inputFluids, List<Integer> fluidInputAmounts, List<Item> inputItems, List<Item> remainders, int cooktime)
    {
        this.inputFluids = inputFluids;
        this.fluidInputAmounts = fluidInputAmounts;
        this.inputItems = inputItems;
        this.outputItems = remainders;
        this.cooktime = cooktime;
    }

    //example recipes
        //250 liquid soul + 250 soul + 250 diamond dust + 250 lava + 5 quartz -> 1 diamond
}
